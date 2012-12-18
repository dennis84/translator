package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator._
import translator.models._

class TranslationAPISpec extends Specification with Fixtures {

  "API" should {
    "untranslated" in new TranslationContext {
      var r = TranslationAPI.untranslated(project1)
      r.length must_== 2L
    }

    "untranslated by name" in new TranslationContext {
      var r = TranslationAPI.untranslated(project1, "hello_world")
      r.length must_== 1L
    }

    "statistics - progress" in new TranslationContext {
      var r = TranslationAPI.entries(project1, Filter("", Nil, ""))
      r.foreach(e => println(e("progress")))
    }
  }

  trait TranslationContext extends Scope {
    ProjectDAO.collection.drop
    TranslationDAO.collection.drop
    LanguageDAO.collection.drop

    ProjectDAO.insert(project1, project2)
    TranslationDAO.insert(
      trans1en, trans1de, trans1fr, trans1es, trans1it, trans1de1, trans1pt1,
      trans2en, trans2de, trans2fr, trans2es,
      trans3en, trans3de, trans3fr,
      trans4en, trans4de, trans4fr)
    LanguageDAO.insert(language1, language2, language3, language4, language5, language6, language7, language8, language9)
  }
}
