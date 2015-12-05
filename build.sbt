name := "gcli-scala"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.3.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)

resolvers += Resolver.sonatypeRepo("public")

