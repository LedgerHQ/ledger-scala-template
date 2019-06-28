package co.ledger.template

import pureconfig.generic.semiauto._
import pureconfig.generic.DerivedConfigReader._
import pureconfig.ConfigReader

object config {
  case class Config(postgres: PostgresConfig, server: ServerConfig)

  // implicit instances for a type go in to the companion object
  object Config {
    implicit val configReader: ConfigReader[Config] = deriveReader[Config]
  }

  case class ServerConfig(
    host: String,
    port: Int
  )
  case class PostgresConfig(
    url: String,
    user: String,
    password: String
  ) {
    def driver: String = "org.postgresql.Driver"
  }
  
}
