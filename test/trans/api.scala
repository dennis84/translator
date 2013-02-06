package test.translator.trans

import scala.concurrent._
import org.specs2.mutable._
import play.api.libs.json._
import test.translator._

class TransApiSpec extends Specification with Fixtures {

  sequential

  "The trans api" should {
    "entry" in new TransContext {
      val r = Await.result(env.transApi.entry(project1, trans1en.id), timeout)
      (r \ "name").as[String] mustEqual "hello_world"
    }

    "export" in new TransContext {
      val r = Await.result(env.transApi.export(project1, "en"), timeout)
      r.as[List[JsValue]].length mustEqual 2L
    }

    "list" in new TransContext {
      val r1 = Await.result(env.transApi.list(project1, "hello_world"), timeout)
      r1.as[List[JsValue]].length mustEqual 8L

      val r2 = Await.result(env.transApi.list(project1, "bye_bye"), timeout)
      r2.as[List[JsValue]].length mustEqual 6L
    }
  }
}
