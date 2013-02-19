package translator
package core

import play.api.Application
import reactivemongo.api._

class Env(app: Application, conf: Conf) {
  import conf._

  lazy val connection = MongoConnection(List(MongoHost + ":" + MongoPort))
  lazy val db = connection(MongoDbName)

  lazy val transRepo = new translator.trans.TransRepo(db("translations"))
  lazy val userRepo = new translator.user.UserRepo(db("users"))
  lazy val projectRepo = new translator.project.ProjectRepo(db("projects"))
  lazy val langRepo = new translator.lang.LangRepo(db("languages"))

  lazy val userForms = new translator.user.UserForms
  lazy val langForms = new translator.lang.LangForms
  lazy val projectForms = new translator.project.ProjectForms
  lazy val transForms = new translator.trans.TransForms
}

object Env {

  def apply(app: Application): Env = new Env(app,
    new Conf(app.configuration.underlying))
}
