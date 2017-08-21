package com.github.nyukhalov.akkaactorperrequest

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.Await

object Boot extends App {

  implicit val system = ActorSystem("actor-per-request")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  new WebServer(38080).start()

  sys.addShutdownHook(() => {
    val future = system.terminate()
    Await.result(future, 120.seconds)
  })
}
