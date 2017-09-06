package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.Controller

import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class PlayDefaultExecutionContextInjectController @Inject()(
    val configuration: Configuration,
    val ws: WSClient,
    val ec: ExecutionContext
) extends Controller
    with HttpTraceController
    with MongoController {

  override def prefix = "play/inject"
}
