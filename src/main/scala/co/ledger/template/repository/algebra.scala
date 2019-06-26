package co.ledger.template.repository

import co.ledger.template.model.{User, UserName}

object algebra {

  trait UserRepository[F[_]] {
    def findUser(username: UserName): F[Option[User]]
  }

}
