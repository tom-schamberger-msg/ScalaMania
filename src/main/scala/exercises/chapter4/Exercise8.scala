package exercises.chapter4

import akka.actor.ActorSystem

import java.util.concurrent.Executors
import scala.collection.immutable.List
import scala.concurrent.{ExecutionContext, Future}

object Exercise8 {

  sealed trait Next[+I]

  case class NextInputs[I](list: List[I]) extends Next[I]
  case object Finished extends Next[Nothing]

  trait Context {
    
    def executionContext: ExecutionContext
    
    def processNext[I, O](source: Source[I], process: Process[I, O]): Future[Next[O]]

    def run[T](sink: Sink[?, T]): Future[T]
    
  }
  
  trait Process[I, O] {

    def process(t: I): List[O]
    
  }

  object Node {

    private var nextId: Long = 0L

    protected def generateId: Long ={
      val id = nextId
      nextId += 1
      id
    }

  }
  
  sealed trait Node {
    
    def init(): Unit = ()

    val id = Node.nextId

    def foreachNode(f: Node => Unit): Unit = {
      f(this)
      sourceNodes.foreach(_.foreachNode(f))
    }

    def sourceNodes: List[Node]

    def close(): Unit = ()
    
  }

  sealed trait Source[O] extends Node {

    override def sourceNodes: List[Node] = Nil

    def next(ctx: Context): Future[Next[O]]

  }
  
  case class ZippedSource[L, R](source1: Source[L], source2: Source[R])
    extends Source[(L, R)] {

    private var left: List[L] = Nil
    private var right: List[R] = Nil

    override def init(): Unit ={
      left = Nil
      right = Nil
    }

    override def sourceNodes: List[Node] = source1 :: source2 :: Nil

    override def next(ctx: Context): Future[Next[(L, R)]] ={
      implicit val ec = ctx.executionContext
      
      source1.next(ctx).zip(source2.next(ctx)).map{
        case (NextInputs(t), NextInputs(s)) =>
          val lExt = left ::: t
          val rExt = right ::: s

          if(lExt.size > rExt.size){
            val (lSub, lRest) = lExt.splitAt(rExt.size)
            left = lRest
            right = Nil
            NextInputs(lSub.zip(rExt))
          }
          else if(lExt.size < rExt.size){
            val (rSub, rRest) = rExt.splitAt(lExt.size)
            left = Nil
            right = rRest
            NextInputs(lExt.zip(rSub))
          }
          else {
            left = Nil
            right = Nil
            NextInputs(lExt.zip(rExt))
          }

        case _ => Finished
      }
    }
  }
  
  sealed trait Operator[I, O] extends Source[O] with Process[I, O] {

    def source: Source[I]

    override def sourceNodes: List[Node] = source :: Nil

    override def next(ctx: Context): Future[Next[O]] = {
      ctx.processNext(source, this)
    }

  }

  sealed trait Sink[I, O] extends Node {

    def source: Source[I]

    override def sourceNodes: List[Node] = source :: Nil

    def start: O

    def sink(i: I, prev: O): O

    def post(o: O): O = o

  }
  
  case class TextSource(fileName: String) extends Source[String] {

    override def init(): Unit ={
      // TODO
      ???
    }
    
    override def next(ctx: Context): Future[Next[String]] = {
      // TODO
      ???
    }

    override def close(): Unit ={
      // TODO
      ???
    }
  }

  case class IteratorSource[I](it: Iterable[I]) extends Source[I] {

    private var iterator: Iterator[I] = _
    
    override def init(): Unit ={
      iterator = it.iterator
    }

    override def next(ctx: Context): Future[Next[I]] = {
      implicit val ec = ctx.executionContext
      
      Future{
        iterator.nextOption() match
          case Some(value) => NextInputs(value :: Nil)
          case None => Finished
      }
    }
  }
  
  case class MapOperator[I, O](input: Source[I], f: I => O) extends Operator[I, O]{

    override def source: Source[I] = input
    
    override def process(t: I): List[O] = f(t) :: Nil
    
  }
  
  case class ZipOperator[S, T](source1: Source[S], 
                               source2: Source[T]) extends Operator[(S, T), (S, T)]{
    
    override def source: Source[(S, T)] = ZippedSource(source1, source2)

    override def process(tuple: (S, T)): List[(S, T)] = tuple :: Nil
    
  }
  
  case class FlatMapOperator[I, O](input: Source[I], f: I => List[O]) extends Operator[I, O]{

    override def source: Source[I] = input

    override def process(i: I): List[O] = f(i)
  }

  case class CollectSink[I](input: Source[I]) extends Sink[I, List[I]] {

    override def source: Source[I] = input

    override def start: List[I] = Nil

    override def sink(t: I, list: List[I]): List[I] = {
      t :: list
    }

    override def post(o: List[I]): List[I] = o.reverse

  }

  case class BuilderSource[T](source: Source[T]) {
    
    def map[S](f: T => S): BuilderSource[S] = BuilderSource(MapOperator(source, f))

    def flatMap[S](f: T => List[S]): BuilderSource[S] = BuilderSource(FlatMapOperator(source, f))

    def zip[S](f: BuilderSource[S]): BuilderSource[(T, S)] ={
      BuilderSource(ZipOperator(source, f.source))
    }

    def collect(ctx: Context): Future[List[T]] ={
      val sink = CollectSink(source)
      ctx.run(sink)
    }

  }

  object BuilderSource {

    def fromFile(fileName: String): BuilderSource[String] = BuilderSource(TextSource(fileName))

    def fromCollection[T](it: Iterable[T]): BuilderSource[T] = BuilderSource(IteratorSource(it))

  }
  
  
  object SimpleContext extends Context{

    private val executorService = Executors.newFixedThreadPool(4)

    override implicit def executionContext: ExecutionContext = ExecutionContext.fromExecutor(executorService)

    override def processNext[I, O](source: Source[I],
                                   process: Process[I, O]): Future[Next[O]] ={
      source.next(this).map{
        case NextInputs(x) => NextInputs(x.flatMap(process.process))
        case Finished => Finished
      }
    }

    override def run[T](sink: Sink[?, T]): Future[T] = {

      sink.foreachNode(_.init())

      def inner(state: T): Future[T] = {
        sink.source.next(this).flatMap {
          case Finished => Future.successful(state)
          case NextInputs(list) =>
            val newState = list.foldLeft(state)((state, value) => sink.sink(value, state))
            inner(newState)
        }
      }

      val fut = inner(sink.start).map(sink.post)

      sink.foreachNode(_.close())

      fut
    }

  }


  object ActorContext extends Context{

    private lazy val actorSystem = ActorSystem()

    override def executionContext: ExecutionContext = actorSystem.dispatcher

    override def processNext[I, O](source: Source[I], process: Process[I, O]): Future[Next[O]] = {
      // TODO
      ???
    }

    override def run[T](sink: Sink[_, T]): Future[T] = {
      // TODO
      ???
    }

  }


  def main(args: Array[String]): Unit = {

    implicit val ec = SimpleContext.executionContext

    val sourceA = BuilderSource
      .fromCollection(List(1, 2, 3, 4))
      .map(_ * 2)

    val sourceB = BuilderSource
      .fromCollection(List(4, 5, 6, 7))


    val resultFut = sourceA
      .zip(sourceB)
      .map(_ + _)
      .collect(SimpleContext)

    resultFut.foreach(col => println(s"Result: $col"))
  }


}
