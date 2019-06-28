package co.ledger

import co.ledger.template.config.Config
import cats.data.ReaderT
import cats.effect.Resource

package object template {

  type Configured[F[_], T] = ReaderT[F, Config, T]
  // '?' is brought by type projector compiler plugin
  type ConfiguredResource[F[_], T] = Configured[Resource[F, ?], T]

}
