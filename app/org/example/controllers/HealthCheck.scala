package org.example.controllers
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

class HealthCheck extends Controller {
  def health(): Action[AnyContent] = Action {
    Ok(Json.obj("status" -> "ok"))
  }
}
