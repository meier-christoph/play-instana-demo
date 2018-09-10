import play.api.mvc.WithFilters
import play.filters.gzip.GzipFilter

/**
  * @author Christoph MEIER (TOP)
  */
object Global extends WithFilters(new GzipFilter())
