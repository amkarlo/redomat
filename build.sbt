name := """redomat"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  "com.google.code.gson" % "gson" % "2.2.3",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "javax.persistence" % "persistence-api" % "1.0",
  "org.mindrot" % "jbcrypt" % "0.3m",
  cache,
  javaWs
)

fork in run := true


fork in run := true

fork in run := true

fork in run := true

fork in run := true