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

    "search" in new TransContext {
      val r = Await.result(env.transApi.search(project1, "hello"), timeout)
      r.as[List[JsValue]].length mustEqual 1L
    }

    "create" in new TransContext {
      val admin = user1.withRoles(project1)
      val r1 = Await.result(env.transApi.create(project1, admin, "en", "hello_world", "Hi"), timeout)
      (r1 \ "status").as[String] mustEqual "inactive"

      val r2 = Await.result(env.transApi.create(project1, admin, "en", "foobar", "Hi"), timeout)
      (r2 \ "status").as[String] mustEqual "active"

      val author = user2.withRoles(project1)
      val r3 = Await.result(env.transApi.create(project1, admin, "en", "foobar", "Hi"), timeout)
      (r3 \ "status").as[String] mustEqual "inactive"
    }

    "update" in new TransContext {
      val r = Await.result(env.transApi.update(trans1en.id, "foo"), timeout)
      (r \ "text").as[String] mustEqual "foo"
    }

    "switch" in new TransContext {
      val r1 = Await.result(env.transApi.switch(project1, trans1de1.id), timeout)
      (r1 \ "text").as[String] mustEqual "Moin Welt"

      val r2 = Await.result(env.transRepo.byNameAndCode(project1, "hello_world", "de"), timeout)
      r2.get.text mustEqual "Moin Welt"
    }

    "delete" in new TransContext {
      val r1 = Await.result(env.transApi.delete(project1, trans1de1.id), timeout)
      (r1 \ "text").as[String] mustEqual trans1de1.text

      val r2 = Await.result(env.transRepo.byId(trans1de1.id), timeout)
      r2 must beNone
    }

//     "inject" in new TransContext {
//       val json = Json.obj(
//         "foo" -> "Foo",
//         "bar" -> "Bar",
//         "baz" -> "Baz")

//       val admin = user1.withRoles(project1)
//       val r = Await.result(env.transApi.inject(project1, admin, Json.stringify(json), "json", "en"), timeout)
//       println(r)
//     }
  }
}
