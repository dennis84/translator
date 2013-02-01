package test.translator.core

import scala.concurrent._
import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.libs.json._
import test.translator._
import translator.core.Lang

class LangApiSpec extends Specification with Fixtures {

  sequential

  "LangApiSpec" should {
    "list" in new LangContext {
      (Await.result(env.langApi.list(project1), timeout) \\ "code") map(
        _.as[String]) mustEqual List("en", "de", "fr", "es", "it", "pt")
    }

    "create" in new LangContext {
      (Await.result(env.langApi.create(project1, "foo", "Bla"), timeout) \ "code").as[String] mustEqual "foo"
    }

    "update" in new LangContext {
      (Await.result(env.langApi.update(lang1.id, lang1.code, "test"), timeout) \ "name").as[String] mustEqual "test"
    }

    "delete" in new LangContext {
      (Await.result(env.langRepo.byId(lang1.id), timeout)) must beSome[Lang]
      (Await.result(env.langApi.delete(project1, lang1.id), timeout))
      (Await.result(env.langRepo.byId(lang1.id), timeout)) must beNone
    }
  }

  trait LangContext extends Scope with TestEnv {
    try {
      Await.result(env.projectRepo.collection.drop(), timeout)
      Await.result(env.langRepo.collection.drop(), timeout)
      Await.result(env.projectRepo.collection.create(), timeout)
      Await.result(env.langRepo.collection.create(), timeout)
    } catch {
      case _: Throwable => println("Collections could not resetted.")
    }

    val langs = List(lang1, lang2, lang3, lang4, lang5, lang6, lang7, lang8, lang9)

    Await.result(env.projectRepo.insert(project1, project2), timeout)
    Await.result(env.langRepo.insert(langs: _*), timeout)
  }
}
