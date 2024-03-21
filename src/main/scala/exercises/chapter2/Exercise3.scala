package exercises.chapter2

import scala.annotation.tailrec

object Exercise3 {

  trait ListOps[T, L[_]] {

    /**
     * First element in the list.
     */
    def head: Option[T]

    /**
     * Elements in the list after the first element.
     */
    def tail: L[T]

    /**
     * Add element e to front
     *
     * @return A list with e as head and this list as tail
     */
    def push(e: T): L[T]

    /**
     * Classical map function
     */
    def map[S](f: T => S): L[S]

    /**
     * Classical flatMap function
     */
    def flatMap[S](f: T => Iterable[S]): L[S]

    /**
     * Classical filter function
     */
    def filter(f: T => Boolean): L[T]


    /**
     * Takes the first count elements from the list
     * and creates a "standard" list
     */
    def take(count: Int): List[T]

    /**
     * Create a list of tuples from this list and the other list
     * i.e. (1 :: 2 :: 3 :: LNil()) zip (5 :: 6 :: 7 :: LNil()) -> ((1,5) :: (2,6) :: (3,7) :: LNil())
     */
    def zip[S](other: L[S]): L[(T, S)]

    /**
     * Check if empty
     */
    def isEmpty: Boolean

    def nonEmpty: Boolean = !isEmpty

  }

  sealed trait LinkedList[T] extends ListOps[T, LinkedList] {

    /**
     * Classical fold function
     */
    def foldLeft[S](init: S)(f: (S, T) => S): S

    def foreach(f: T => Unit): Unit = {
      head match {
        case Some(value) =>
          f(value)
          tail.foreach(f)
        case None => ()
      }
    }

    def ::(e: T) = push(e)

  }

  object LinkedList {

    def apply[T](): LinkedList[T] = {
      // TODO: Implement
      ???
    }

  }
  
  /**
   * Sum all elements in the list
   * i.e. sumAll(1 :: 2 :: 3 :: CList()) -> 6
   */
  def sumAll(list: LinkedList[Int]): Int = {
    // TODO: Implement
    ???
  }

  def main(args: Array[String]): Unit = {
    // Here you can try your code

    // As a result of this exercise, we should have a collection we could use as this:
    val mylist = 1 :: 2 :: 3 :: 4 :: LinkedList()

    mylist.foreach{e => println(e)}

    // Output:
    // 1
    // 2
    // 3
    // 4
    
    val sum = sumAll(mylist)
    println(sum)

    // Output:
    // 10
  }



}
