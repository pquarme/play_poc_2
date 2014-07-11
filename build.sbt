name := """sito2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.google.code.gson" % "gson" % "2.2.2",
  "mysql" % "mysql-connector-java" % "5.1.28"
)
