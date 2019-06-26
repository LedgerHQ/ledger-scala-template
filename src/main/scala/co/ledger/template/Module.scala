package co.ledger.template

import cats.effect.{Async, ContextShift}
import doobie.util.transactor.Transactor
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits._
import co.ledger.template.http.{HttpErrorHandler, UserHttpRoutes}
import co.ledger.template.repository.PostgresUserRepository
import co.ledger.template.repository.algebra.UserRepository
import co.ledger.template.service.UserService

// Custom DI module
class Module[F[_]: Async: ContextShift] {

  private val xa: Transactor[F] =
    Transactor.fromDriverManager[F](
      "org.postgresql.Driver", "jdbc:postgresql:users", "postgres", "postgres"
    )

  private val userRepository: UserRepository[F] = new PostgresUserRepository[F](xa)

  private val userService: UserService[F] = new UserService[F](userRepository)

  implicit val httpErrorHandler: HttpErrorHandler[F] = new HttpErrorHandler[F]

  private  val userRoutes: HttpRoutes[F] = new UserHttpRoutes[F](userService).routes

  val httpApp: HttpApp[F] = userRoutes.orNotFound

}
