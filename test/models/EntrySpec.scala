package test.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator._
import translator.models._

class EntrySpec extends Specification with Fixtures {

  "Entry" should {
    "percentage" in new EntryContext {
      println(entry1.percentage)
      1 must_== 1
    }
  }

  trait EntryContext extends Scope {
    LanguageDAO.collection.drop
    EntryDAO.collection.drop

    LanguageDAO.insert(language1, language2, language3, language4, language5, language6)
    EntryDAO.insert(entry1)
  }
}
