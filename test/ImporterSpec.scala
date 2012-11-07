package test

import org.specs2.specification.Scope
import org.specs2.mutable._

class ImporterSpec extends Specification {

  "Importer" should {
    "test file extension" in {
      var file = new java.io.File("foo.json")
      1 must_== 1
    }
  }
}
