package exercises.chapter2

import exercises.chapter2.Exercise3.ListOps

object Exercise4 {


  object LazyList {

    def apply[T](): LazyList[T] = LazyNil()

  }

  sealed trait LazyList[T] extends ListOps[T, LazyList]

  case class LazyItem[T](t: T, next: () => LazyList[T]) extends LazyList[T] {

    lazy val tail = next()

    override def head: Option[T] = Some(t)

    override def push(e: T): LazyList[T] = LazyItem(e, () => this)

    override def map[S](f: T => S): LazyList[S] = {
      LazyItem(f(t), () => tail.map(f))
    }

    override def flatMap[S](f: T => Iterable[S]): LazyList[S] = {

      def inner(list: List[S]): LazyList[S] = {
        list match {
          case head :: rest => LazyItem(head, () => inner(rest))
          case Nil => tail.flatMap(f)
        }
      }

      val list = f(t).toList
      inner(list)
    }

    override def filter(f: T => Boolean): LazyList[T] = {
      if(f(t)) LazyItem(t, () => tail.filter(f))
      else tail.filter(f)
    }

    override def take(count: Int): List[T] = {
      if(count > 0) t :: tail.take(count - 1)
      else Nil
    }

    override def zip[S](other: LazyList[S]): LazyList[(T, S)] = {
      other match {
        case item @ LazyItem(t2, _) => LazyItem((t, t2), () => tail.zip(item.tail))
        case LazyNil() => LazyNil()
      }
    }

    override def isEmpty: Boolean = false

  }

  case class LazyNil[T]() extends LazyList[T] {

    override def head: Option[T] = None

    override def tail: LazyList[T] = this

    override def push(e: T): LazyList[T] = LazyItem(e, () => this)

    override def map[S](f: T => S): LazyList[S] = LazyNil()

    override def flatMap[S](f: T => Iterable[S]): LazyList[S] = LazyNil()

    override def filter(f: T => Boolean): LazyList[T] = LazyNil()

    override def take(count: Int): List[T] = Nil

    override def zip[S](other: LazyList[S]): LazyList[(T, S)] = LazyNil()

    override def isEmpty: Boolean = true
  }


  extension [T](t: T) {

    /**
     * This is a helper function to construct lazy lists.
     * @param tail Tail of the constructed lazy list
     * @return A lazy list with head t and tail ll.
     */
    def #::(tail: => LazyList[T]): LazyList[T] = LazyItem(t, () => tail)

  }

  /**
   * A lazy list containing all integer numbers
   * i.e. 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: ...
   */
  lazy val integers: LazyList[Int] = {

    def intGen(i: Int): LazyList[Int] = i #:: intGen(i + 1)

    intGen(1)
  }
  
  /**
   * A lazy list containing all odd numbers
   * i.e. 1 :: 3 :: 5 :: 7 :: 9 :: 11 :: ...
   */
  lazy val oddNumbers: LazyList[Int] = {

    integers.filter(_ % 2 == 1)

  }

  /**
   * A lazy list containing all fibonacci numbers
   * i.e. 1 :: 1 :: 2 :: 3 :: 5 :: 8 :: ...
   */
  lazy val fibonacci: LazyList[Int] = 1 #:: 1 #:: fibonacci.zip(fibonacci.tail).map(_ + _)


  def main(args: Array[String]): Unit = {
    // Here you can try your code

    // As a result of this exercise, we should have a collection we could use as this:
    val mylist = integers.take(4)

    mylist.foreach { e => println(e) }

    // Output:
    // 1
    // 2
    // 3
    // 4
    
    val lazylist = 7 #:: 8 #:: integers

    lazylist.take(4).foreach{ e => println(e) }

    // Output:
    // 7
    // 8
    // 1
    // 2
    
  }
}
