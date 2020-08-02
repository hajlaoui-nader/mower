package xyz.funnycoding

import suite.PureTestSuite
import xyz.funnycoding.domain.data._
import xyz.funnycoding.domain.data.Lawn._
import xyz.funnycoding.arbitrairies._
import cats.effect.IO

final class LawnSpec extends PureTestSuite {
  forAll { (m: Mower) =>
    spec("forward north action") {
      IO {
        val mower    = m.copy(orientation = North)
        val lawn     = Lawn(Position(m.position.x + 10, m.position.y + 10), List(mower))
        val result   = action(lawn, mower, Forward)
        val expected = Mower.forward(mower)
        assert {
          result._1.occupied == List(expected) && result._2 == expected
        }
      }
    }
    spec("forward east action") {
      IO {
        val mower    = m.copy(orientation = East)
        val lawn     = Lawn(Position(m.position.x + 10, m.position.y + 10), List(mower))
        val result   = action(lawn, mower, Forward)
        val expected = Mower.forward(mower)
        assert {
          result._1.occupied == List(expected) && result._2 == expected
        }
      }
    }
    spec("forward west action") {
      IO {
        val mower    = m.copy(orientation = West)
        val lawn     = Lawn(Position(m.position.x + 10, m.position.y + 10), List(mower))
        val result   = action(lawn, mower, Forward)
        val expected = Mower.forward(mower)
        assert {
          result._1.occupied == List(expected) && result._2 == expected
        }
      }
    }
    spec("forward south action") {
      IO {
        val mower    = m.copy(orientation = South)
        val lawn     = Lawn(Position(m.position.x + 10, m.position.y + 10), List(mower))
        val result   = action(lawn, mower, Forward)
        val expected = Mower.forward(mower)
        assert {
          result._1.occupied == List(expected) && result._2 == expected
        }
      }
    }
    spec("left action") {
      IO {
        val lawn     = Lawn(Position(m.position.x + 10, m.position.y + 10), List(m))
        val result   = action(lawn, m, Left)
        val expected = m.copy(orientation = Orientation.left(m.orientation))
        assert {
          result._1.occupied == List(expected) && result._2 == expected
        }
      }
    }
    spec("right action") {
      IO {
        val lawn     = Lawn(Position(m.position.x + 10, m.position.y + 10), List(m))
        val result   = action(lawn, m, Right)
        val expected = m.copy(orientation = Orientation.right(m.orientation))
        assert {
          result._1.occupied == List(expected) && result._2 == expected
        }
      }
    }
  }
}
