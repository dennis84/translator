package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection

object UserDAO
  extends SalatDAO[User, ObjectId](collection = MongoConnection()("translator")("users")) {

  def findAll = find(DBObject()).toList

  def findOneByUsername(username: String) = findOne(MongoDBObject("username" -> username))

  def findOneByUsernameAndPassword(username: String, password: String): Option[User] =
    findOne(MongoDBObject("username" -> username, "password" -> password))
}
