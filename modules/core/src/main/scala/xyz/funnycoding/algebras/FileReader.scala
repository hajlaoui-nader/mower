package xyz.funnycoding.algebras

import cats.effect.Sync
import scala.io.Source

trait FileReader[F[_]] {
  def read(path: String): F[List[String]]
}

final class LiveFileReader[F[_]: Sync] extends FileReader[F] {
  override def read(path: String): F[List[String]] = Sync[F].delay(
    Source.fromResource(path).getLines().toList
  )
}

object LiveFileReader {
  def make[F[_]: Sync]: F[FileReader[F]] = Sync[F].delay(
    new LiveFileReader[F]()
  )
}
