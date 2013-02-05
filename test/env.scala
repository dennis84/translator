package test.translator

import scala.concurrent._
import scala.concurrent.duration._
import translator.core._
import com.typesafe.config._
import play.api.test._
import play.api.test.Helpers._
import reactivemongo.bson.handlers.DefaultBSONHandlers
import language._

trait TestEnv {

  lazy val timeout = 10 seconds

  lazy val conf = ConfigFactory.load("application_test")

  lazy val env = new Env(
    FakeApplication(),
    new Conf(conf))

  implicit val ec = ExecutionContext.Implicits.global
  implicit val writer = DefaultBSONHandlers.DefaultBSONDocumentWriter
  implicit val handler = DefaultBSONHandlers.DefaultBSONReaderHandler
}
