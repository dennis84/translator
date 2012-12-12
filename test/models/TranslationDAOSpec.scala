package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator._
import translator.models._

class TranslationDAOSpec extends Specification with Fixtures {

  "Find all" should {
    "sorted" in new TranslationContext {
      var r = TranslationDAO.findAllByProjectAndName(project1, "hello_world")
      r.foreach(println(_))
      1 must_== 1
    }
  }

  trait TranslationContext extends Scope {
    TranslationDAO.collection.drop
    LanguageDAO.collection.drop

    TranslationDAO.insert(
      trans1en, trans1de, trans1fr, trans1es, trans1it, trans1de1, trans1pt1,
      trans2en, trans2de, trans2fr, trans2es,
      trans3en, trans3de, trans3fr,
      trans4en, trans4de, trans4fr)
    LanguageDAO.insert(language1, language2, language3, language4, language5, language6, language7, language8, language9)
  }
}
