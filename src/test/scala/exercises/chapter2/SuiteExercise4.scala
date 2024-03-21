package exercises.chapter2

import exercises.chapter2.Exercise4.{fibonacci, integers, oddNumbers}

class SuiteExercise4 extends munit.FunSuite {

  test("LazyList integers should yield correct numbers") {
    val list = integers.take(20)
    val should = (1 to 20).toList

    assertEquals(list, should)
  }

  test("LazyList integers should yield correct numbers") {
    val list = oddNumbers.take(20)
    val should = (1 to 40).filter(_ % 2 == 1).toList

    assertEquals(list, should)
  }

  test("LazyList integers should yield correct numbers") {
    val list = fibonacci.take(11)
    val should = 1 :: 1 :: 2 :: 3 :: 5 :: 8 :: 13 :: 21 :: 34 :: 55 :: 89 :: Nil

    assertEquals(list, should)
  }

}
