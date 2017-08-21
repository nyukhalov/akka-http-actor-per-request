package com.github.nyukhalov.akkaactorperrequest.routing

import java.util.UUID

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, ReceiveTimeout, SupervisorStrategy}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import com.github.nyukhalov.akkaactorperrequest.json.JsonSupport
import com.github.nyukhalov.akkaactorperrequest.routing.PerRequestActor.WithProps
import com.github.nyukhalov.akkaactorperrequest.{Error, NotExist, PetsWithOwners, RestMessage, SuccessfulOperation, Validation}
import spray.json._

import scala.concurrent.Promise
import scala.concurrent.duration._

trait PerRequestActor extends Actor with JsonSupport {
  import PerRequestActor._
  import context._

  def r: RequestContext
  def target: ActorRef
  def message: RestMessage
  def p: Promise[RouteResult]

  setReceiveTimeout(2.seconds)
  target ! message

  override def receive: Receive = {
    case res: PetsWithOwners => complete(OK, res.pets)
    case SuccessfulOperation => complete(OK, EmptyJson)
    case ne: NotExist => complete(NotFound, ne)
    case v: Validation => complete(BadRequest, v)
    case e: Error => complete(InternalServerError, e)
    case ReceiveTimeout => complete(GatewayTimeout, RequestTimeoutResponse)
  }

  def complete(m: => ToResponseMarshallable): Unit = {
    val f = r.complete(m)
    f.onComplete(p.complete(_))
    stop(self)
  }

  override val supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy() {
      case e =>
        complete(InternalServerError, Error(e.getMessage))
        Stop
    }
}

object PerRequestActor {
  val EmptyJson = "{}".parseJson
  val RequestTimeoutResponse = Error("Request timeout")

  case class WithProps(r: RequestContext, props: Props, message: RestMessage, p: Promise[RouteResult]) extends PerRequestActor {
    lazy val target: ActorRef = context.actorOf(props, "target")
  }
}

trait PerRequestCreator {
  def perRequest(r: RequestContext, props: Props, req: RestMessage, p: Promise[RouteResult])
                (implicit ac: ActorSystem): ActorRef =
    ac.actorOf(Props(classOf[WithProps], r, props, req, p), s"pr-${UUID.randomUUID().toString}")
}
