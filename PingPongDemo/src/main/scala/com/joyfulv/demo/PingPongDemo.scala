package com.joyfulv.demo

import akka.actor.{Props, ActorSystem, Actor}
import scala.concurrent.{Await, future, blocking}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class Ping extends Actor {

  def receive: Receive = {
    case MSG_PING(count) =>
      println("ping - "+count)
      val waiting = future { blocking(Thread.sleep(1000L)) }
      Await.result(waiting, 2 second)
      sender ! MSG_PONG(count + 1)
    case _ => //Do nothing...
  }

}

class Pong extends Actor {

  def receive: Receive = {
    case MSG_PONG(count) =>
      println("pong - "+count)
      val waiting = future { blocking(Thread.sleep(1000L)) }
      Await.result(waiting, 2 second)
      sender ! MSG_PING(count + 1)
    case _ => //Do nothing...
  }

}

object PingPongDemo extends App {
  println("Game started!")

  val system = ActorSystem("PingPong")
  val pingActor = system.actorOf(Props[Ping], name = "pingActor")
  val pongActor = system.actorOf(Props[Pong], name = "pongActor")

  pingActor.tell(MSG_PING(1), pongActor)
}

sealed trait MESSAGE

case class MSG_PING(count:Int) extends MESSAGE
case class MSG_PONG(count:Int) extends MESSAGE
