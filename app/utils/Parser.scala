package translator.utils

import play.api.libs.json.Json

object Parser {

  def parse(content: String, ext: String): Map[String, String] = ext match {
    case "json" => json(content)
    case "po"   => po(content)
    case "yaml" => yaml(content)
    case "yml"  => yaml(content)
    case _ => Map.empty[String, String]
  }

  def json(content: String) = (Json parse content).as[Map[String, String]]

  def yaml(content: String) = Map.empty[String, String]

  def po(content: String) = {
    val regex = 
"""
msgid "(.*)"
msgstr "(.*)"
""".r

    (for (regex(id, str) <- regex findAllIn content) yield (id -> str)) toMap
  }
}
