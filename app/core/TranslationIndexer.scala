package translator.core

import com.codahale.jerkson.Json
import com.traackr.scalastic.elasticsearch._

class TranslationIndexer(
  settings: Settings,
  transDAO: TranslationDAO) {

  import settings._

  lazy val indexer = Indexer.transport(
    settings = Map("cluster.name" -> ElasticsearchCluster),
    host = ElasticsearchHost,
    ports = Seq(ElasticsearchPort))

  def rebuild = {
    indexer.deleteIndex(Seq("translator"))
    indexer.createIndex("translator", settings = Map())
    indexer.waitTillActive()
    indexer.putMapping("translator", "translation", Json generate Map("translation" -> Map(
      "properties" -> Map(
        "name" -> Map("type" -> "string"),
        "text" -> Map("type" -> "string"))
    )))

    transDAO.all map { trans =>
      indexer.index("translator", "translation", trans.id.toString, Json generate Map(
        "name" -> trans.name,
        "text" -> trans.text))
    }

    indexer.refresh()
  }
}
