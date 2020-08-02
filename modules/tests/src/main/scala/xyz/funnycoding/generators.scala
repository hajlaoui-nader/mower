package xyz.funnycoding

import org.scalacheck.Gen
import xyz.funnycoding.domain.data._
import xyz.funnycoding.domain.parser.InitialProblem
import xyz.funnycoding.testdata.data._

object generators {
  val positionGen: Gen[Position] = for {
    x <- Gen.choose[Int](0, 100)
    y <- Gen.choose[Int](0, 100)
  } yield Position(x, y)

  val orientationGen: Gen[Orientation] = Gen.oneOf(North, South, East, West)

  val mowerGen: Gen[Mower] = for {
    p <- positionGen
    o <- orientationGen
  } yield Mower(p, o)

  val commandsGen: Gen[Command] = Gen.oneOf(Left, Right, Forward)

  val mowerDescriptionGen: Gen[MowerDescription] = for {
    m <- mowerGen
    lc <- Gen.nonEmptyListOf(commandsGen)
  } yield MowerDescription(m, lc)

  val initialProblemGen: Gen[InitialProblem] = for {
    ur <- positionGen
    md <- Gen.nonEmptyListOf(mowerDescriptionGen)
  } yield InitialProblem(ur, md)

  val collisionInitialProblemGen: Gen[CollisionInitialProblem] = for {
    ur <- collisionPosition
    md <- collisionMowerDescriptionGen(ur)
  } yield CollisionInitialProblem(InitialProblem(ur, md))

  val nonCollisionInitialProblemGen: Gen[NonCollisionInitialProblem] = for {
    ur <- collisionPosition
    md <- nonCollisionMowerDescriptionGen(ur)
  } yield NonCollisionInitialProblem(InitialProblem(ur, md))

  private def collisionPosition: Gen[Position] =
    for {
      x <- Gen.choose[Int](10, 100)
      y <- Gen.choose[Int](10, 100)
    } yield Position(x, y)

  private def collisionMowerDescriptionGen(position: Position): Gen[List[MowerDescription]] =
    for {
      m <- collisionMowerGen(position)
      lc <- Gen.oneOf(List(List(Forward)))
      m2 = collision(m)
    } yield List(MowerDescription(m, lc), MowerDescription(m2, Nil))

  private def nonCollisionMowerDescriptionGen(position: Position): Gen[List[MowerDescription]] =
    for {
      m <- collisionMowerGen(position)
      lc <- Gen.oneOf(List(List(Forward)))
      m2 = nonCollision(m)
    } yield List(MowerDescription(m, lc), MowerDescription(m2, Nil))

  private def collisionMowerGen(position: Position): Gen[Mower] =
    for {
      p <- mowerCollisionPosition(position)
      o <- Gen.oneOf(List(North))
    } yield Mower(p, o)

  private def mowerCollisionPosition(position: Position): Gen[Position] =
    for {
      x <- Gen.choose[Int](0, position.x - 1)
      y <- Gen.choose[Int](0, position.y - 1)
    } yield Position(x, y)

  private def collision(mower: Mower): Mower =
    Mower(mower.position.copy(x = mower.position.x, y = mower.position.y + 1), North)

  private def nonCollision(mower: Mower): Mower =
    Mower(mower.position.copy(x = mower.position.x + 1, y = mower.position.y), North)
}
