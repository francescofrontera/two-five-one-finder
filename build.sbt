ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "two-five-one-progression"

version := "0.1-SNAPSHOT"

organization := "io.github.francescofrontera"

ThisBuild / scalaVersion := "2.12.8"

val flinkVersion = "1.9.1"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala"           % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided"
)

lazy val root = (project in file(".")).settings(libraryDependencies ++= flinkDependencies)

assembly / mainClass := Some("io.github.francescofrontera.Job")
//Formatter
scalafmtOnCompile := true

// make run command include the provided dependencies
Compile / run := Defaults.runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false)
