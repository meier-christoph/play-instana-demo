package controllers

import play.api.Configuration
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Christoph MEIER (TOP)
  */
trait HttpTraceController {
  this: Controller =>

  def prefix: String
  def configuration: Configuration
  def ws: WSClient
  def ec: ExecutionContext

  val url: String = configuration
    .getString("app.test-url")
    .getOrElse(throw new IllegalStateException("missing config"))

  def index(): Action[AnyContent] = Action.async {
    // http get will run on internal ws client executor
    // while the map will be executed on the local executor
    // internal > local
    ws.url(url)
      .get()
      .map { resp =>
        Ok(resp.json)
      }(ec)
  }

  def future(): Action[AnyContent] = Action.async {
    // future will use local executor
    // the http calls will use the internal ws client executor
    // but the map and flatMap will use the local executor again
    // local > internal > local > local > local > internal > local
    Future {
      ws.url(url)
        .get()
        .map { resp =>
          resp // using map to force usage of local executor
        }(ec)
    }(ec).flatMap { t =>
      // flatten the responses on local executor (twice)
      t.flatMap { resp =>
        ws.url(url)
          .get()
          .map { res => // use internal executor again
            // but force local executor on map
            Ok(Json.obj("call" -> resp.json) ++ res.json.as[JsObject])
          }(ec)
      }(ec)
    }(ec)
  }

  def http(): Action[AnyContent] = Action.async {
    // map and flatMap use local executor
    // http calls use the internal ws client executor
    // internal > local > internal > local
    ws.url(url)
      .get()
      .flatMap { resp =>
        ws.url(url)
          .get()
          .map { res =>
            Ok(Json.obj("call" -> resp.json) ++ res.json.as[JsObject])
          }(ec)
      }(ec)
  }
}
