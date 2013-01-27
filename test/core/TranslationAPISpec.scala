package test.translator.core

import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import translator.core._
import translator.test._

class TranslationAPISpec extends Specification with Fixtures {

  "The Translation API" should {
    "filter entries with no filter" in new TranslationContext {
      implicit val ctx = adminContext
      val trans = env.transAPI.entries(Filter(false, Nil, false))
      trans.length must_== 2L
    }

    "filter untranslated entries" in new TranslationContext {
      implicit val ctx = adminContext
      val trans1 = env.transAPI.entries(Filter(true, List("es"), false))
      trans1.length must_== 1L

      val trans2 = env.transAPI.entries(Filter(true, List("pt"), false))
      trans2.length must_== 2L
    }

    "filter activatable entries" in new TranslationContext {
      implicit val ctx = adminContext
      val trans = env.transAPI.entries(Filter(false, Nil, true))
      trans.length must_== 1L
    }

    "create in user1 context" in new TranslationContext {
      implicit val ctx = adminContext
      val t1 = env.transAPI.create("de", "test", "Test")
      t1.get.status must_== Status.Active
      
      val t2 = env.transAPI.create("de", "hello_world", "Foo")
      t2.get.status must_== Status.Inactive
    }

    "create in user2 context" in new TranslationContext {
      implicit val ctx = memberContext
      val t1 = env.transAPI.create("de", "test", "Test")
      t1.get.status must_== Status.Inactive

      val t2 = env.transAPI.create("de", "hello_world", "Foo")
      t2.get.status must_== Status.Inactive
    }

    "search" in new TranslationContext {
      val r = env.transAPI.search(project1, "hello")
      println(r)
    }
  }

  trait TranslationContext extends Scope with TestEnv {
    import translator.core.Implicits._

    env.projectDAO.collection.drop
    env.transDAO.collection.drop
    env.langDAO.collection.drop

    env.projectDAO.insert(project1, project2)

    env.transDAO.insert(
      trans1en, trans1de, trans1fr, trans1es, trans1it, trans1de1, trans1pt1,
      trans2en, trans2de, trans2fr, trans2es,
      trans3en, trans3de, trans3fr,
      trans4en, trans4de, trans4fr)

    env.langDAO.insert(language1, language2, language3, language4, language5,
      language6, language7, language8, language9)

    val adminContext = ProjectContext(
      FakeRequest(),
      user1.withRoles(project1),
      project1,
      List(project1, project2))

    val memberContext = ProjectContext(
      FakeRequest(),
      user2.withRoles(project1),
      project1,
      List(project1, project2))
  }
}
