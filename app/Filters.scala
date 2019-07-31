import akka.stream.Materializer
import javax.inject.Inject
import play.api.Logger
import play.api.http.DefaultHttpFilters
import play.api.mvc.{Filter, RequestHeader, Result}
import play.api.routing.Router

import scala.concurrent.Future

class Filters @Inject()(logging: Filters.LoggingFilter) extends DefaultHttpFilters(logging)

object Filters {
  class LoggingFilter @Inject()(implicit val mat: Materializer) extends Filter {
    override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
      Logger.info(s"filter template: ${rh.tags.get(Router.Tags.RoutePattern)}")

      // FIXME: the route template is available via tags but be aware that this has changed slightly in every
      //        version of play 2. Also you will need to sanitize it as there are some regex patterns in there.

      f(rh)
    }
  }
}
