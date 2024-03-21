package exercises.chapter1

import exercises.chapter1.Exercise1.Person

import scala.annotation.tailrec

object Exercise2 {


  type ID = String

  trait Identifier[T] {

    def identify(t: T): ID

  }

  /**
   * This function calculates the distance between two persons
   * regarding the knowing relation.
   *
   * @param obj1 One object
   * @param obj2 The other object
   * @param relation  A relation between objects
   * @tparam T Identifiable object
   * @return The minimum distance between the objects or None if there is no connection.
   *         E.g. if a object A knows object B and object B knows object C,
   *         the distance between A and C is 2 and between A and B is 1.
   */
  def distance[T](obj1: T,
                  obj2: T,
                  relation: List[(T, T)],
                  identifier: Identifier[T]): Option[Int] = {
    distanceSolution1(obj1, obj2, relation, identifier)
  }

  /**
   * Solution 1
   * Author: Tom Schamberger
   */
  def distanceSolution1[T](obj1: T,
                           obj2: T,
                           relation: List[(T, T)],
                           identifier: Identifier[T]): Option[Int] = {
    val objId1 = identifier.identify(obj1)
    val objId2 = identifier.identify(obj2)

    val map: Map[ID, Set[ID]] = relation
      .map(t => (identifier.identify(t._1), identifier.identify(t._2)))
      .flatMap(t => (t._1, t._2) :: (t._2, t._1) :: Nil)
      .groupBy(_._1)
      .map(t => t._1 -> t._2.map(_._2).toSet)

    @tailrec
    def inner(current: Set[ID], visited: Set[ID], count: Int): Option[Int] = {
      val next = current.flatMap(x => map.getOrElse(x, Nil)) -- visited

      if (next.contains(objId2)) Some(count)
      else if (next.isEmpty) None
      else inner(next, visited ++ next, count + 1)
    }

    if (objId1 == objId2) Some(0)
    else inner(current = Set(objId1), visited = Set(objId1), count = 1)
  }

  /**
   * Solution 2
   * Author: Jonas Lossin
   */
  def distanceSolution2[T](obj1: T,
                           obj2: T,
                           relation: List[(T, T)],
                           identifier: Identifier[T]): Option[Int] = {

    def inner(objId1: ID,
              objId2: ID,
              relation: List[(ID, ID)]): Option[Int] ={
      // condition to stop recursion
      if (objId1 == objId2) return Some(0)

      // retrieve all the people that person1 knows
      val (connectionsPerson1, rest) = relation.partition((p1, p2) => p1 == objId1 || p2 == objId1)
      val friendsObj1 = connectionsPerson1.map((p1, p2) => if (p1 == objId1) p2 else p1)

      // compute distance with each friend person1's friends and person2 and return min + 1
      friendsObj1
        .map(obj => inner(obj, objId2, rest))
        .minByOption {
          case Some(value) => value
          case None => Int.MaxValue
        }
        .flatten // Alternative to .getOrElse(None)
        .map(_ + 1)
    }

    val objId1 = identifier.identify(obj1)
    val objId2 = identifier.identify(obj2)
    val relationAsIds = relation.map(t => (identifier.identify(t._1), identifier.identify(t._2)))

    inner(objId1, objId2, relationAsIds)

  }


  object PersonIdentifier extends Identifier[Person] {

    override def identify(t: Person): ID = s"${t.name}:${t.age}"

  }

  def main(args: Array[String]): Unit = {
    // Here you can try your code
  }


}
