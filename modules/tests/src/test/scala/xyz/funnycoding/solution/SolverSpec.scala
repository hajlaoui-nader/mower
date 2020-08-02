package xyz.funnycoding.solution

import suite.PureTestSuite
import xyz.funnycoding.testdata.data._
import xyz.funnycoding.arbitrairies._
import cats.effect.IO
import xyz.funnycoding.domain.data.Mower
import xyz.funnycoding.modules.Solutions
import cats.effect.ContextShift

final class SolverSpec extends PureTestSuite {
  forAll { (c: CollisionInitialProblem) =>
    spec("collision detection") {

      for {
        solver <- Solutions.make[IO](c.initialProblem, implicitly[ContextShift[IO]])
        solution <- solver.solve
      } yield {
        assert(solution.mowers == c.initialProblem.mowersDescriptions.map(_.mower))
      }
    }

  }
  forAll { (nc: NonCollisionInitialProblem) =>
    spec("non collision") {

      for {
        solver <- Solutions.make[IO](nc.initialProblem, implicitly[ContextShift[IO]])
        solution <- solver.solve
      } yield {
        val expected: List[Mower] = Mower.forward(
            nc.initialProblem.mowersDescriptions
              .map(_.mower)
              .head
          ) :: nc.initialProblem.mowersDescriptions.map(_.mower).tail
        assert(solution.mowers == expected)
      }
    }

  }
}
