package co.ledger.template.service

import cats.effect.IO
import co.ledger.template.TestUsers.users
import co.ledger.template.model.UserName
import co.ledger.template.repository.algebra.UserRepository

object TestUserService {

  private val testUserRepo: UserRepository[IO] =
    (username: UserName) => IO {
      users.find(_.username.value == username.value)
    }

  val service: UserService[IO] = new UserService[IO](testUserRepo)

}
