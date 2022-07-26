package com.akkademy.test

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Status}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object GeneralActorTesting extends App {

  object ScalaPongActor {
    def props(response: String) : Props = {
      Props(classOf[ScalaPongActor], response)
    }
  }
  class ScalaPongActor(response: String) extends  Actor with ActorLogging {
    override def receive: Receive = {
      case "Ping" => {
        log.info(s"I am ${self.path}")
        log.info(s"Received message will reply with $response")
        sender() ! response
      }
      case _ => {
        log.info("Unknown value sent triggering error")
        sender() ! Status.Failure(new Exception("unknown type received"))
      }

    }
  }

  val actorSystem = ActorSystem("system")
  //val scalaPongActor = actorSystem.actorOf(Props[ScalaPongActor], "scalaPongActor")
  val scalaPongActor = actorSystem.actorOf(ScalaPongActor props "PongFOO", "scalaPongActor")
  val pongActorSelected = actorSystem.actorSelection("/user/scalaPongActor")

  scalaPongActor ! "Ping"
  pongActorSelected ! "Ping"

  //Random Future testing
  //https://docs.scala-lang.org/overviews/scala-book/futures.html
  def getUsernameFromDatabaseAsync(userId: Int): Future[String] = Future {
    println("Starting Long computation")
    Thread.sleep(5000)
    "Unicron90121"
  }

  val userName = getUsernameFromDatabaseAsync(12121)
  userName.onComplete{
    case Success(x) => {
      println(s"Value from Future $x")

    }
    case Failure(e) => {
      e.printStackTrace()
    }
  }
  println("Some other computation")

  /*
  val result: Future[String] = for {
    aapl <- userName
  } yield (aapl) */







}
