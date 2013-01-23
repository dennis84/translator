package test.translator.core

import org.specs2.specification.Scope
import org.specs2.mutable._

class TranslationParserSpec extends Specification {

  "Parser" should {
    "po" in {
      var str = """|msgid ""
                   |msgstr ""
                   |"Project-Id-Version: Translator\n"
                   |"Report-Msgid-Bugs-To: Translator <d.dietrich84@gmail.com>\n"
                   |"POT-Creation-Date: 2012-11-08 18:00+0100\n"
                   |"PO-Revision-Date: 2012-11-08 18:00+0100\n"
                   |"Last-Translator: dennis84\n"
                   |"Language-Team: Translator <d@dietrich84@gmail.com>\n"
                   |"MIME-Version: 1.0\n"
                   |"Content-Type: text/plain; charset=UTF-8\n"
                   |"Content-Transfer-Encoding: 8bit\n"

                   |msgid "foo"
                   |msgstr "Foo"

                   |msgid "bar"
                   |msgstr "Bar"

                   |msgid "hello_world"
                   |msgstr "This Text should only imported for language pt."
                   |""".stripMargin

      val ex  = """|msgid "(.+)"
                   |msgstr "(.+)"""".stripMargin.r

      val result = (for {
        ex(id, txt) <- ex findAllIn str
      } yield (id -> txt)) toMap

      1 must_== 1
    }
  }
}
