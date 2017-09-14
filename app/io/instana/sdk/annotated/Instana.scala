package io.instana.sdk.annotated

import com.instana.sdk.annotation.Span
import com.instana.sdk.annotation.Span.{End, Start}
import com.instana.sdk.support.{ContextSupport, SpanSupport}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future
import scala.util.Try

/**
  * @author Christoph MEIER (TOP)
  */
object Instana {

  def apply[A](block: => Future[A]): Future[A] = {
    start()
    SpanSupport.annotate(Span.Type.INTERMEDIATE, "Text 1", "foobar")
    SpanSupport.annotate(Span.Type.INTERMEDIATE, "Markdown 1", "_foobar_")
    SpanSupport.annotate(Span.Type.INTERMEDIATE, "Html 1", "<b>foobar</b>")
    val snapshot = ContextSupport.takeSnapshot()
    val future = block
    future.onComplete(r => complete(r, snapshot))
    future
  }

  @Start("demo")
  def start(): Unit = ()

  @End("demo")
  def end(): Unit = ()

  def complete(result: Try[_], snapshot: Any): Unit = {
    ContextSupport.restoreSnapshot(snapshot)
    SpanSupport.annotate(Span.Type.INTERMEDIATE, "Text 2", "foobar")
    SpanSupport.annotate(Span.Type.INTERMEDIATE, "Markdown 2", "_foobar_")
    SpanSupport.annotate(Span.Type.INTERMEDIATE, "Html 2", "<b>foobar</b>")
    end()
  }
}
