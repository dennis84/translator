package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import translator._
import translator.models._
import translator.controllers.{ Context, ProjectContext }

class TranslationAPISpec extends Specification with Fixtures {

  "The Translation API" should {
    "filter entries with no filter" in new TranslationContext {
      implicit val ctx = context
      val trans = TranslationAPI.entries(Filter("", Nil, ""))
      trans.length must_== 2L
    }

    "filter untranslated entries" in new TranslationContext {
      implicit val ctx = context
      val trans = TranslationAPI.entries(Filter("true", List("pt"), ""))
      trans.length must_== 1L
      trans(0).name must_== "bye_bye"
    }

    "filter activatable entries" in new TranslationContext {
      implicit val ctx = context
      val trans = TranslationAPI.entries(Filter("", Nil, "true"))
      trans.length must_== 1L
      trans.foreach(t => println(t.toString))
    }
  }

  trait TranslationContext extends Scope {
    import translator.models.Implicits._

    ProjectDAO.collection.drop
    TranslationDAO.collection.drop
    LanguageDAO.collection.drop

    ProjectDAO.insert(project1, project2)

    TranslationDAO.insert(
      trans1en, trans1de, trans1fr, trans1es, trans1it, trans1de1, trans1pt1,
      trans2en, trans2de, trans2fr, trans2es,
      trans3en, trans3de, trans3fr,
      trans4en, trans4de, trans4fr)

    LanguageDAO.insert(language1, language2, language3, language4, language5,
      language6, language7, language8, language9)

    val context = ProjectContext(
      FakeRequest(),
      user1,
      project1,
      List(project1, project2))
  }
}
