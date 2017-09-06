package controllers

import akka.actor.ActorSystem
import features.{HttpBehavior, MongoBehavior}
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
class AkkaCustomForkJoinExecutionContextController @Inject()(
    val configuration: Configuration,
    val ws: WSClient,
    as: ActorSystem
) extends Controller
    with SimpleRouter
    with HttpBehavior
    with MongoBehavior {

  override def prefix = "akka/fork-join"
  override def routes: Routes = httpRoutes orElse mongoRoutes
  override val ec: ExecutionContext =
    as.dispatchers.lookup("app.custom-fork-join-dispatcher")
}
