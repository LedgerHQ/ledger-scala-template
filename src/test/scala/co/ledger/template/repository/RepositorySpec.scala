package co.ledger.template.repository

import cats.effect.{ContextShift, IO}
import doobie.scalatest.IOChecker
import doobie.util.transactor.Transactor
import org.flywaydb.core.Flyway
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

import scala.concurrent.ExecutionContext

trait RepositorySpec extends FunSuiteLike with BeforeAndAfterAll with IOChecker {

  val ec: ExecutionContext = ExecutionContext.global
  implicit val cs: ContextShift[IO] = IO.contextShift(ec)

  val dbUrl   = "jdbc:h2:mem:users;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
  val dbUser  = "sa"
  val dbPass  = ""


  /** doobie is migrating to Resource for the transactor.
    * But the IOChecker doesn't adapt yet, so here we generate transactor
    * directly. Should be changed when doobie updates it.
    */
  override def transactor: doobie.Transactor[IO] =
    Transactor.fromDriverManager("org.h2.Driver", dbUrl, dbUser, dbPass)

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
