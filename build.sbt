name := "play-instana-demo"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  ws,
  filters,
  cache,
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",
  "com.instana" % "instana-java-opentracing" % "0.30.0"
)

// play
routesGenerator := InjectedRoutesGenerator

// disable sources and doc
sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

// sbt-native-packager
mainClass in Compile := Some("utils.Launcher")
topLevelDirectory in Universal := None
