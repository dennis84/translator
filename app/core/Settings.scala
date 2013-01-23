package translator.core

import com.typesafe.config.Config

class Settings(config: Config) {
  import config._

  val MongoHost = getString("mongo.host")
  val MongoPort = getInt("mongo.port")
  val MongoDbName = getString("mongo.db_name")

  val ElasticsearchHost = getString("elasticsearch.host")
  val ElasticsearchPort = getInt("elasticsearch.port")
  val ElasticsearchCluster = getString("elasticsearch.cluster")
}
