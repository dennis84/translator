package test.translator.models

import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import translator._
import translator.models._
import translator.models.Implicits._

class TranslationCollectionSpec extends Specification with Fixtures {

  "translation collection" should {
    "filter activatable" in new TranslationContext {
      val trans = TranslationDAO.listByName(project1, "bye_bye")
      val r = trans.filterActivatable
      println(r)
    }

    /* "filter must activated" in new TranslationContext { */
    /*   val trans = TranslationDAO.listByName(project1, "bye_bye") */
    /*   val r = trans.filterMustActivated */
    /*   println(r) */
    /* } */
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
