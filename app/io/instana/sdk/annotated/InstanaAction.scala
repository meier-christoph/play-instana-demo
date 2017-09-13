// using a package name which should not collide with existing code
package io.instana.sdk.annotated

import com.instana.sdk.annotation.Span.{End, Start}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * @author Christoph MEIER (TOP)
  */
object InstanaAction extends ActionBuilder[Request] {

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    val method = request.method.toLowerCase
    val route = request.path.toLowerCase.stripPrefix("/")
    val name = s"/http/$method/$route"

    start(name)
    val future = block(request)
    future.onComplete {
      case Success(resp) =>
        success(resp)
      case Failure(err) =>
        failure(err)
    }
    future
  }

  @Start(value = "/http", captureArguments = true)
  def start(name: String): Unit = ()

  @End(value = "/http", captureReturn = true)
  def success(result: Result): String = result.header.status.toString

  @End(value = "/http", captureReturn = true)
  def failure(err: Throwable): String = err.getMessage
}
