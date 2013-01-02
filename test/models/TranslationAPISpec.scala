package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator._
import translator.models._

class TranslationAPISpec extends Specification with Fixtures {

  "The Translation API" should {
    "filter entries with no filter" in new TranslationContext {
      TranslationAPI.entries(project, Filter("", Nil, ""))
      1 must_== 1
    }

    "filter untranslated entries" in new TranslationContext {
      TranslationAPI.entries(project, Filter("true", Nil, ""))
      1 must_== 1
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

    LanguageDAO.insert(language1, language2, language3, language4, language5,
      language6, language7, language8, language9)

    val project = Project(project1.name, project1.token, project1.adminId, id = project1.id)
  }
}
