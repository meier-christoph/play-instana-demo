package org.example.sentry

import io.sentry.SentryClient
import io.sentry.event.interfaces.{ExceptionInterface, MessageInterface, UserInterface}
import io.sentry.event.{Breadcrumb, BreadcrumbBuilder, Event, EventBuilder}
import play.api.http.HttpErrorHandler
import play.api.mvc.{RequestHeader, Result, Results}
import play.core.PlayVersion

import java.util.Date
import javax.inject.Inject

import scala.collection.JavaConverters._
import scala.concurrent.Future

class ErrorHandler @Inject()(sentry: SentryClient) extends HttpErrorHandler {
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(Results.BadRequest(message))
  }
  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    sentry.sendEvent(
      new EventBuilder()
        .withRelease("1.0.0") // read from build info
        .withEnvironment("dev") // read from env var
        .withTag("play.version", PlayVersion.current)
        .withTag("java.version", System.getProperty("java.version"))
        .withLevel(Event.Level.ERROR)
        .withMessage(exception.getMessage)
        .withSentryInterface(new MessageInterface("fatal"))
        .withSentryInterface(new UserInterface("meier.c", null, null, null))
        .withSentryInterface(new ExceptionInterface(exception))
        // TODO .withSentryInterface(new HttpInterface())
        .withBreadcrumbs(Seq(
          new BreadcrumbBuilder()
            .setTimestamp(new Date())
            .setLevel(Breadcrumb.Level.INFO)
            .setType(Breadcrumb.Type.HTTP)
            .setCategory("http")
            .setMessage("Failed to do something")
            .build(),
          new BreadcrumbBuilder()
            .setTimestamp(new Date())
            .setLevel(Breadcrumb.Level.WARNING)
            .setType(Breadcrumb.Type.DEFAULT)
            .setCategory("default")
            .setMessage("Failed to do something")
            .build(),
          new BreadcrumbBuilder()
            .setTimestamp(new Date())
            .setLevel(Breadcrumb.Level.ERROR)
            .setType(Breadcrumb.Type.USER)
            .setCategory("user")
            .setMessage("Failed to do something")
            .build(),
          new BreadcrumbBuilder()
            .setTimestamp(new Date())
            .setLevel(Breadcrumb.Level.ERROR)
            .setType(Breadcrumb.Type.NAVIGATION)
            .setCategory("navigation")
            .setMessage("Failed to do something")
            .build()
        ).asJava)
        .build())
    Future.successful(Results.InternalServerError("todo"))
  }
}
