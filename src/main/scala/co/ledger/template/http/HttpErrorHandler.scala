package co.ledger.template.http

import cats.Monad
import org.http4s.Response
import org.http4s.dsl.Http4sDsl
import co.ledger.template.model.{
  ApiError,
  OtherError,
  UserAlreadyExist,
  UserNotFound
}

class HttpErrorHandler[F[_]: Monad] extends Http4sDsl[F] {

  // Map your business errors to responses here
  val handle: ApiError => F[Response[F]] = {
    case UserNotFound(u) => NotFound(s"User not found ${u.value}")
    case UserAlreadyExist(username) =>
      BadRequest(s"User ${username.value} already exist")
    case OtherError(msg) => BadRequest(msg)
  }

}
