package org.example.controllers

import org.example.features.{HttpBehavior, MongoBehavior}
import play.api.Play.current
import play.api.libs.ws.{WS, WSClient}
import play.api.mvc.Controller
import play.api.{Configuration, Play}

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
object PlayDefaultExecutionContextImportController extends Controller with HttpBehavior with MongoBehavior {

  override def prefix = "play/import"
  override def configuration: Configuration = Play.configuration
  override def ws: WSClient = WS.client
  override val ec: ExecutionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
}
