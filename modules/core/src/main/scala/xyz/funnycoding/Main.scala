package xyz.funnycoding

import xyz.funnycoding.domain.parser
import xyz.funnycoding.solution._
import cats.effect._
import cats.implicits._
import xyz.funnycoding.modules.Solutions
import cats.data.Validated._
import xyz.funnycoding.algebras.LiveFileReader
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
