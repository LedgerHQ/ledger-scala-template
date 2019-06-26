package co.ledger.template.repository

import co.ledger.template.model.{User, UserName}

object algebra {

  trait UserRepository[F[_]] {
    def findUser(username: UserName): F[Option[User]]
    def addUser(user: User): F[Unit]
    def updateUser(user: User): F[Unit]
    def deleteUser(username: UserName): F[Unit]
  }

}
