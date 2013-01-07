package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object LanguageDAO
  extends SalatDAO[DbLanguage, ObjectId](collection = MongoConnection()("translator")("languages")) {

  def findAllByProject(project: Project) =
    find(MongoDBObject("projectId" -> project.id)).toList
      .map(makeLanguage(_).withProject(project))

  def findAllByProjectId(projectId: ObjectId) =
    find(MongoDBObject("projectId" -> projectId)).toList map(makeLanguage(_))

  def byId(id: ObjectId) =
    findOneById(id) map(makeLanguage(_))

  def findOneByProjectAndCode(project: Project, code: String) =
    findOne(MongoDBObject(
      "code" -> code,
      "projectId" -> project.id
    )) map(makeLanguage(_).withProject(project))

  def findFirstByProject(project: Project) =
    find(MongoDBObject("projectId" -> project.id)).limit(1).toList
      .map(makeLanguage(_).withProject(project))

  def makeLanguage(l: DbLanguage): Language =
    Language(l.code, l.name, l.projectId, id = l.id)
}
