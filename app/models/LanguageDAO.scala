package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object LanguageDAO
  extends SalatDAO[Language, ObjectId](collection = MongoConnection()("translator")("languages")) {

  def findAllByProject(project: Project) =
    find(MongoDBObject("projectId" -> project.id))
      .sort(orderBy = MongoDBObject("code" -> 1))
      .toList

  def findAllByProjectId(projectId: ObjectId) =
    find(MongoDBObject("projectId" -> projectId))
      .sort(orderBy = MongoDBObject("code" -> 1))
      .toList

  def findOneByCodeAndProject(code: String, project: Project) =
    findOne(MongoDBObject("code" -> code, "projectId" -> project.id))
}
