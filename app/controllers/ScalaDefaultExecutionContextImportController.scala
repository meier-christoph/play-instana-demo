package controllers

import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.Controller

import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class ScalaDefaultExecutionContextImportController @Inject()(
    val configuration: Configuration,
    val ws: WSClient
) extends Controller
    with HttpTraceController
    with MongoController {

  override def prefix = "scala"
  override val ec: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global
}
