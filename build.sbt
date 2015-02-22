name := "Smart Process API - PlayApp"

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "npm" % "1.3.26",
  "org.webjars" % "requirejs" % "2.1.16",
  "org.webjars" % "jquery-ui" % "1.11.3",
  "org.webjars" % "bpmn-js" % "0.8.0"
)