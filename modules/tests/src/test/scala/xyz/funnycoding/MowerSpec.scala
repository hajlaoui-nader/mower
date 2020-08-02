package xyz.funnycoding

import suite.PureTestSuite
import xyz.funnycoding.domain.data._
import xyz.funnycoding.domain.data.Orientation._
import xyz.funnycoding.arbitrairies._
import cats.effect.IO

final class MowerSpec extends PureTestSuite {
  forAll { (o: Orientation) =>
    spec("orientation identity left then right ") {
      IO { assert(right(left(o)) == o) }
    }
    spec("orientation identity right then left ") {
      IO { assert(left(right(o)) == o) }
    }
  }
  forAll { (m: Mower) =>
    spec("move forward increment") {
      IO {
        val forward = Mower.forward(m)
        m.orientation match {
          case North => assert(forward.position.y == m.position.y + 1)
          case South => assert(forward.position.y == m.position.y - 1)
          case East  => assert(forward.position.x == m.position.x + 1)
          case West  => assert(forward.position.x == m.position.x - 1)
        }
      }
    }
  }
}
