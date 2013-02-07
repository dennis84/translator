package test.translator.trans

import scala.concurrent._
import org.specs2.mutable._
import play.api.libs.json._
import test.translator._

class ImporterSpec extends Specification with Fixtures {

  sequential

  "The import api" should {
    "import only translations" in new TransContext {
      val json = Json.stringify(Json.obj(
        "foo" -> "Foo",
        "bar" -> "Bar",
        "baz" -> "Baz"))
 
      val admin = user1.withRoles(project1)
      val r = Await.result(env.transApi.inject(project1, admin, json, "json", "en"), timeout)
    }

    "skip existing" in new TransContext {
      val json = Json.stringify(Json.obj(
        "hello_world" -> "Foo",
        "bye_bye" -> "Bar",
        "baz" -> "Baz"))
 
      val admin = user1.withRoles(project1)
      val r = Await.result(env.transApi.inject(project1, admin, json, "json", "en"), timeout)
      println(r)
    }
  }
}
