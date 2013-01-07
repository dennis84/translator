package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.map_reduce.MapReduceInlineOutput

object TranslationDAO
  extends SalatDAO[DbTranslation, ObjectId](collection = MongoConnection()("translator")("translations")) {

  def findAll: List[Translation] =
    find(MongoDBObject()).toList map(makeTranslation(_))

  def findAllByProject(project: Project): List[Translation] =
    find(MongoDBObject("projectId" -> project.id)).toList
      .map(makeTranslation(_))

  def findAllByProjectAndCode(project: Project, code: String): List[Translation] =
    find(MongoDBObject(
      "projectId" -> project.id,
      "code" -> code)).toList map(makeTranslation(_))

  def findAllByProjectAndName(project: Project, name: String): List[Translation] =
    find(MongoDBObject(
      "projectId" -> project.id,
      "name" -> name)).toList map(makeTranslation(_))

  def findActivatedByProjectAndCode(project: Project, code: String): List[Translation] =
    find(MongoDBObject(
      "projectId" -> project.id,
      "code" -> code,
      "status.code" -> Status.Active.id)).toList map(makeTranslation(_))

  def findAllByProjectAndIds(project: Project, ids: List[ObjectId]): List[Translation] =
    find(MongoDBObject(
      "projectId" -> project.id,
      "_id" -> MongoDBObject("$in" -> ids))).toList map(makeTranslation(_))

  def byId(id: ObjectId): Option[Translation] =
    findOneById(id) map(makeTranslation(_))

  def findOneByProjectNameAndCode(project: Project, name: String, code: String): Option[Translation] =
    findOne(MongoDBObject(
      "projectId" -> project.id,
      "name" -> name,
      "code" -> code)) map(makeTranslation(_))

  def removeAllByProjectAndName(project: Project, name: String) =
    find(MongoDBObject(
      "projectId" -> project.id,
      "name" -> name
    )).toList foreach(remove(_))

  private def makeTranslation(t: DbTranslation) =
    Translation(t.code, t.name, t.text, t.author, t.status, t.projectId, id = t.id)
}
