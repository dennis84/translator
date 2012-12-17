package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object ProjectDAO
  extends SalatDAO[Project, ObjectId](collection = MongoConnection()("translator")("projects")) {

  def findAll = find(DBObject()) toList

  def findOneByToken(token: String) = findOne(MongoDBObject("token" -> token))

  def findOneByName(name: String) = findOne(MongoDBObject("name" -> name))
}
