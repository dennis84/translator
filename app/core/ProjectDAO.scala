package translator.core

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoDB
import com.mongodb.casbah.Imports._

class ProjectDAO(mongodb: MongoDB)
  extends SalatDAO[DbProject, ObjectId](collection = mongodb("projects")) {

  def listByIds(ids: List[ObjectId]): List[Project] =
    find(MongoDBObject(
      "_id" -> MongoDBObject("$in" -> ids)
    )).toList map(makeProject(_))

  def byId(id: ObjectId) =
    findOneById(id) map(makeProject(_))

  def byToken(token: String) =
    findOne(MongoDBObject("token" -> token)) map(makeProject(_))

  def byName(name: String) =
    findOne(MongoDBObject("name" -> name)) map(makeProject(_))

  private def makeProject(p: DbProject) =
    Project(p.name, p.token, p.adminId, open = p.open, repo = p.repo, id = p.id)
}
