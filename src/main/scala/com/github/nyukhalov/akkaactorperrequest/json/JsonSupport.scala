package com.github.nyukhalov.akkaactorperrequest.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.nyukhalov.akkaactorperrequest.{EnrichedPet, Error, NotExist, Owner, Validation}
import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val ownerFormat = jsonFormat1(Owner)
  implicit val petsFormat = jsonFormat2(EnrichedPet)

  implicit val errorFormat = jsonFormat1(Error)
  implicit val validationFormat = jsonFormat1(Validation)
  implicit val notExistFormat = jsonFormat1(NotExist)

//  implicit object UserWithIdFormat extends RootJsonFormat[UserWithId] {
//    override def read(json: JsValue): UserWithId = {
//      throw new RuntimeException("Not implemented")
//    }
//    override def write(obj: UserWithId): JsValue = userFormat.write(obj.user)
//  }
}
