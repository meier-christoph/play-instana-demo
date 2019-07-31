package org.example.controllers

import akka.util.ByteString
import javax.inject.Inject
import play.api.libs.streams.Accumulator
import play.api.mvc.{EssentialAction, RequestHeader, RequestTaggingHandler, Result}
import play.api.routing.Router.Routes
import play.api.routing.sird._
import play.api.routing.{Router, SimpleRouter}

class MySimpleRouter @Inject()(ctrl: SimulateWorkController) extends SimpleRouter {
  override def routes: Routes = {
    case GET(p"/work") =>
      tagTemplate("/work")(ctrl.work0())
    case GET(p"/work/without/tags") =>
      ctrl.work0() // FIXME: when using sird, this is usually all you would write as most apps don't care about the template tags
    case GET(p"/work/with/multiple/levels") =>
      tagTemplate("/work/with/multiple/levels")(ctrl.work0())
    case GET(p"/work/with/$params/of/different/${int(types)}") =>
      tagTemplate("/work/with/{params}/of/different/{types}")(ctrl.work2(params, types))
    case GET(p"/work/with/$patterns<[a-z]+>") =>
      tagTemplate("/work/with/{patterns}")(ctrl.work1(patterns)) // FIXME: you may have a different naming convention for patterns when doing it manually, play doesn't care
  }

  // FIXME: when using the sbt plugin and the routes file, this code will be generated for you
  //        on the other hand, when using the sird router dsl you would actually need to do this
  //        by hand, not ideal but at the very least doable

  def tagTemplate[T](tpl: String)(action: EssentialAction): EssentialAction =
    new EssentialAction with RequestTaggingHandler {
      def apply(rh: RequestHeader): Accumulator[ByteString, Result] =
        action(rh)
      def tagRequest(rh: RequestHeader): RequestHeader =
        rh.copy(tags = rh.tags ++ Map(Router.Tags.RoutePattern -> tpl))
    }
}
