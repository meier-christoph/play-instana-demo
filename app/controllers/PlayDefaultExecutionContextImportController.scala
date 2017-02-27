package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.Controller

import javax.inject.Inject

/**
  * @author Christoph MEIER (TOP)
  */
class PlayDefaultExecutionContextImportController @Inject()
(val configuration: Configuration, val ws: WSClient)
  extends Controller
    with HttpTraceController {

  val ec = play.api.libs.concurrent.Execution.Implicits.defaultContext

}
