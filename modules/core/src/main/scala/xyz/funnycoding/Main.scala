package xyz.funnycoding

import cats.effect._
import cats.implicits._
import xyz.funnycoding.domain._
import xyz.funnycoding.effect._
import xyz.funnycoding.modules._
import cats.data.Validated._
import xyz.funnycoding.algebras._
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors

object Main extends IOApp {
  val path = "input.txt"

  val solverThreadPool =
    ExecutionContext.fromExecutor(Executors.newCachedThreadPool(new NamedThreadFactory("solverTP", true)))

  override def run(args: List[String]): IO[ExitCode] =
    for {
      fileReader <- LiveFileReader.make[IO]
      ln <- fileReader.read(path)
      parseResult <- parser.InitialProblem.raw(ln).pure[IO]
      _ <- parseResult match {
            case Invalid(e) => IO.raiseError(new IllegalArgumentException(e.toList.mkString))
            case Valid(initialProblem) =>
              for {
                solver <- Solutions.make[IO](initialProblem, IO.contextShift(solverThreadPool))
                solution <- solver.solve
                _ <- IO { println(solution) }
              } yield ()
          }
    } yield ExitCode.Success

}
