package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator._
import translator.models._

class Spec extends Specification with Fixtures {

  "API" should {
    "list" in new LanguageContext {
      val r = Collection[Language, LanguageItem](
        LanguageDAO.findAllByProject(project1))

      println(r.models)

      1 must_== 1
    }
  }

  trait LanguageContext extends Scope {
    ProjectDAO.collection.drop
    LanguageDAO.collection.drop

    ProjectDAO.insert(project1, project2)
    LanguageDAO.insert(
      language1, language2, language3, language4, language5, language6,
      language7, language8, language9)
  }
}
