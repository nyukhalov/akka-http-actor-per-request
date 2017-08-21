package com.github.nyukhalov.akkaactorperrequest.routing

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{get, path}
import akka.http.scaladsl.server.Directives._
import com.github.nyukhalov.akkaactorperrequest.GetPetsWithOwners
import com.github.nyukhalov.akkaactorperrequest.core.GetPetsWithOwnersActor

trait PetsRoute extends BaseRoute {

  def petClientActor: ActorRef
  def ownerClientActor: ActorRef

  val petsRoute: Route =
    get {
      path("pets") {
        parameters('names) { names =>
          petsWithOwner {
            names.split(',').toList
          }
        }
      }
    }

  def petsWithOwner(owners: List[String]): Route = {
    handleRequest(Props(classOf[GetPetsWithOwnersActor], petClientActor, ownerClientActor), GetPetsWithOwners(owners))
  }
}
