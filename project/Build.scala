import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName    = "translator"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.github.nscala-time" %% "nscala-time" % "0.2.0",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.8",
    "com.roundeights" % "hasher" % "0.3")

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature"))
}
