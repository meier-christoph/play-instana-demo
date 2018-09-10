package org.example.sentry

import io.sentry.{SentryClient, SentryClientFactory}
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}

class SentryModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = {
    val client = SentryClientFactory.sentryClient()
    Seq(
      bind[SentryClient].toInstance(client).eagerly()
    )
  }
}
