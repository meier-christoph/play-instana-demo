import akka.stream.Materializer
import javax.inject.Inject
import play.api.Logger
import play.api.http.DefaultHttpFilters
import play.api.mvc.{Filter, RequestHeader, Result}
import play.filters.gzip.GzipFilter

import scala.concurrent.Future

class Filters @Inject()(gzipFilter: GzipFilter, logging: Filters.LoggingFilter)
    extends DefaultHttpFilters(gzipFilter, logging)

object Filters {
  class LoggingFilter @Inject()(implicit val mat: Materializer) extends Filter {
    override def apply(f: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
      Logger.info(s"request: $rh")
      f(rh)
    }
  }
}
