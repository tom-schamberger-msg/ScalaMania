package exercises.chapter1

import exercises.chapter1.Exercise1.Person

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
    // TODO: Implement
    ???
  }


  object PersonIdentifier extends Identifier[Person] {

    override def identify(t: Person): ID = s"${t.name}:${t.age}"

  }

  def main(args: Array[String]): Unit = {
    // Here you can try your code
  }


}
