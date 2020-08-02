package xyz.funnycoding.domain

import xyz.funnycoding.domain.data._
import cats.data._
import cats.implicits._
import scala.util.{ Failure, Success, Try }
import scala.collection.immutable.Nil

object parser {
  case class InitialProblem(ur: Position, mowersDescriptions: List[MowerDescription])

  sealed trait ParserError
  case object InvalidRawConstruct extends ParserError
  case object InvalidRawMowerDescription extends ParserError
  case object InvalidUpperRightPosition extends ParserError
  case object InvalidInt extends ParserError
  case object InvalidOrientation extends ParserError
  case object InvalidCommand extends ParserError

  object InitialProblem {
    def raw(xs: List[String]): ValidatedNec[ParserError, InitialProblem] = xs match {
      case rawPosition :: rawMowersDescriptions =>
        (upperRightPosition(rawPosition), mowersDescriptions(rawMowersDescriptions))
          .mapN {
            case (p, md) => InitialProblem(p, md)
          }
      case _ => InvalidRawConstruct.invalidNec[InitialProblem]
    }

    private def upperRightPosition(str: String): ValidatedNec[ParserError, Position] =
      str.split(" ").toList match {
        case rawx :: rawy :: Nil =>
          (int(rawx), int(rawy)).mapN(Position)
        case _ => InvalidUpperRightPosition.invalidNec[Position]
      }

    private def mowersDescriptions(xs: List[String]): ValidatedNec[ParserError, List[MowerDescription]] =
      xs.sliding(2, 2).toList.traverse(mowerDescription)

    private def mowerDescription(xs: List[String]): ValidatedNec[ParserError, MowerDescription] = xs match {
      case rawMower :: rawCommands :: Nil => (mower(rawMower), commands(rawCommands)).mapN(MowerDescription)
      case _                              => InvalidRawMowerDescription.invalidNec[MowerDescription]
    }

    private def mower(str: String): ValidatedNec[ParserError, Mower] =
      str.split(" ").toList match {
        case rawx :: rawy :: rawOrientation :: Nil =>
          (int(rawx), int(rawy), orientation(rawOrientation)).mapN {
            case (x, y, o) => Mower(Position(x, y), o)
          }
        case _ => InvalidRawMowerDescription.invalidNec[Mower]
      }

    private def commands(str: String): ValidatedNec[ParserError, List[Command]] =
      str.map(command).toList.sequence

    private def command(c: Char): ValidatedNec[ParserError, Command] =
      c match {
        case 'L' => Left.validNec
        case 'R' => Right.validNec
        case 'F' => Forward.validNec
        case _   => InvalidCommand.invalidNec

      }

    private def orientation(str: String): ValidatedNec[ParserError, Orientation] = str match {
      case "N" => North.validNec
      case "E" => East.validNec
      case "W" => West.validNec
      case "S" => South.validNec
      case "_" => InvalidOrientation.invalidNec[Orientation]

    }

    private def int(str: String): ValidatedNec[ParserError, Int] =
      Try(str.toInt) match {
        case Failure(_) => InvalidInt.invalidNec
        case Success(x) => x.validNec
      }
  }
}
