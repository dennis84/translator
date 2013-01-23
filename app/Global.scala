import play.api._
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.codahale.jerkson.Json
import translator._
import translator.core._
import translator.core.Implicits._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    RegisterJodaTimeConversionHelpers()
    DataFixtures.refresh
    SearchFixtures.refresh
  }
}

object DataFixtures extends Fixtures {

  import translator.core.Implicits._

  def refresh = {
    UserDAO.collection.drop
    ProjectDAO.collection.drop
    TranslationDAO.collection.drop
    LanguageDAO.collection.drop

    UserDAO.insert(user1, user2)
    ProjectDAO.insert(project1, project2)
    TranslationDAO.insert(
      trans1en, trans1de, trans1fr, trans1es, trans1it, trans1de1, trans1pt1,
      trans2en, trans2de, trans2fr, trans2es,
      trans3en, trans3de, trans3fr,
      trans4en, trans4de, trans4fr)
    LanguageDAO.insert(language1, language2, language3, language4, language5,
      language6, language7, language8, language9)
  }
}

object SearchFixtures {

  def refresh = {
    Search.reset

    TranslationDAO.all map { trans =>
      Search.indexer.index("translator", "translation", trans.id, Json generate Map(
        "name" -> trans.name,
        "text" -> trans.text))
    }

    Search.indexer.refresh()
  }
}
