package test.translator.utils

import org.specs2.specification.Scope
import org.specs2.mutable._

class ParserSpec extends Specification {

  case class Msg(val id: String, val str: String)

  "Parser" should {
    "po" in {
      var po =
"""
msgid "foo"
msgstr "Foo"

msgid "bar"
msgstr "Bar"
""";

      val regex = 
"""
msgid "(.*)"
msgstr "(.*)"
""".r

      var r = for (regex(id, str) <- regex findAllIn po) yield Msg(id, str)

      r.foreach(println(_))

      1 must_== 1
    }
  }
}
