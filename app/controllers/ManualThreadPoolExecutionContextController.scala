package controllers

import features.{HttpBehavior, MongoBehavior}
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.Controller
import play.api.routing.Router.Routes
import play.api.routing.{Router, SimpleRouter}

import java.util.concurrent.Executors
import javax.inject.Inject

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
class ManualThreadPoolExecutionContextController @Inject()(
    val configuration: Configuration,
    val ws: WSClient
) extends Controller
    with SimpleRouter
    with HttpBehavior
    with MongoBehavior {

  override def prefix = "manual/thread-pool"
  override def routes: Routes = httpRoutes orElse mongoRoutes
  override val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(10)
  )
}
