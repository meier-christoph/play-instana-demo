package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import javax.inject.Inject

/**
  * @author Christoph MEIER (TOP)
  */
class ScalaDefaultExecutionContextImportController @Inject()
(cfg: Configuration, ws: WSClient)
  extends Controller {

  val url = cfg.getString("app.test-url").get

  // using the default scala context via import
  import scala.concurrent.ExecutionContext.Implicits.global

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
