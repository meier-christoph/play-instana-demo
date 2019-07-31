name := "play-instana-demo"
organization := "org.example"

scalaVersion := "2.11.8"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(BuildInfoPlugin)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-encoding", "UTF-8")

libraryDependencies ++= Seq(
  filters
)

// play
routesGenerator := InjectedRoutesGenerator
packageOptions in PlayKeys.playJarSansExternalized := (packageOptions in (Compile, packageBin)).value

// disable sources and doc
sources in (Compile, doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

// sbt-native-packager
mainClass in Compile := Some("org.example.Launcher")
topLevelDirectory in Universal := None

// sbt-buildinfo
buildInfoPackage := "utils"
buildInfoOptions += BuildInfoOption.ToJson
buildInfoOptions += BuildInfoOption.BuildTime
