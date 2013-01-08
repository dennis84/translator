package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object ProjectDAO
  extends SalatDAO[DbProject, ObjectId](collection = MongoConnection()("translator")("projects")) {

  def findAllByIds(ids: List[ObjectId]): List[Project] =
    find(MongoDBObject(
      "_id" -> MongoDBObject("$in" -> ids))).toList map(makeProject(_))

  def findOneByToken(token: String) =
    findOne(MongoDBObject("token" -> token)) map(makeProject(_))

  def findOneByName(name: String) =
    findOne(MongoDBObject("name" -> name)) map(makeProject(_))

  def makeProject(p: DbProject) =
    Project(p.name, p.token, p.adminId, id = p.id)
}
