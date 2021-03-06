import AssemblyKeys._

seq(assemblySettings: _*)

name := "scala-storm-starter"

version := "0.0.2-SNAPSHOT"

scalaVersion := "2.9.2"

scalacOptions += "-Yresolve-term-conflict:package"

fork in run := true

resolvers ++= Seq(
  "twitter4j" at "http://twitter4j.org/maven2",
  "clojars.org" at "http://clojars.org/repo"
)

libraryDependencies ++= Seq(
//  "storm" % "storm" % "0.8.2" % "provided",
  "com.github.velvia" % "scala-storm_2.9.1" % "0.2.2",
  "org.clojure" % "clojure" % "1.4.0" % "provided",
  "org.twitter4j" % "twitter4j-core" % "2.2.6-SNAPSHOT",
  "org.twitter4j" % "twitter4j-stream" % "2.2.6-SNAPSHOT",
  "org.specs2" %% "specs2" % "1.11" % "test"
)

//mainClass in Compile := Some("storm.starter.topology.ExclamationTopology")

mainClass in Compile := Some("storm.starter.topology.DrpcClient")

//mainClass in Compile := Some("storm.starter.topology.Trident")

//mainClass in Compile := Some("storm.starter.topology.Drpc")

//mainClass in assembly := Some("storm.starter.topology.ExclamationTopology")

//mainClass in assembly := Some("storm.starter.topology.Drpc")

mainClass in assembly := Some("storm.starter.topology.Trident")

TaskKey[File]("generate-storm") <<= (baseDirectory, fullClasspath in Compile, mainClass in Compile) map { (base, cp, main) =>
  val template = """#!/bin/sh
java -classpath "%s" %s "$@"
"""
  val mainStr = main getOrElse error("No main class specified")
  val contents = template.format(cp.files.absString, mainStr)
  val out = base / "bin/run-main-topology.sh"
  IO.write(out, contents)
  out.setExecutable(true)
  out
}
