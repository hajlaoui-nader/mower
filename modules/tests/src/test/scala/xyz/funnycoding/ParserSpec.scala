package xyz.funnycoding

import suite.PureTestSuite
import xyz.funnycoding.arbitrairies._
import xyz.funnycoding.domain.data._
import xyz.funnycoding.domain.parser._
import cats.effect.IO

final class ParserSpec extends PureTestSuite {

  forAll { (ip: InitialProblem) =>
    spec("parsing identity") {
      IO {
        InitialProblem
          .raw(toRaw(ip))
          .fold(
            fe => fail(fe.toNonEmptyList.toList.mkString),
            res => assert(res == ip)
          )
      }
    }
  }

  private def toRaw(ip: InitialProblem): List[String] =
    List(toRawPosition(ip.ur)) ++ toRawMowersDescriptions(ip.mowersDescriptions)

  private def toRawPosition(p: Position): String =
    s"${p.x} ${p.y}"

  private def toRawMowersDescriptions(mds: List[MowerDescription]): List[String] =
    mds.flatMap(toRawMowerDescription)

  private def toRawMowerDescription(m: MowerDescription): List[String] = {
    val lc = s"${m.commands.map(toRawCommand).mkString}"
    List(s"${m.mower.position.x} ${m.mower.position.y} ${toRawOrientation(m.mower.orientation)}", lc)
  }

  private def toRawOrientation(o: Orientation): String = o match {
    case North => "N"
    case South => "S"
    case East  => "E"
    case West  => "W"
  }

  private def toRawCommand(c: Command): Char = c match {
    case Forward => 'F'
    case Right   => 'R'
    case Left    => 'L'
  }
}
