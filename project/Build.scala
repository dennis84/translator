import sbt._
import Keys._
import Project._

trait Resolvers {
  val sonatype = "sonatype" at "http://oss.sonatype.org/content/repositories/releases"
  val sonatypeS = "sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots"
  val typesafe = "typesafe.com" at "http://repo.typesafe.com/typesafe/releases/"
  val codahale = "repo.codahale.com" at "http://repo.codahale.com/"
  val iliaz = "iliaz.com" at "http://scala.iliaz.com/"
}

trait Dependencies {
  val salat = "com.novus" %% "salat" % "1.9.1"
  val hasher = "com.roundeights" % "hasher" % "0.3" from "http://cloud.github.com/downloads/Nycto/Hasher/hasher_2.9.1-0.3.jar"
  val scalastic = "com.traackr" % "scalastic_2.9.2" % "0.0.6-HACKED"
  val scalaTime = "com.github.nscala-time" %% "nscala-time" % "0.2.0"
}

object ApplicationBuild extends Build with Resolvers with Dependencies {

  private val buildSettings = Project.defaultSettings ++ Seq(
    scalaVersion := "2.9.1",
    organization := "com.github.dennis84",
    version := "1.0",
    resolvers := Seq(iliaz, codahale, sonatype, sonatypeS, typesafe),
    scalacOptions := Seq("-deprecation", "-unchecked")
  )

  lazy val translator = PlayProject("translator", settings = buildSettings).settings(
    libraryDependencies ++= Seq(salat, hasher, scalastic, scalaTime)
  )
}
