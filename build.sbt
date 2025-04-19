scalaVersion := "2.13.16"

name    := "alert-term-extraction"
version := "1.0"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalatest"                 %% "scalatest"       % "3.2.19" % "test",
  "com.softwaremill.sttp.client4" %% "core"            % "4.0.3",
  "com.github.pureconfig"         %% "pureconfig"      % "0.17.8",
  "com.github.pathikrit"          %% "better-files"    % "3.9.2",
  "com.typesafe.scala-logging"    %% "scala-logging"   % "3.9.5",
  "ch.qos.logback"                 % "logback-classic" % "1.3.5"
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % "0.14.12")
