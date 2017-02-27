package controllers

import akka.actor.ActorSystem
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import javax.inject.Inject

/**
  * @author Christoph MEIER (TOP)
  */
class AkkaCustomThreadPoolExecutionContextController @Inject()
(cfg: Configuration, ws: WSClient, as: ActorSystem)
  extends Controller {

  val port = cfg.getInt("http.port").getOrElse(9000)
  implicit val ec = as.dispatchers.lookup("app.custom-thread-pool-dispatcher")

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
