scalaVersion := "2.12.1"

name := "akka-actor-per-request"
organization := "com.github.nyukhalov"
version := "1.0"

resolvers ++= Seq(
  "Typesafe repository"       at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Snapshots"    at "https://oss.sonatype.org/content/repositories/snapshots"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka"           %% "akka-actor"             % "2.5.3",
  "com.typesafe.akka"           %% "akka-stream"            % "2.5.3",
  "com.typesafe.akka"           %% "akka-http"              % "10.0.9",
  "com.typesafe.akka"           %% "akka-http-spray-json"   % "10.0.9",
  "ch.qos.logback"              %  "logback-classic"        % "1.2.3",
  "com.typesafe.scala-logging"  %% "scala-logging"          % "3.7.2",

  "com.typesafe.akka" %% "akka-testkit" % "2.5.3" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.3" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test,
  "org.specs2" %% "specs2-core" % "3.9.1" % "test",
  "org.specs2" %% "specs2-mock" % "3.9.1" % "test"
)

exportJars := true
mainClass in Compile := Option("com.github.nyukhalov.akkaactorperrequest.Boot")

enablePlugins(JavaAppPackaging)
