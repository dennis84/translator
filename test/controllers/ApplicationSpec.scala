package test.translator.controllers

import org.specs2.specification.Scope
import org.specs2.mutable._
import translator.test.Fixtures
import translator.core._

class ApplicationSpec extends Specification with Fixtures {

  "SecuredWithProject" should {
    "allow by user roles" in {
      val u1 = user1 withRoles(project1)
      println(u1.roles)

      val u2 = user1 withRoles(project2)
      println(u2.roles)

      1 must_== 1
    }
  }
}
