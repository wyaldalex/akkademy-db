package com.akkademy

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.akkademy.test.GeneralActorTesting.ScalaPongActor
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class ScalaAskExamplesTest extends FunSpecLike with Matchers {

  val system = ActorSystem("system")
  implicit val timeout = Timeout(5 seconds)
  //val pongActor = system.actorOf(Props(classOf[ScalaPongActor]))
  val pongActor = system.actorOf(ScalaPongActor props "Pong", "scalaPongActor")


  describe("Pong actor") {
    it("should respond with Pong") {
      val future = pongActor ? "Ping"
      val result = Await.result(future.mapTo[String], 1 second)
      assert(result == "Pong")
    }
    it("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1 second)
      }
    }
    import scala.concurrent.ExecutionContext.Implicits.global
    it("should print to console"){
      (pongActor ? "Ping").onSuccess({
        case x : String => println("replied with: " + x)
      })
      println("Who gets run first?")
    }
    it("should do more async testing") {
      def askPong(message: String): Future[String] = (pongActor ? message).mapTo[String]

      //loops forever?
      val f: Future[String] = askPong("Ping").flatMap(x => askPong("unknown").recoverWith({
        case t: Exception => askPong("unknown")
      }) )

    }
  }





}
