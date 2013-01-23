package translator.test

import translator.core._
import com.typesafe.config._
import play.api.test._

trait TestEnv {

  lazy val conf = ConfigFactory.load("application_test")

  lazy val env = new Env(
    FakeApplication(),
    new Settings(conf))
}
