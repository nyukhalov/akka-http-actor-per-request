package com.github.nyukhalov.akkaactorperrequest.core

import akka.actor.{Actor, ActorRef}
import com.github.nyukhalov.akkaactorperrequest._
import com.github.nyukhalov.akkaactorperrequest.clients.OwnerClient.{GetOwnersForPets, OwnersForPets}
import com.github.nyukhalov.akkaactorperrequest.clients.PetClient.{GetPets, Pets}

class GetPetsWithOwnersActor(petClientActor: ActorRef, ownerClientActor: ActorRef) extends Actor {

  var pets = Option.empty[Seq[Pet]]
  var owners = Option.empty[Seq[Owner]]

  override def receive: Receive = {
    case GetPetsWithOwners(names) if names.size > 2 => throw PetOverflowException
    case GetPetsWithOwners(names) =>
      petClientActor ! GetPets(names)
      ownerClientActor ! GetOwnersForPets(names)
      context.become(waitingResponses)
  }

  def waitingResponses: Receive = {
    case Pets(petSeq) =>
      pets = Some(petSeq)
      replyIfReady

    case OwnersForPets(ownerSeq) =>
      owners = Some(ownerSeq)
      replyIfReady

    case f: Validation => context.parent ! f
  }

  private def replyIfReady: Unit =
    if(pets.nonEmpty && owners.nonEmpty) {
      val petSeq = pets.head
      val ownerSeq = owners.head

      val enrichedPets = (petSeq zip ownerSeq).map { case (pet, owner) => pet.withOwner(owner) }
      context.parent ! PetsWithOwners(enrichedPets)
    }
}
