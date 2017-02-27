package controllers

import akka.actor.ActorSystem
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import javax.inject.Inject

/**
  * @author Christoph MEIER (TOP)
  */
class AkkaCustomForkJoinExecutionContextController @Inject()
(cfg: Configuration, ws: WSClient, as: ActorSystem)
  extends Controller {

  val url = cfg.getString("app.test-url").get
  implicit val ec = as.dispatchers.lookup("app.custom-fork-join-dispatcher")

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
