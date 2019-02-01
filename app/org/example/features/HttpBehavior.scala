package org.example.features

import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import play.api.Configuration
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, Controller, RequestHeader}
import play.api.routing.Router
import play.api.routing.sird._

import scala.concurrent.{ExecutionContext, Future}

trait HttpBehavior {
  this: Controller =>

  def prefix: String
  def configuration: Configuration
  def ws: WSClient
  implicit def ec: ExecutionContext
  implicit def mat: Materializer

  def httpRoutes: Router.Routes = {
    case GET(p"/http/strict")   => strict()
    case GET(p"/http/stream")   => stream()
    case GET(p"/http/chained")  => chained()
    case GET(p"/http/parallel") => parallel()
    case GET(p"/http/traverse") => traverse()
    case GET(p"/http/sequence") => sequence()
  }

  def url(implicit req: RequestHeader): String = {
    req.getQueryString("url").getOrElse(s"http://${req.host}/work")
  }

  def call(implicit req: RequestHeader): Future[JsValue] = {
    ws.url(url)
      .get()
      .map(_.json)
  }

  def strict(): Action[AnyContent] = Action.async { implicit req =>
    call.map(Ok(_))
  }

  def stream(): Action[AnyContent] = Action.async { implicit req =>
    ws.url(url)
      .stream()
      .flatMap {
        _.body.runWith(Sink.fold[Long, ByteString](0L) { (total, bytes) => total + bytes.length
        })
      }
      .map(l => Json.obj("length" -> l))
      .map(Ok(_))
  }

  def chained(): Action[AnyContent] = Action.async { implicit req =>
    // should see 3 calls one after the other (staircase)
    for {
      r1 <- call
      r2 <- call
      r3 <- call
    } yield {
      Ok(Json.obj("r1" -> r1, "r2" -> r2, "r3" -> r3))
    }
  }

  def traverse(): Action[AnyContent] = Action.async { implicit req =>
    Future.traverse[Int, JsValue, Seq](1 to 10)(_ => call).map(l => Ok(Json.obj("list" -> l)))
  }

  def parallel(): Action[AnyContent] = Action.async { implicit req =>
    // should see 3 calls in parallel
    val f1 = call
    val f2 = call
    val f3 = call
    for {
      r1 <- f1
      r2 <- f2
      r3 <- f3
    } yield {
      Ok(Json.obj("r1" -> r1, "r2" -> r2, "r3" -> r3))
    }
  }

  def sequence(): Action[AnyContent] = Action.async { implicit req =>
    val f = (1 to 10).map(_ => call)
    Future.sequence(f).map(l => Ok(Json.obj("list" -> l)))
  }
}
