package org.example.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

/**
  * This is the callback we will try to call from all the other controllers.
  *
  * @author Christoph MEIER (TOP)
  */
class CallbackController extends Controller {

  def index() = Action {
    Ok(Json.obj("status" -> "ok"))
  }
}
