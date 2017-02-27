package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import java.util.concurrent.Executors
import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class ManualThreadPoolExecutionContextController @Inject()
(cfg: Configuration, ws: WSClient)
  extends Controller {

  val port = cfg.getInt("http.port").getOrElse(9000)
  implicit val ec = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(10)
  )

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
