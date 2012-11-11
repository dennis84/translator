package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator._
import translator.models._

class EntryDAOSpec extends Specification with Fixtures {

  "Find entries" should {
    "by project and filter" in new EntryContext {
      val r = EntryDAO.findAllByProjectAndFilter(project1, Filter("true", Seq("pt")))
      r.foreach(println(_))
      1 must_== 1
    }
  }

  trait EntryContext extends Scope {
    ProjectDAO.collection.drop
    LanguageDAO.collection.drop
    EntryDAO.collection.drop

    ProjectDAO.insert(project1, project2)
    LanguageDAO.insert(language1, language2, language3, language4, language5, language6)
    EntryDAO.insert(entry1, entry2)
  }
}
