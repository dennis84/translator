package translator.core

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoDB
import com.mongodb.casbah.Imports._

class LanguageDAO(mongodb: MongoDB)
  extends SalatDAO[DbLanguage, ObjectId](collection = mongodb("languages")) {

  def list(project: Project) =
    find(MongoDBObject("projectId" -> project.id)).toList
      .map(makeLanguage(_).withProject(project))

  def byId(id: ObjectId) =
    findOneById(id) map(makeLanguage _)

  def byCode(project: Project, code: String) =
    findOne(MongoDBObject(
      "code" -> code,
      "projectId" -> project.id
    )) map(makeLanguage(_).withProject(project))

  def primary(project: Project) =
    find(MongoDBObject("projectId" -> project.id)).limit(1).toList
      .map(makeLanguage(_).withProject(project))
      .headOption

  def validateCode(project: Project, code: String): Option[String] = code match {
    case "" => primary(project) map(_.code)
    case _  => byCode(project, code).map(_.code)
  }

  private def makeLanguage(l: DbLanguage): Language =
    Language(l.code, l.name, l.projectId, id = l.id)
}
