package org.example.controllers

import play.api.mvc.{Action, AnyContent, Controller}

object CrashController extends Controller {
  def crash: Action[AnyContent] = Action {
    InternalServerError("crash")
  }

  def fatal: Action[AnyContent] = Action.async { _ =>
    throw new RuntimeException("fatal")
  }
}
