import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName    = "translator"
  val appVersion = "1.0-SNAPSHOT"

  val appResolvers = Seq(
    "Sonatype" at "http://oss.sonatype.org/content/repositories/releases/",
    "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/")

  val appDependencies = Seq(
    "com.github.nscala-time" %% "nscala-time" % "0.2.0",
    "org.reactivemongo" %% "reactivemongo" % "0.9-SNAPSHOT",
    "com.roundeights" % "hasher" % "0.3")

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= appResolvers,
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature"))
}
