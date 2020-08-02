package xyz.funnycoding.testdata

import xyz.funnycoding.domain.parser.InitialProblem

object data {
  case class CollisionInitialProblem(initialProblem: InitialProblem)
  case class NonCollisionInitialProblem(initialProblem: InitialProblem)
}
