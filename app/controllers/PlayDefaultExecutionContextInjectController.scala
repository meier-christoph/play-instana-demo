package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class PlayDefaultExecutionContextInjectController @Inject()
(cfg: Configuration, ws: WSClient)
(implicit ec: ExecutionContext) // using the default play context via inject
  extends Controller {

  val port = cfg.getInt("http.port").getOrElse(9000)

  def index() = Action.async {
    ws.url(s"http://127.0.0.1:$port/callback").get().map { resp =>
      Ok(resp.json)
    }
  }

  def nested() = Action.async {
    ws.url(s"http://127.0.0.1:$port/callback").get().flatMap { resp =>
      ws.url(s"http://127.0.0.1:$port/callback").get().map { resp =>
        Ok(resp.json)
      }
    }
  }
}
