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

  val url = cfg.getString("app.test-url").get
  implicit val ec = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(10)
  )

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
