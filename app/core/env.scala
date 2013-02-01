package translator
package core

import play.api.Application
import reactivemongo.api._

class Env(app: Application, conf: Conf) {
  import conf._

  lazy val connection = MongoConnection(List("localhost:27017"))
  lazy val db = connection("translator")

  lazy val transRepo = new TransRepo(db("translations"))
  lazy val userRepo = new UserRepo(db("users"))
  lazy val projectRepo = new ProjectRepo(db("projects"))
  lazy val langRepo = new LangRepo(db("languages"))

  lazy val userApi = new UserApi(userRepo)
  lazy val projectApi = new ProjectApi(projectRepo, userRepo, transRepo, langRepo)
  lazy val transApi = new TransApi(transRepo, langRepo)
  lazy val langApi = new LangApi(langRepo)
}

object Env {

  def apply(app: Application): Env = new Env(app,
    new Conf(app.configuration.underlying))
}
