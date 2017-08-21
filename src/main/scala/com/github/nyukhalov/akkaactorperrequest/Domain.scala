package com.github.nyukhalov.akkaactorperrequest

final case class Pet(name: String) {
  def withOwner(owner: Owner) = EnrichedPet(name, owner)
}
final case class Owner(name: String)
final case class EnrichedPet(name: String, owner: Owner)

trait RestMessage

case class GetPetsWithOwners(petNames: List[String]) extends RestMessage
case class PetsWithOwners(pets: Seq[EnrichedPet]) extends RestMessage

object SuccessfulOperation
final case class Validation(msg: String)
final case class Error(msg: String)
final case class NotExist(msg: String)

case object PetOverflowException extends Exception("PetOverflowException: OMG. Pets. Everywhere.")
