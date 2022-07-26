package com.akkademy

/*
* Delete the original quick test example.
* It makes all tests fail
* Projected generated with:
* https://developer.lightbend.com/start/?group=akka&project=akka-quickstart-scala
* */

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import com.akkademy.messages._
import org.scalatest.{BeforeAndAfterEach, FunSpecLike, Matchers}

class AkkademyDbSpec extends FunSpecLike
with Matchers
with BeforeAndAfterEach{
  implicit val system = ActorSystem()
  describe("akkademydb") {
    describe("given SetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef(new AkkademyDB)
        actorRef ! SetRequest("somekey","somevalue")
        val akkademyDB = actorRef.underlyingActor
        akkademyDB.map.get("somekey") should equal(Some("somevalue"))
      }
    }
  }



}
