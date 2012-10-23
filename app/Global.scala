import play.api._
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import translator._
import translator.models._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    RegisterJodaTimeConversionHelpers()
    DataFixtures.insert
  }
}

object DataFixtures extends Fixtures {

  def insert = {
    UserDAO.collection.drop
    ProjectDAO.collection.drop
    EntryDAO.collection.drop
    LanguageDAO.collection.drop

    UserDAO.insert(user1, user2)
    ProjectDAO.insert(project1)
    EntryDAO.insert(entry1, entry2)
    LanguageDAO.insert(language1, language2, language3, language4, language5, language6)
  }
}
