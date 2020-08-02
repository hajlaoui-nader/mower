package xyz.funnycoding.modules

import xyz.funnycoding.domain.parser._
import xyz.funnycoding.solution._
import cats.effect._
import cats.Parallel

object Solutions {
  def make[F[_]: Concurrent: Parallel](initialProblem: InitialProblem, cs: ContextShift[F]): F[Solver[F]] =
    Sync[F].delay(
      new Solver[F](initialProblem, cs)
    )
}
