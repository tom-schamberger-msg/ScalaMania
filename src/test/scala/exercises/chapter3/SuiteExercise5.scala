package exercises.chapter3

import exercises.chapter3.Exercise5.*

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.*

class SuiteExercise5 extends munit.FunSuite {

  test("Async sum should work") {
    val sumFut = sumAsync((1 to 10).toList)
    val result = Await.result(sumFut, 1.second)

    assertEquals(result, 55)
  }

  test("Async combine should work") {

    val list = (1 to 4).toList

    val fut = combineAsync(list.map(x => Future(someWork(x))))
    val result = Await.result(fut, 2.second)

    assertEquals(result, list)
  }

  test("Parallelize should work") {

    val list = (1 to 4).toList

    val fut = parallelize(list.map(x => () => someWork(x)))
    val result = Await.result(fut, 2.second)

    assertEquals(result, list)
  }

}
