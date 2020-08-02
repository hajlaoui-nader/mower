package xyz.funnycoding.domain

object data {
  sealed trait Orientation
  case object North extends Orientation
  case object South extends Orientation
  case object East extends Orientation
  case object West extends Orientation

  object Orientation {
    def left(o: Orientation): Orientation =
      o match {
        case North => West
        case South => East
        case East  => North
        case West  => South
      }
    def right(o: Orientation): Orientation =
      o match {
        case North => East
        case South => West
        case East  => South
        case West  => North
      }
  }

  sealed trait Command
  case object Forward extends Command
  case object Right extends Command
  case object Left extends Command

  case class Position(x: Int, y: Int)

  case class Mower(position: Position, orientation: Orientation)

  object Mower {
    def forward(mower: Mower): Mower =
      mower.orientation match {
        case North => mower.copy(position = Position(mower.position.x, mower.position.y + 1))
        case South => mower.copy(position = Position(mower.position.x, mower.position.y - 1))
        case East  => mower.copy(position = Position(mower.position.x + 1, mower.position.y))
        case West  => mower.copy(position = Position(mower.position.x - 1, mower.position.y))
      }
  }

  case class MowerDescription(mower: Mower, commands: List[Command])

  case class Lawn(ur: Position, occupied: List[Mower])
}
