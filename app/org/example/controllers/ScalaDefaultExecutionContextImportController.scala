package org.example.controllers

import org.example.features.{HttpBehavior, MongoBehavior}
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.Controller
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter

import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class ScalaDefaultExecutionContextImportController @Inject()(
    val configuration: Configuration,
    val ws: WSClient
) extends Controller
    with SimpleRouter
    with HttpBehavior
    with MongoBehavior {

  override def prefix = "scala"
  override def routes: Routes = httpRoutes orElse mongoRoutes
  override val ec: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global
}
