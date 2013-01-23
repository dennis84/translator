package translator.core

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.map_reduce.MapReduceInlineOutput

object TranslationDAO
  extends SalatDAO[DbTranslation, ObjectId](collection = MongoConnection()("translator")("translations")) {

  def all: List[Translation] =
    find(MongoDBObject()).toList map(makeTranslation(_))

  def list(project: Project): List[Translation] =
    find(MongoDBObject("projectId" -> project.id)).toList
      .map(makeTranslation(_) withProject(project))

  def listByName(project: Project, name: String): List[Translation] =
    find(MongoDBObject(
      "projectId" -> project.id,
      "name" -> name
    )).toList map {
      makeTranslation(_) withProject(project)
    }

  def listByIds(project: Project, ids: List[ObjectId]): List[Translation] =
    find(MongoDBObject(
      "projectId" -> project.id,
      "_id" -> MongoDBObject("$in" -> ids)
    )).toList map {
      makeTranslation(_) withProject(project)
    }

  def listActive(project: Project, code: String): List[Translation] =
    find(MongoDBObject(
      "projectId" -> project.id,
      "code" -> code,
      "status" -> Status.Active.id
    )).toList map {
      makeTranslation(_) withProject(project)
    }

  def byId(id: ObjectId): Option[Translation] =
    findOneById(id) map(makeTranslation(_))

  def byName(p: Project, name: String) =
    findOne(MongoDBObject(
      "name" -> name,
      "projectId" -> p.id
    )) map {
      makeTranslation(_) withProject(p)
    }

  def byNameAndCode(p: Project, name: String, code: String) =
    findOne(MongoDBObject(
      "name" -> name,
      "code" -> code,
      "projectId" -> p.id
    )) map {
      makeTranslation(_) withProject(p)
    }

  def activated(p: Project, name: String, code: String): Option[Translation] =
    findOne(MongoDBObject(
      "projectId" -> p.id,
      "name" -> name,
      "code" -> code,
      "status" -> Status.Active.id
    )) map {
      makeTranslation(_) withProject(p)
    }

  def removeEntry(project: Project, name: String) =
    find(MongoDBObject(
      "projectId" -> project.id,
      "name" -> name
    )).toList foreach(remove(_))

  private def makeTranslation(t: DbTranslation) =
    Translation(t.code, t.name, t.text, t.author, Status(t.status), t.projectId, id = t.id)
}