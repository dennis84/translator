package translator.core

import com.typesafe.config.Config

class Settings(config: Config) {
  import config._

  val MongoHost = getString("mongo.host")
  val MongoPort = getInt("mongo.port")
  val MongoDbName = getString("mongo.db_name")
  val MongoUser = getString("mongo.user")
  val MongoPassword = getString("mongo.password")
}
