package translator.core

import play.api.Application
import com.mongodb.casbah.MongoConnection
import com.mongodb.ServerAddress
import com.traackr.scalastic.elasticsearch._

class Environment(app: Application, settings: Settings) {
  import settings._

  // Initialize the mongodb database connection.
  lazy val mongodb = MongoConnection(
    new ServerAddress(MongoHost, MongoPort))(MongoDbName)

  lazy val langDAO = new LanguageDAO(mongodb)
  lazy val transDAO = new TranslationDAO(mongodb)
  lazy val userDAO = new UserDAO(mongodb)
  lazy val projectDAO = new ProjectDAO(mongodb)

  // Initialize the elasticsearch indexer.
  lazy val search = new TranslationIndexer(settings, transDAO)

  lazy val langAPI = new LanguageAPI(langDAO)
  lazy val transAPI = new TranslationAPI(transDAO, langDAO, search)
  lazy val userAPI = new UserAPI(userDAO)
  lazy val projectAPI = new ProjectAPI(projectDAO, userDAO, transDAO, langDAO)

  lazy val forms = new DataForm(userDAO, projectDAO, langDAO)
}

object Environment {

  def apply(app: Application): Environment = new Environment(app,
    new Settings(app.configuration.underlying))
}
