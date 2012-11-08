package test.utils

import org.specs2.specification.Scope
import org.specs2.mutable._
import java.io.File
import play.api.libs.Files._

class ImporterSpec extends Specification {

  "Importer" should {
    "test" in {
      var tmp = TemporaryFile(new File("/tmp/foo"))
      var mvd = tmp.moveTo(new File("/tmp/bar"))
      println(mvd)

      1 must_== 1
    }
  }
}
