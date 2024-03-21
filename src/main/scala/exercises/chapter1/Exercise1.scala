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
    distanceSolution1(person1, person2, knowing)
  }

  /**
   * Solution 1
   * Author: Tom Schamberger
   */
  def distanceSolution1(person1: Person,
                        person2: Person,
                        knowing: List[(Person, Person)]): Option[Int] = {
    val map: Map[Person, Set[Person]] = knowing
      .flatMap(t => (t._1, t._2) :: (t._2, t._1) :: Nil)
      .groupBy(_._1)
      .map(t => t._1 -> t._2.map(_._2).toSet)

    @tailrec
    def inner(current: Set[Person], visited: Set[Person], count: Int): Option[Int] = {
      val next = current.flatMap(x => map.getOrElse(x, Nil)) -- visited

      if (next.contains(person2)) Some(count)
      else if (next.isEmpty) None
      else inner(next, visited ++ next, count + 1)
    }

    if (person1 == person2) Some(0)
    else inner(current = Set(person1), visited = Set(person1), count = 1)
  }

  /**
   * Solution 2
   * Author: Jonas Lossin
   */
  def distanceSolution2(person1: Person,
                        person2: Person,
                        knowing: List[(Person, Person)]): Option[Int] = {
    // condition to stop recursion
    if (person1 == person2) return Some(0)
    if (knowing.isEmpty) return None

    // retrieve all the people that person1 knows
    val (connectionsPerson1, rest) = knowing.partition((p1, p2) => p1 == person1 || p2 == person1)
    val friendsPerson1 = connectionsPerson1.map((p1, p2) => if (p1 == person1) p2 else p1)

    // compute distance with each friend person1's friends and person2 and return min + 1
    friendsPerson1
      .map(friend => distance(friend, person2, rest))
      .minByOption {
        case Some(value) => value
        case None => Int.MaxValue
      }
      .flatten // Alternative to .getOrElse(None)
      .map(_ + 1)
  }

  def main(args: Array[String]): Unit = {
    // Here you can try your code
  }

}
