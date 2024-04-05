package exercises.chapter3

// Please note: We need some imports for this exercise
// We use an actor library called Akka (run SBT to load it)
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.concurrent.duration.*

object Exercise6 {

  sealed trait MyMessage
  case class Greeting(from: String) extends MyMessage
  case class Task(list: List[String]) extends MyMessage
  case object Goodbye extends MyMessage


  class MyActor extends Actor with ActorLogging {

    val slave = context.actorOf(Props[SlaveActor](), name = "slave")
    
    // We need a timeout for the "ask" (?) pattern
    implicit val timeout: Timeout = 1.seconds
    // We need a execution context for operations on futures such as "foreach"
    implicit val ec: ExecutionContextExecutor = context.system.dispatcher
    
    
    def receive = {
      case Greeting(greeter) => log.info(s"I was greeted by $greeter.")
      case Task(list) =>
        // Ask pattern
        val _sender = sender()
        (slave ? Task(list))
          .foreach(result => _sender ! result)
        
      case Goodbye => log.info("Someone said goodbye to me.")
      case other => log.info(s"I do not understand: $other")
    }

  }

  class SlaveActor extends Actor with ActorLogging {

    def receive = {
      case Task(list) => 
        val result = list.mkString(",")
        sender() ! result
      case other => log.info(s"I am a slave and cannot do this: $other")
    }

  }

  class PingPongActor extends Actor with ActorLogging {

    var count = 0

    def receive = {
      case "Die!" => self ! PoisonPill
      case x =>
        count += 1
        sender() ! s"Pong: $x (message # $count)"
    }

  }


  type ID = Int
  case class User(name: String, address: String)

  sealed trait UserAction
  case class AddUser(user: User) extends UserAction
  case class RemoveUser(id: ID) extends UserAction
  case class ListUsers() extends UserAction

  class UserStore extends Actor with ActorLogging {

    var nextId = 0
    var map = Map[ID, User]()

    def receive = {
      case action: UserAction =>
        action match {
          case AddUser(user) =>
            map += nextId -> user
            sender() ! nextId
            nextId += 1

          case RemoveUser(id) =>
            map -= id
            log.info(s"Removed id $id: $map")

          case ListUsers() =>
            sender() ! map.toList.map(_._2)
        }
    }

  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("mySystem")

    implicit val timeout: Timeout = 1.seconds
    implicit val ec: ExecutionContext = system.dispatcher

    val userStoreActor: ActorRef = system.actorOf(Props[UserStore](), "userstore")

    (userStoreActor ? AddUser(User("Tom", "München")))
      .foreach(x => println(s"User id of Tom: $x"))

    (userStoreActor ? AddUser(User("Jonas", "München")))
      .foreach(x => println(s"User id of Jonas: $x"))

    (userStoreActor ? AddUser(User("Steffen", "München")))
      .map{
        case id: Int =>
          userStoreActor ! RemoveUser(id)
          id
        case x =>
          println(s"Error: Got something else")
      }
      .foreach(x => println(s"User id of Steffen: $x"))


    Thread.sleep(3.seconds.toMillis)

    (userStoreActor ? ListUsers())
      .foreach{
        case list: List[User] =>
          list.foreach(x => println(s"User $x"))
        case x =>
          println(s"Error: Got something else")
      }

    println("Test")
  }


}
