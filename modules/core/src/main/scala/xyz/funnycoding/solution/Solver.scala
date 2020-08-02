package xyz.funnycoding.solution

import xyz.funnycoding.domain.data.Mower
import xyz.funnycoding.domain.parser._
import xyz.funnycoding.domain.data._
import cats.effect._
import cats.implicits._
import cats.effect.concurrent.MVar
import cats.Parallel

final class Solver[F[_]: Concurrent: Parallel](initialProblem: InitialProblem, cs: ContextShift[F]) {

  case class Solution(mowers: List[Mower])

  def solve: F[Solution] = {
    val lawn: Lawn         = buildLawn(initialProblem)
    val mowersDescriptions = initialProblem.mowersDescriptions
    for {
      _ <- cs.shift
      state <- MVar.of[F, Lawn](lawn)
      descriptors = mowersDescriptions.map(md => new Worker[F](md, state))
      descriptorsFibers <- descriptors.parTraverse(w => Concurrent[F].start(w.solve))
      _ <- descriptorsFibers.traverse(_.join)
      result <- state.read
    } yield Solution(result.occupied)
  }

  def buildLawn(initialProblem: InitialProblem): Lawn =
    Lawn(initialProblem.ur, initialProblem.mowersDescriptions.map(_.mower))

}
