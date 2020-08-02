package xyz.funnycoding

import org.scalacheck.Arbitrary
import xyz.funnycoding.generators._

object arbitrairies {
  implicit val mowerDescriptionArb = Arbitrary(mowerDescriptionGen)
  implicit val initialProblemArb   = Arbitrary(initialProblemGen)
  implicit val orientationArb      = Arbitrary(orientationGen)
  implicit val mowerArb            = Arbitrary(mowerGen)
  implicit val collisionArb        = Arbitrary(collisionInitialProblemGen)
  implicit val nonCollisionArb     = Arbitrary(nonCollisionInitialProblemGen)
}
