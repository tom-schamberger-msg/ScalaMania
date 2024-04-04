package exercises.chapter3

// Please note: We need some imports for this exercise
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.*

object Exercise6 {

  sealed trait MyMessage
  case class Greeting(from: String) extends MyMessage
  case class Task(list: List[String], replyTo: ActorRef) extends MyMessage
  case object Goodbye extends MyMessage


  class MyActor extends Actor with ActorLogging {

    val slave = context.actorOf(Props[SlaveActor](), name = "slave")
    
    // We need a timeout for the "ask" (?) pattern
    implicit val timeout: Timeout = 1.seconds
    // We need a execution context for operations on futures such as "foreach"
    implicit val ec: ExecutionContext = context.system.dispatcher
    
    
    def receive = {
      case Greeting(greeter) => log.info(s"I was greeted by $greeter.")
      case Task(list, replyTo) =>
        // Ask pattern
        (slave ? Task(list, self))
          .foreach(result => replyTo ! result)
        
      case Goodbye => log.info("Someone said goodbye to me.")
      case other => log.info(s"I do not understand: $other")
    }

  }

  class SlaveActor extends Actor with ActorLogging {

    def receive = {
      case Task(list, replyTo) => 
        val result = list.mkString(",")
        replyTo ! result
      case other => log.info(s"I am a slave and cannot do this: $other")
    }

  }

  class PingPongActor extends Actor with ActorLogging {

    def receive = {
      case "Die!" => self ! PoisonPill
      case x => sender() ! s"Pong: $x"
    }

  }

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("mySystem")
    val myActor = system.actorOf(Props[MyActor](), "myactor")

    myActor ! Greeting("Hey")
    // I was greeted by Hey.

  }


}
