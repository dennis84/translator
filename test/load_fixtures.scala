package test.translator

import scala.concurrent._
import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.libs.json._
import translator.core._

class LoadFixturesSpec extends Specification with Fixtures {
  
  "fixtures" should {
    "load" in  new FixtureContext {
      // users
      Await.result(env.userRepo.collection.drop(), timeout)
      Await.result(env.userRepo.collection.create(), timeout)
      Await.result(env.userRepo.insert(user1, user2), timeout)

      // projetcs
      Await.result(env.projectRepo.collection.drop(), timeout)
      Await.result(env.projectRepo.collection.create(), timeout)
      Await.result(env.projectRepo.insert(project1, project2), timeout)

      // languages
      Await.result(env.langRepo.collection.drop(), timeout)
      Await.result(env.langRepo.collection.create(), timeout)
      val langs = List(lang1, lang2, lang3, lang4, lang5, lang6, lang7, lang8, lang9)
      Await.result(env.langRepo.insert(langs: _*), timeout)

      // translations
      Await.result(env.transRepo.collection.drop(), timeout)
      Await.result(env.transRepo.collection.create(), timeout)
      val translations = List(
        trans1en, trans1de, trans1de1, trans1fr, trans1es, trans1it, trans1pt1,
        trans2en, trans2de, trans2fr, trans2es,
        trans3en, trans3de, trans3fr,
        trans4en, trans4de, trans4fr)

      Await.result(env.transRepo.insert(translations: _*), timeout)
    }
  }

  trait FixtureContext extends Scope with TestEnv
}
