package co.ledger.template.repository

import cats.effect.{ContextShift, IO}
import doobie.scalatest.IOChecker
import org.flywaydb.core.Flyway
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

import scala.concurrent.ExecutionContext

trait RepositorySpec extends FunSuiteLike with BeforeAndAfterAll with IOChecker {

  val ec: ExecutionContext = ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(ec)

  val dbUrl   = "jdbc:h2:mem:users;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
  val dbUser  = "sa"
  val dbPass  = ""

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    migrateDB.unsafeRunSync()
  }

  private val migrateDB: IO[Unit] =
    IO {
      val flyway = new Flyway
      flyway.setDataSource(dbUrl, dbUser, dbPass)
      flyway.migrate()
    }

}
