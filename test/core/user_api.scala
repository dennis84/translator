package test.translator.core

import scala.concurrent._
import org.specs2.specification.Scope
import org.specs2.mutable._
import play.api.libs.json._
import test.translator._

class UserApiSpec extends Specification with Fixtures {

  "The user api" should {
    "list contributors" in new UserContext {
      (Await.result(env.userApi.contributors(project1), timeout) \\ "username") map(
        _.as[String]) mustEqual List("d.dietrich84@gmail.com", "frank.drebin.1984@gmail.com")
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
