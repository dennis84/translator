package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object TranslationDAO
  extends SalatDAO[Translation, ObjectId](collection = MongoConnection()("translator")("translations")) {

  def findAll = find(MongoDBObject()) toList

  def findAllByProject(project: Project) =
    find(MongoDBObject("projectId" -> project.id)).toList

  def findAllByProjectAndCode(project: Project, code: String) =
    find(MongoDBObject("projectId" -> project.id, "code" -> code)) toList

  def findAllByProjectAndName(project: Project, name: String) =
    find(MongoDBObject("projectId" -> project.id, "name" -> name)).toList

  def findActivatedByProjectAndCode(project: Project, code: String) =
    find(MongoDBObject("projectId" -> project.id, "code" -> code, "status.code" -> Status.Active.id)) toList

  def findAllByProjectAndIds(project: Project, ids: List[ObjectId]) =
    find(MongoDBObject("projectId" -> project.id, "_id" -> MongoDBObject("$in" -> ids))) toList

  def findOneByProjectNameAndCode(project: Project, name: String, code: String) =
    findOne(MongoDBObject("projectId" -> project.id, "name" -> name, "code" -> code))

  def removeAllByProjectAndName(project: Project, name: String) =
    find(MongoDBObject("projectId" -> project.id, "name" -> name)).toList foreach (remove(_))
}
