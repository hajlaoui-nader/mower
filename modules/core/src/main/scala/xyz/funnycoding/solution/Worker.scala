package xyz.funnycoding.solution

import cats.effect._
import xyz.funnycoding.domain.data._
import xyz.funnycoding.domain.data.Orientation._
import xyz.funnycoding.domain.data.Lawn._
import xyz.funnycoding.domain.data.Mower._
import cats.effect.concurrent._
import cats.implicits._

class Worker[F[_]: Sync](mowerDescription: MowerDescription, state: MVar[F, Lawn]) {
  def solve: F[Mower] = {
    def solveImpl(mower: Mower, commands: List[Command]): F[Mower] =
      commands.headOption match {
        case None => {
          Sync[F].pure(mower)
        }
        case Some(c) =>
          for {
            lawn <- state.take
            actionable = possibleMouvement(lawn, mower, c) && possibleAction(lawn, mower, c)
            m <- if (actionable) {
                  val update   = action(lawn, mower, c)
                  val newLawn  = update._1
                  val newMower = update._2
                  state.put(newLawn) *> solveImpl(newMower, commands.tail)
                } else
                  state.put(lawn) *>
                    solveImpl(mower, commands.tail)
          } yield m
      }
    solveImpl(mowerDescription.mower, mowerDescription.commands)
  }

  private def possibleAction(lawn: Lawn, mower: Mower, command: Command): Boolean = {
    val ePosition = estimatedPosition(command, mower).position
    if (ePosition == mower.position)
      true
    else
      !lawn.occupied.map(_.position).contains(ePosition)

  }

  private def estimatedPosition(command: Command, mower: Mower): Mower = command match {
    case Forward => {
      forward(mower)
    }
    case Right => mower.copy(orientation = right(mower.orientation))
    case Left  => mower.copy(orientation = left(mower.orientation))
  }

  private def possibleMouvement(lawn: Lawn, mower: Mower, command: Command) = command match {
    case Forward =>
      mower.orientation match {
        case North => mower.position.y + 1 <= lawn.ur.y && mower.position.y + 1 >= 0
        case South => mower.position.y - 1 <= lawn.ur.y && mower.position.y - 1 >= 0
        case East  => mower.position.x + 1 <= lawn.ur.x && mower.position.x + 1 >= 0
        case West  => mower.position.x - 1 <= lawn.ur.x && mower.position.x - 1 >= 0
      }
    case _ => true
  }
}
