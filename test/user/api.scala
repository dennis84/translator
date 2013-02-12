package test.translator.user

import scala.concurrent._
import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.libs.json._
import test.translator._
import translator.core._
import translator.user.Role
import translator.user.User

class UserApiSpec extends Specification with Fixtures {

  sequential

  "The user api" should {
    "list contributors" in new UserContext {
      val users = Await.result(env.userApi.contributors(project1), timeout)
      users mustEqual List(user1, user2)
    }

    "authenticate" in new UserContext {
      val user = Await.result(env.userApi.authenticate("d.dietrich84@gmail.com", "demo"), timeout)
      user must beSome(user1)
    }

    "create" in new UserContext {
      val user = Await.result(env.userApi.create(project1, "foo", "demo", Nil), timeout)
      user must beSome[User]

      val error = Await.result(env.userApi.create(project1, "d.dietrich84@gmail.com", "demo", Nil), timeout)
      error must beNone
    }

    "update password" in new UserContext {
      val user = Await.result(env.userApi.updatePassword(user1.id, "foobar"), timeout)
      (user \ "username").as[String] mustEqual user1.username

      val error = Await.result(env.userApi.updatePassword(Doc.mkID, "demo"), timeout)
      (error \ "error").as[String] mustEqual "user_not_found"
    }

    "update roles" in new UserContext {
      val user = Await.result(env.userApi.updateRoles(project1, user1.id, List(Role.ADMIN)), timeout)
      (user \ "username").as[String] mustEqual user1.username
    }

    "usernames like" in new UserContext {
      val usernames = Await.result(env.userApi.usernamesLike("d.die"), timeout)
      usernames.as[List[String]] mustEqual List(user1.username)
    }
  }

  trait UserContext extends Scope with TestEnv {
    try {
      Await.result(env.userRepo.collection.drop(), timeout)
      Await.result(env.userRepo.collection.create(), timeout)
    } catch {
      case _: Throwable => println("Collections could not resetted.")
    }

    Await.result(env.userRepo.insert(user1, user2), timeout)
  }
}
