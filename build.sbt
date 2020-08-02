import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "xyz.funnycoding"
ThisBuild / organizationName := "funnycoding"

lazy val root = (project in file("."))
  .settings(
    name := "mower"
  )
  .aggregate(core, tests)

lazy val tests = (project in file("modules/tests"))
  .configs(IntegrationTest)
  .settings(
    name := "mower-test-suite",
    scalacOptions += "-Ymacro-annotations",
    scalafmtOnCompile := true,
    Defaults.itSettings,
    libraryDependencies ++= Seq(
          compilerPlugin(Libraries.kindProjector cross CrossVersion.full),
          compilerPlugin(Libraries.betterMonadicFor),
          Libraries.scalaCheck,
          Libraries.scalaTest,
          Libraries.scalaTestPlus
        )
  )
  .dependsOn(core)

lazy val core = (project in file("modules/core"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "mower-core",
    packageName in Docker := "mower",
    scalacOptions += "-Ymacro-annotations",
    scalafmtOnCompile := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    Defaults.itSettings,
    dockerBaseImage := "openjdk:8u201-jre-alpine3.9",
    makeBatScripts := Seq(),
    dockerUpdateLatest := true,
    libraryDependencies ++= Seq(
          compilerPlugin(Libraries.kindProjector cross CrossVersion.full),
          compilerPlugin(Libraries.betterMonadicFor),
          Libraries.cats,
          Libraries.catsEffect
        )
  )
