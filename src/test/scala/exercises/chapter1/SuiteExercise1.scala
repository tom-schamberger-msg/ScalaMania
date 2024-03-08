package exercises.chapter1

import exercises.chapter1.Exercise1.Person

import scala.util.Random

class SuiteExercise1 extends munit.FunSuite {

  val persons = (0 until 100).map(x => Person(s"Person $x", Random.nextInt(100))).toVector

  test("Distance function should handle basic input") {
    val connections = List((0,1), (1,2), (2,3), (1,3), (3,4), (0,4), (4, 5))
      .map(x => (persons(x._1), persons(x._2)))

    val dist = Exercise1.distance(persons(0), persons(5), connections)

    assertEquals(dist, Some(2))
  }

  test("Distance function should handle long connections") {
    val connections = (0 until 99).map(x => (persons(x), persons(x + 1))).toList
    val dist = Exercise1.distance(persons(0), persons(99), connections)

    assertEquals(dist, Some(100))
  }

  test("Distance function should handle unconnected ") {
    val dist = Exercise1.distance(persons(0), persons(1), (persons(0), persons(1)) :: Nil)

    assertEquals(dist, None)
  }

}
