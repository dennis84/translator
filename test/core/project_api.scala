package test.translator.core

import scala.concurrent._
import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.libs.json._
import test.translator._
import translator.core.User
import translator.core.Doc

class ProjectApiSpec extends Specification with Fixtures {

  sequential

  "The project api" should {
    "list mine" in new ProjectContext {
      (Await.result(env.projectApi.listMine(user1), timeout) \\ "name") map(
        _.as[String]) mustEqual List("acme", "foo")
    }

    "create" in new ProjectContext {
      val p = Await.result(env.projectApi.create("hello", user1), timeout)
      (p \ "name").as[String] mustEqual "hello"

      val id = (p \ "id").as[String]
      val u = Await.result(env.userRepo.byId(user1.id), timeout).get
      u.dbRoles.map(_.projectId) must contain(id)
    }

    "create fails" in new ProjectContext {
      val error = Await.result(env.projectApi.create("acme", user1), timeout)
      (error \ "error").as[String] mustEqual "project_name_taken"
    }

    "update" in new ProjectContext {
      val p = Await.result(env.projectApi.update(project1.id, "foo", true), timeout)
      (p \ "repo").as[String] mustEqual "foo"
      (p \ "open").as[Boolean] mustEqual true
    }

    "update fails" in new ProjectContext {
      val error = Await.result(env.projectApi.update(Doc.mkID, "foo", true), timeout)
      (error \ "error").as[String] mustEqual "project_not_found"
    }

    "signup" in new ProjectContext {
      val p = Await.result(env.projectApi.signup("bar", "dennis84", "demo"), timeout)
      (p \ "name").as[String] mustEqual "bar"

      Await.result(env.userRepo.byUsername("dennis84"), timeout) must beSome[User]
    }

    "signup fails" in new ProjectContext {
      val error1 = Await.result(env.projectApi.signup("acme", "d.dietrich84@gmail.com", "demo"),  timeout)
      (error1 \ "error").as[String] mustEqual "project_name_taken"

      val error2 = Await.result(env.projectApi.signup("bar", "d.dietrich84@gmail.com", "demo"),  timeout)
      (error2 \ "error").as[String] mustEqual "username_taken"
    }
  }

  trait ProjectContext extends Scope with TestEnv {
    try {
      Await.result(env.userRepo.collection.drop(), timeout)
      Await.result(env.userRepo.collection.create(), timeout)
      Await.result(env.projectRepo.collection.drop(), timeout)
      Await.result(env.projectRepo.collection.create(), timeout)
    } catch {
      case _: Throwable => println("Collections could not resetted.")
    }

    Await.result(env.userRepo.insert(user1, user2), timeout)
    Await.result(env.projectRepo.insert(project1, project2), timeout)
  }
}
