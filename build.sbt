packAutoSettings

name := "stud"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "ws.unfiltered" % "unfiltered-filter_2.12" % "0.9.0-beta2",
  "ws.unfiltered" % "unfiltered-netty-server_2.12" % "0.9.0-beta2",
  "org.json4s" %% "json4s-jackson" % "3.5.0",
  "com.lihaoyi" %% "scalatags" % "0.6.2"
)