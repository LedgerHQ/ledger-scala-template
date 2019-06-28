package co.ledger.template

import cats.effect.{IOApp, IO, ExitCode}
import co.ledger.template.config.Config
import cats.syntax.functor._

// The only place where the Effect is defined. You could change it for `TaskApp` and `monix.eval.Task` for example.
object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val config = pureconfig.loadConfigOrThrow[Config]
    HttpServer.defaultServer[IO].run(config).use(_ => IO.never).as(ExitCode.Success)
  }

}

