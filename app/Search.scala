package translator

import com.traackr.scalastic.elasticsearch._
import com.codahale.jerkson.Json

object Search {

  lazy val indexer = Indexer.transport(
    settings = Map("cluster.name" -> "elasticsearch"),
    host = "localhost",
    ports = Seq(9300))

  def reset = {
    indexer.deleteIndex(Seq("translator"))
    indexer.createIndex("translator", settings = Map())
    indexer.waitTillActive()
    indexer.putMapping("translator", "translation", Json generate Map("translation" -> Map(
      "properties" -> Map(
        "name" -> Map("type" -> "string"),
        "text" -> Map("type" -> "string"))
    )))

    indexer.refresh()
  }
}
