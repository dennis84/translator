import play.api._
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.codahale.jerkson.Json
import translator._
import translator.models._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    RegisterJodaTimeConversionHelpers()
    DataFixtures.refresh
    SearchFixtures.refresh
  }
}

object DataFixtures extends Fixtures {

  def refresh = {
    UserDAO.collection.drop
    ProjectDAO.collection.drop
    EntryDAO.collection.drop
    LanguageDAO.collection.drop

    UserDAO.insert(user1, user2)
    ProjectDAO.insert(project1, project2)
    EntryDAO.insert(entry1, entry2, entry3, entry4)
    LanguageDAO.insert(language1, language2, language3, language4, language5, language6, language7, language8, language9)
  }
}

object SearchFixtures {

  def refresh = {
    EntryDAO.findAll map { entry =>
      Search.indexer.index("translator", "entry", entry.id, Json generate entry.toMap)
    }

    Search.indexer.refresh()
  }
}
