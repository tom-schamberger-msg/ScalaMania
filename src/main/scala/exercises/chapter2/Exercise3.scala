package exercises.chapter2

import scala.annotation.tailrec

object Exercise3 {

  sealed trait CList[T] {

    /**
     * First element in the list.
     */
    def head: Option[T]

    /**
     * Elements in the list after the first element.
     */
    def tail: CList[T]

    /**
     * Add element e to front
     *
     * @return A list with e as head and this list as tail
     */
    def push(e: T): CList[T]

    /**
     * Classical map function
     */
    def map[S](f: T => S): CList[S]

    /**
     * Classical flatMap function
     */
    def flatMap[S](f: T => Iterable[S]): CList[S]

    /**
     * Classical fold function
     */
    def foldLeft[S](init: S)(f: (S, T) => S): S

    /**
     * Classical filter function
     */
    def filter(f: T => Boolean): CList[T]


    /**
     * Takes the first count elements from the list
     * and creates a "standard" list
     */
    def take(count: Int): List[T]

    /**
     * Create a list of tuples from this list and the other list
     * i.e. (1 :: 2 :: 3 :: CList()) zip (5 :: 6 :: 7 :: CList()) -> ((1,5) :: (2,6) :: (3,7) :: CList())
     */
    def zip[S](other: CList[S]): CList[(T, S)]

    /**
     * Check if empty
     */
    def isEmpty: Boolean

    def nonEmpty: Boolean = !isEmpty

    def ::(e: T) = push(e)

    def foreach(f: T => Unit): Unit = {
      head match {
        case Some(value) =>
          f(value)
          tail.foreach(f)
        case None => ()
      }
    }

  }

  object CList {

    def apply[T](): CList[T] ={
      // TODO: Implement
      ???
    }

  }
  
  /**
   * Sum all elements in the list
   * i.e. sumAll(1 :: 2 :: 3 :: CList()) -> 6
   */
  def sumAll(list: CList[Int]): Int = {
    // TODO: Implement
    ???
  }

  def main(args: Array[String]): Unit = {
    // Here you can try your code

    // As a result of this exercise, we should have a collection we could use as this:
    val mylist = 1 :: 2 :: 3 :: 4 :: CList()

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
