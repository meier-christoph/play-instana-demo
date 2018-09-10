package org.example.controllers

import org.example.features.{HttpBehavior, MongoBehavior}
import play.api.Play.current
import play.api.libs.ws.{WS, WSClient}
import play.api.mvc.Controller
import play.api.{Configuration, Play}

import java.util.concurrent.Executors

import scala.concurrent.ExecutionContext

/**
  * @author Christoph MEIER (TOP)
  */
object ManualThreadPoolExecutionContextController extends Controller with HttpBehavior with MongoBehavior {

  override def prefix = "manual/thread-pool"
  override def configuration: Configuration = Play.configuration
  override def ws: WSClient = WS.client
  override val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(10)
  )
}
