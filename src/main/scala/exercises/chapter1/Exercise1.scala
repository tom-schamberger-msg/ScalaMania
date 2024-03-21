package exercises.chapter1

import scala.annotation.tailrec

object Exercise1 {
  
  case class Person(name: String, age: Int)


  /**
   * This function calculates the distance between two persons 
   * regarding the knowing relation.
   * 
   * @param person1 One person
   * @param person2 The other person
   * @param knowing A relation between persons
   * @return The minimum distance between the person or None if there is no connection.
   *         E.g. if a person A knows person B and person B knows person C,
   *         the distance between A and C is 2 and between A and B is 1.
   */
  def distance(person1: Person, 
               person2: Person,
               knowing: List[(Person, Person)]): Option[Int] = {
    val map = knowing
      .flatMap(t => (t._1, t._2) :: (t._2, t._1) :: Nil)
      .groupBy(_._1)
      .map(t => t._1 -> t._2.map(_._2).toSet)

    @tailrec
    def inner(current: Set[Person], visited: Set[Person], count: Int): Option[Int] = {
      val next = current.flatMap(x => map.getOrElse(x, Nil)) -- visited

      if next.contains(person2) then Some(count)
      else if next.isEmpty then None
      else inner(next, visited ++ next, count + 1)
    }

    if person1 == person2 then Some(0)
    else inner(current = Set(person1), visited = Set(person1), count = 1)
  }
  
  @main
  def run(): Unit = {
    // Here you can try your code
  }

}
