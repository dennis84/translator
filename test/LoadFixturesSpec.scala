package translator.test

import org.specs2.specification.Scope
import org.specs2.mutable._
import com.typesafe.config._
import com.codahale.jerkson.Json
import play.api.test._
import translator.core._
import translator.core.Implicits._

class LoadFixturesSpec extends Specification with Fixtures {

  "Fixtures" should {
    "refresh database" in new FixtureContext {
      // This spec is a workaround to execute a load fixture command.
      env.userDAO.collection.drop
      env.projectDAO.collection.drop
      env.transDAO.collection.drop
      env.langDAO.collection.drop

      env.userDAO.insert(user1, user2, user3)
      env.projectDAO.insert(project1, project2)
      env.transDAO.insert(
        trans1en, trans1de, trans1fr, trans1es, trans1it, trans1de1, trans1pt1,
        trans2en, trans2de, trans2fr, trans2es,
        trans3en, trans3de, trans3fr,
        trans4en, trans4de, trans4fr)
      env.langDAO.insert(language1, language2, language3, language4, language5,
        language6, language7, language8, language9)
    }
  }

  trait FixtureContext extends Scope {
    lazy val conf = ConfigFactory.load("application")

    lazy val env = new Env(
      FakeApplication(),
      new Settings(conf))
  }
}
