val scala3Version = "3.4.0"

val AkkaVersion = "2.8.5"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val root = project
  .in(file("."))
  .settings(
    name := "ScalaMania",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.14" % Runtime,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test

  )
