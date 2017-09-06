package controllers

import akka.actor.ActorSystem
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.Controller

import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class AkkaCustomThreadPoolExecutionContextController @Inject()(
    val configuration: Configuration,
    val ws: WSClient,
    as: ActorSystem
) extends Controller
    with HttpTraceController
    with MongoController {

  override def prefix = "akka/thread-pool"
  override val ec: ExecutionContext =
    as.dispatchers.lookup("app.custom-thread-pool-dispatcher")
}
