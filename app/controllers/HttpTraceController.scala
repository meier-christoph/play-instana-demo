package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Christoph MEIER (TOP)
  */
trait HttpTraceController {
  this: Controller =>

  def configuration: Configuration

  def ws: WSClient

  def ec: ExecutionContext

  val url = configuration.getString("app.test-url").get

  def index() = Action.async {
    ws.url(url).get().map { resp =>
      Ok(resp.json)
    }(ec)
  }

  def future() = Action.async {
    Future {
      ws.url(url).get().map { resp =>
        Ok(resp.json)
      }(ec)
    }(ec).flatMap(t => t)(ec)
  }

  def http() = Action.async {
    ws.url(url).get().flatMap { resp =>
      ws.url(url).get().map { resp =>
        Ok(resp.json)
      }(ec)
    }(ec)
  }
}
