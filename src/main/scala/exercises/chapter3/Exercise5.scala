package exercises.chapter3

// Please note: We need some imports for this exercise
import java.util.concurrent.Executors
import scala.concurrent.*
import scala.concurrent.duration.*

object Exercise5 {

  /**
   * This function represents some time-consuming work such as reading a file
   * or doing a time-consuming computation
   */
  def someWork[R](result: R) = {
    // DO NOT CHANGE!
    val waitDuration = 1.second
    Thread.sleep(waitDuration.toMillis)
    result
  }

  // Let us define a thread pool
  private val executorService = Executors.newFixedThreadPool(4)
  // We can use this thread pool implicitly in all futures of this context
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(executorService)


  /**
   * Sum values in list asynchronously.
   * @param list List of values
   * @return Future of sum of values
   */
  def sumAsync(list: List[Int]): Future[Int] ={
    // TODO: Implement
    ???
  }

  /**
   * Combine results of a sum of futures and create a future with a list containing all results
   *
   * @param list List of futures
   * @tparam T Result type
   * @return Future with list of results
   */
  def combineAsync[T](list: List[Future[T]]): Future[List[T]] = {
    // TODO: Implement
    ???
  }

  /**
   * Runs function in list in parallel.
   * 
   * @param list List of functions
   * @tparam T Result type
   * @return Future with list of results
   */
  def parallelize[T](list: List[() => T]): Future[List[T]] ={
    // TODO: Implement
    ???
  }


  def main(args: Array[String]): Unit = {

    val list = 1 :: 2 :: 3 :: 4 :: Nil

    val fut: Future[Int] = Future(list)
      .map(_.sum)
      .map(_ + 1)

    val i: Int = Await.result(fut, 10.seconds)

    println(i)


    val sumFuture = sumAsync(1 :: 2 :: 3 :: 4 :: Nil)
    
    // Get result with timeout of 10 seconds
    val timeoutDuration: Duration = 10.seconds
    val sum = Await.result(sumFuture, timeoutDuration)
    
    println(sum)
    // 10
  }

}
