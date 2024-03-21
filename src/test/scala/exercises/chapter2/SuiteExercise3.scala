package exercises.chapter2

import exercises.chapter1.Exercise1.Person
import exercises.chapter2.Exercise3.{CList, sumAll}

import scala.util.Random

class SuiteExercise3 extends munit.FunSuite {

  val list = (0 until 10).foldRight(CList[Int]())(_ :: _)

  test("CList head and tail methods should work") {
    val l = 1 :: 3 :: 7 :: CList()

    assertEquals(l.head, Some(1))
    assertEquals(l.tail.head, Some(3))
    assertEquals(l.tail.tail.head, Some(7))
    assertEquals(l.tail.tail.tail.head, None)
  }

  test("CList map and fold methods should work") {
    val result = list.map(_ + 2).foldLeft(4)(_ + _)

    assertEquals(result, 69)
  }

  test("CList take and filter methods should work") {
    assertEquals(list.take(0), Nil)
    assertEquals(list.filter(x => x % 2 == 0).take(5), 0 :: 2 :: 4 :: 6 :: 8 :: Nil)
  }

  test("CList filter methods should work") {
    assertEquals(list.take(0), Nil)
    assertEquals(list.take(5), 0 :: 1 :: 2 :: 3 :: 4 :: Nil)
  }

  test("CList isEmpty method should work") {
    assertEquals(list.isEmpty, false)
    assertEquals(CList().isEmpty, true)
    assertEquals((1 :: CList()).tail.isEmpty, true)
  }

  test("CList flatMap and fold methods should work") {
    val result = list.flatMap(x =>
      if(x % 2 == 1) List(x, x + 1)
      else if (x % 4 == 0) Nil
      else List(x, x + 1, x - 1)
    ).foldLeft(0)(_ + _)

    assertEquals(result, 79)
  }

  test("sumAll method should work") {
    assertEquals(sumAll(CList()), 0)
    assertEquals(sumAll(list), 45)
  }

}
