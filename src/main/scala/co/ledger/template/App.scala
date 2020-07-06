package co.ledger.template

import cats.effect.{ExitCode, IO, IOApp}
import co.ledger.template.config.Config
import pureconfig.ConfigSource

// The only place where the Effect is defined. You could change it for `TaskApp` and `monix.eval.Task` for example.
object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val config = ConfigSource.default.loadOrThrow[Config]
    HttpServer
      .defaultServer[IO]
      .run(config)
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

}
