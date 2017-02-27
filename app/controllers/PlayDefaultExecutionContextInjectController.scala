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

  val url = cfg.getString("app.test-url").get

  def index() = Action.async {
    ws.url(url).get().map { resp =>
      Ok(resp.json)
    }
  }

  def nested() = Action.async {
    ws.url(url).get().flatMap { resp =>
      ws.url(url).get().map { resp =>
        Ok(resp.json)
      }
    }
  }
}
