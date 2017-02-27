package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.Controller

import java.util.concurrent.Executors
import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class ManualThreadPoolExecutionContextController @Inject()
(val configuration: Configuration, val ws: WSClient)
  extends Controller
    with HttpTraceController {

  val ec = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(10)
  )

}
