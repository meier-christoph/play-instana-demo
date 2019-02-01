package org.example.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

class SimulateWorkController extends Controller {
  def work(): Action[AnyContent] = Action {
    Thread.sleep(750) // simulate some work
    Ok(Json.obj("work" -> "done"))
  }
}
