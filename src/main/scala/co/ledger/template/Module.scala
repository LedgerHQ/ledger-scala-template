package co.ledger.template

import cats.effect.{Async, Blocker, ContextShift, Resource}
import co.ledger.template.http.{HttpErrorHandler, UserHttpRoutes}
import co.ledger.template.repository.PostgresUserRepository
import co.ledger.template.repository.algebra.UserRepository
import co.ledger.template.service.UserService
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.implicits._
import org.http4s.{HttpApp, HttpRoutes}

// Custom DI module
class Module[F[_] : Async : ContextShift] {

  // Resource yielding a transactor configured with a bounded connect EC and an unbounded
  // transaction EC. Everything will be closed and shut down cleanly after use.
  val transactor: Resource[F, HikariTransactor[F]] = for {
    ce <- ExecutionContexts.fixedThreadPool[F](32) // our connect EC
    te <- ExecutionContexts.cachedThreadPool[F] // our transaction EC
    xa <- HikariTransactor.newHikariTransactor[F](
      "org.postgresql.Driver", // driver classname
      "jdbc:postgresql:users", // connect URL
      "postgres", // username
      "postgres", // password
      ce, // await connection here
      Blocker.liftExecutionContext(te) // execute JDBC operations here
    )
  } yield xa

  val httpApp: Resource[F, HttpApp[F]] = transactor.map { xa =>
    val userRepository: UserRepository[F] = new PostgresUserRepository[F](xa)

    val userService: UserService[F] = new UserService[F](userRepository)

    implicit val httpErrorHandler: HttpErrorHandler[F] = new HttpErrorHandler[F]

    val userRoutes: HttpRoutes[F] = new UserHttpRoutes[F](userService).routes

    userRoutes.orNotFound
  }

}
