package com.github.nyukhalov.akkaactorperrequest

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.github.nyukhalov.akkaactorperrequest.clients.{OwnerClient, PetClient}
import com.github.nyukhalov.akkaactorperrequest.routing.PetsRoute

import scala.concurrent.ExecutionContext

class WebServer(serverPort: Int)
               (implicit actorSystem: ActorSystem, mat: Materializer, ec: ExecutionContext)
  extends PetsRoute with AppLogger {

  override def actorSys: ActorSystem = implicitly

  override val petClientActor: ActorRef = actorSys.actorOf(Props[PetClient], "pet-client")

  override val ownerClientActor: ActorRef = actorSys.actorOf(Props[OwnerClient], "owner-client")

  val route: Route = petsRoute

  def start(): Unit = {
    Http().bind("0.0.0.0", serverPort).runForeach(_.handleWith(Route.handlerFlow(route)))
    logger.info(s"Server started on port $serverPort")
  }
}
