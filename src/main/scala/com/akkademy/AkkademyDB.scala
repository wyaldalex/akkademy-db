package com.akkademy

import akka.actor.{Actor, ActorSystem, Props, Status}
import akka.event.Logging
import com.akkademy.messages._

import scala.collection.mutable

object Main extends App {
  val system = ActorSystem("akkademy")
  system.actorOf(Props[AkkademyDB], "akkademy-db")
}


class AkkademyDB extends Actor {

  val map = new mutable.HashMap[String,Object]()
  val log = Logging(context.system,this)

  override def receive: Receive = {
    case SetRequest(key,value) => {
      log.info(s"Received SetRequest -key: $key value $value")
      map.put(key, value)
      sender() ! Status.Success
    }
    case GetRequest(key) => {
      log.info(s"received GetRequest - Key: $key ")
      val response: Option[Object] = map.get(key)
      response match{
        case Some(x) => sender() ! x
        case None => sender() ! Status.Failure(new KeyNotFoundException(key))

      }
    }
    case o => {
      log.info(s"Received unknown message: $o")
      Status.Failure(new ClassNotFoundException())
    }

  }

}
