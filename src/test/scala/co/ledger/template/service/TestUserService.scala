package co.ledger.template.service

import cats.effect.IO
import cats.syntax.applicative._
import co.ledger.template.TestUsers.users
import co.ledger.template.model
import co.ledger.template.model.UserName
import co.ledger.template.repository.algebra.UserRepository

import scala.collection.mutable

object TestUserService {

  private val testUserRepo: UserRepository[IO] = new UserRepository[IO] {

    val repo: mutable.Buffer[model.User] = scala.collection.mutable.Buffer(users:_*)

    override def findUser(username: UserName): IO[Option[model.User]] =
      repo.find(_.username.value == username.value).pure[IO]

    override def addUser(user: model.User): IO[Unit] = IO{
      repo += user
    }

    override def updateUser(user: model.User): IO[Unit] = IO{
      repo.indexWhere(_.username.value == user.username.value) match {
        case -1 => IO.raiseError(new Exception(s"no user ${user.username.value} in the repo"))
        case i => repo.update(i, user)
      }
    }

    override def deleteUser(username: UserName): IO[Unit] = IO {
      repo.indexWhere(_.username.value == username.value) match {
        case -1 => IO.raiseError(new Exception(s"no user ${username.value} in the repo"))
        case i => repo.remove(i)
      }
    }
  }

  val service: UserService[IO] = new UserService[IO](testUserRepo)

}
