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
class AkkaCustomForkJoinExecutionContextController @Inject()(
    val configuration: Configuration,
    val ws: WSClient,
    as: ActorSystem
) extends Controller
    with HttpTraceController
    with MongoController {

  override def prefix = "akka/fork-join"
  override val ec: ExecutionContext =
    as.dispatchers.lookup("app.custom-fork-join-dispatcher")
}
