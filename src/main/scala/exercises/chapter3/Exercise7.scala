package exercises.chapter3

// Please note: We need some imports for this exercise
// We use an actor library called Akka (run SBT to load it)
// Instead of exercise 6, we use typed actors here

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.LoggerOps
import akka.actor.typed.*

object Exercise7 {

  final case class Greet(whom: String, replyTo: ActorRef[Greeted])
  final case class Greeted(whom: String, from: ActorRef[Greet])
  final case class SayHello(name: String)

  object Greeter {

    def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
      context.log.info("Hello {}!", message.whom)
      message.replyTo ! Greeted(message.whom, context.self)
      Behaviors.same
    }

  }

  object Bot {

    def apply(max: Int = 10) = bot(0, max)

    private def bot(greetingCounter: Int, max: Int): Behavior[Greeted] =
      Behaviors.receive { (context, message) =>
        val n = greetingCounter + 1

        context.log.info2("Greeting {} for {}", n, message.whom)

        if (n == max) {
          Behaviors.stopped
        } else {
          message.from ! Greet(message.whom, context.self)
          bot(n, max)
        }
      }

  }

  object GreeterApp {

    def apply(): Behavior[SayHello] =
      Behaviors.setup { context =>
        val greeter = context.spawn(Greeter(), "greeter")

        Behaviors.receiveMessage { message =>
          val replyTo = context.spawn(Bot(max = 3), message.name)
          greeter ! Greet(message.name, replyTo)
          Behaviors.same
        }
      }

  }

  def main(args: Array[String]): Unit = {

    val system: ActorSystem[SayHello] = ActorSystem(GreeterApp(), "hello")

    system ! SayHello("World")
    system ! SayHello("Akka")

  }
  
}
