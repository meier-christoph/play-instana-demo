import play.api.http.DefaultHttpFilters
import play.filters.gzip.GzipFilter

import javax.inject.Inject

/**
  * @author Christoph MEIER (TOP)
  */
class Filters @Inject()(gzipFilter: GzipFilter)
    extends DefaultHttpFilters(gzipFilter)
