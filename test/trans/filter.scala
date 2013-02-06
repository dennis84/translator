package test.translator.trans

import scala.concurrent._
import org.specs2.mutable._
import play.api.libs.json._
import test.translator._
import translator.trans.Filter

class FilterApiSpec extends Specification with Fixtures {

  sequential

  "The trans api entry filter" should {
    "list all entries" in new TransContext {
      val r = Await.result(env.transApi.entries(project1, Filter(false, Nil, false)), timeout)
      r.as[List[JsValue]].length must_== 2L
    }

    "list all untranslated entries by langs: [de]" in new TransContext {
      val r = Await.result(env.transApi.entries(project1, Filter(true, List("de", "en"), false)), timeout)
      r.as[List[JsValue]].length must_== 0L
    }

    "list all untranslated entries by langs: [pt]" in new TransContext {
      val r = Await.result(env.transApi.entries(project1, Filter(true, List("pt"), false)), timeout)
      r.as[List[JsValue]].length must_== 2L
    }

    "list all untranslated entries by langs: [it,es]" in new TransContext {
      val r = Await.result(env.transApi.entries(project1, Filter(true, List("it"), false)), timeout)
      r.as[List[JsValue]].length must_== 1L
    }

    "list all activatable" in new TransContext {
      val r = Await.result(env.transApi.entries(project1, Filter(false, Nil, true)), timeout)
      r.as[List[JsValue]].length must_== 1L
    }
  }
}
