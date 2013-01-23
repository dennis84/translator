package translator.test

import org.specs2.specification.Scope
import org.specs2.mutable._

class LoadFixturesSpec extends Specification {

  "Fixtures" should {
    "refresh database" in {
      DataFixtures.refresh
      1 must_== 1
    }

    "refresh search indices" in {
      SearchFixtures.refresh
      1 must_== 1
    }
  }
}
