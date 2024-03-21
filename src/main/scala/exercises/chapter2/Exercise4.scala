package exercises.chapter2

import exercises.chapter2.Exercise3.ListOps

object Exercise4 {


  object LazyList {

    def apply[T](): LazyList[T] = {
      // TODO: Implement
      ???
    }

  }

  sealed trait LazyList[T] extends ListOps[T, LazyList]


  extension [T](t: T) {

    /**
     * This is a helper function to construct lazy lists.
     * @param tail Tail of the constructed lazy list
     * @return A lazy list with head t and tail ll.
     */
    def #::(tail: => LazyList[T]): LazyList[T] ={
      // TODO: Implement
      ???
    }

  }


  /**
   * A lazy list containing all integer numbers
   * i.e. 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: ...
   */
  lazy val integers: LazyList[Int] = {
    // TODO: Implement
    ???
  }
  
  /**
   * A lazy list containing all odd numbers
   * i.e. 1 :: 3 :: 5 :: 7 :: 9 :: 11 :: ...
   */
  lazy val oddNumbers: LazyList[Int] = {
    // TODO: Implement
    ???
  }

  /**
   * A lazy list containing all fibonacci numbers
   * i.e. 1 :: 1 :: 2 :: 3 :: 5 :: 8 :: ...
   */
  lazy val fibonacci: LazyList[Int] = {
    // TODO: Implement
    ???
  }


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
