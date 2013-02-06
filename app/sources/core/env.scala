package translator
package core

import play.api.Application
import reactivemongo.api._

class Env(app: Application, conf: Conf) {
  import conf._

  lazy val connection = MongoConnection(List("localhost:27017"))
  lazy val db = connection("translator")

  lazy val transRepo = new translator.trans.TransRepo(db("translations"))
  lazy val userRepo = new translator.user.UserRepo(db("users"))
  lazy val projectRepo = new translator.project.ProjectRepo(db("projects"))
  lazy val langRepo = new translator.lang.LangRepo(db("languages"))

  lazy val userApi = new translator.user.UserApi(userRepo)
  lazy val projectApi = new translator.project.ProjectApi(projectRepo, userRepo, transRepo, langRepo)
  lazy val transApi = new translator.trans.TransApi(transRepo, langRepo)
  lazy val langApi = new translator.lang.LangApi(langRepo)
}

object Env {

  def apply(app: Application): Env = new Env(app,
    new Conf(app.configuration.underlying))
}
