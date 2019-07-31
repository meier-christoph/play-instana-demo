package org.example.controllers

import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.routing.Router

class SimulateWorkController extends Controller {
  def work0(): Action[AnyContent] = Action { rh =>
    Logger.info(s"ctrl template: ${rh.tags.get(Router.Tags.RoutePattern)}")

    // FIXME: similar to the filter the tags are also available in the Controller

    Thread.sleep(750) // simulate some work
    Ok(Json.obj("work" -> "done"))
  }

  def work1(s: String): Action[AnyContent] = work0()

  def work2(s: String, i: Int): Action[AnyContent] = work0()
}
