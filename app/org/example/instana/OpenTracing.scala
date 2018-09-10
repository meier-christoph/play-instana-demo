package org.example.instana

import com.instana.opentracing.InstanaTracer
import io.opentracing.propagation.{Format, TextMapExtractAdapter}
import io.opentracing.{Span, Tracer}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, ActionBuilder, BodyParser, Request, Result, WrappedRequest}

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * @author Christoph MEIER (TOP)
  */
class OpenTracingRequest[A](val span: Span, req: Request[A]) extends WrappedRequest[A](req)

case class OpenTracing[A](action: Action[A]) extends Action[A] {
  override def apply(request: Request[A]): Future[Result] = OpenTracingAction.invokeBlock(request, action.apply)
  override def parser: BodyParser[A] = action.parser
}

object OpenTracingAction extends ActionBuilder[OpenTracingRequest] {

  val tracer: Tracer = new InstanaTracer()

  override def invokeBlock[A](request: Request[A], block: (OpenTracingRequest[A]) => Future[Result]): Future[Result] = {
    val ctx = tracer.extract(
      Format.Builtin.HTTP_HEADERS,
      new TextMapExtractAdapter(request.headers.toSimpleMap.asJava)
    )

    val method = request.method.toLowerCase
    val route = request.path.toLowerCase.stripPrefix("/")

    val span = tracer
      .buildSpan(s"/http/$method/$route")
      .asChildOf(ctx)
      .withTag("span.kind", "server")
      .withTag("http.url", request.uri.toString)
      .withTag("http.method", request.method)
      .start()

    val future = block(new OpenTracingRequest(span, request))
    future.onComplete {
      case Success(resp) =>
        val status = resp.header.status
        span.setTag("http.status_code", status.toString)
        span.finish()
      case Failure(_) =>
        span.setTag("http.status_code", "500")
        span.setTag("error", true)
        span.finish()
    }
    future
  }
}
