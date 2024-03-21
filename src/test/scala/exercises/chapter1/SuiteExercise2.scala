package exercises.chapter1

import exercises.chapter1.Exercise1.Person
import exercises.chapter1.Exercise2.PersonIdentifier

import scala.util.Random

class SuiteExercise2 extends munit.FunSuite {

  lazy val persons = (0 until 100).map(x => Person(s"Person $x", Random.nextInt(100))).toVector

  test("Distance function should handle basic input") {
    val connections = List((0,1), (1,2), (2,3), (1,3), (3,4), (0,4), (4, 5))
      .map(x => (persons(x._1), persons(x._2)))

    val dist = Exercise2.distance(persons(0), persons(5), connections, PersonIdentifier)

    assertEquals(dist, Some(2))
  }

  test("Distance function should handle long connections") {
    val connections = (0 until 99).map(x => (persons(x), persons(x + 1))).toList
    val dist = Exercise2.distance(persons(0), persons(99), connections, PersonIdentifier)

    assertEquals(dist, Some(99))
  }

  test("Distance function should handle unconnected") {
    val dist = Exercise2.distance(persons(0), persons(1), Nil, PersonIdentifier)

    assertEquals(dist, None)
  }

  test("Should handle loops") {
    val connections = List((0, 1), (1, 2), (2, 3), (3, 0))
      .map(x => (persons(x._1), persons(x._2)))
    val dist = Exercise2.distance(persons(0), persons(4), connections, PersonIdentifier)

    assertEquals(dist, None)
  }

  test("Should handle the trivial case") {
    val dist = Exercise2.distance(persons(0), persons(0), Nil, PersonIdentifier)

    assertEquals(dist, Some(0))
  }

}
