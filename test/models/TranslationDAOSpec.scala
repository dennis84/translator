package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator._
import translator.models._

class TranslationDAOSpec extends Specification with Fixtures {

  "Find all" should {
    "sorted by ids" in new TranslationContext {
      //val r = TranslationDAO.findAllByIds(List(trans1en.id, trans1de.id, trans1fr.id, trans1de1.id))
      println(entry1.translations)
      1 must_== 1
    }
  }

  trait TranslationContext extends Scope {
    EntryDAO.collection.drop
    TranslationDAO.collection.drop

    EntryDAO.insert(entry1)
    TranslationDAO.insert(trans1en, trans1de, trans1fr, trans1de1)
  }
}
