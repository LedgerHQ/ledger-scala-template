package co.ledger.template.service

import cats.data.EitherT
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import co.ledger.template.IOAssertion
import co.ledger.template.TestUsers._
import co.ledger.template.model.{UserName, UserNotFound}

class UserServiceSpec extends AnyFlatSpecLike with Matchers {

  it should "retrieve an user" in IOAssertion {
    EitherT(TestUserService.service.flatMap(_.findUser(users.head.username))).map { user =>
      user should be (users.head)
    }.value
  }

  it should "fail retrieving an user" in IOAssertion {
    EitherT(TestUserService.service.flatMap(_.findUser(UserName("xxx")))).leftMap { error =>
      error shouldBe a [UserNotFound]
    }.value
  }

}
