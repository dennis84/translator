package translator.core

import play.api.libs.json.Json

object Parser {

  def parse(content: String, ext: String): List[(String, String)] =
    (ext match {
      case "json" ⇒ json(content)
      case "po"   ⇒ po(content)
      case "yaml" ⇒ yaml(content)
      case "yml"  ⇒ yaml(content)
      case _      ⇒ Map.empty[String, String]
    }) toList

  def json(content: String) = (Json parse content).as[Map[String, String]]

  def po(content: String) = {
    val regex = """|msgid "(.+)"
                   |msgstr "(.+)"""".stripMargin.r

    (for {
      regex(id, str) ← regex findAllIn content
    } yield (id -> str)) toMap
  }

  def yaml(content: String) = Map.empty[String, String]

  def xml(content: String) = Map.empty[String, String]
}
