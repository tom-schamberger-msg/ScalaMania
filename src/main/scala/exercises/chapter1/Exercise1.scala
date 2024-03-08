package exercises.chapter1

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
    // TODO: Implement
    ???
  }
  
  @main
  def run(): Unit = {
    // Here you can try your code
  }

}
