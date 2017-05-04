name := "gatling-aerospike"

organization := "com.github.simsasg"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.gatling" % "gatling-core" % "2.2.5" % "provided",
  "com.aerospike" % "aerospike-client" % "3.3.4"
)

// Gatling contains scala-library
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)