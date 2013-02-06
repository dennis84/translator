package translator
package project

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers._
import reactivemongo.core.commands.LastError
import play.api.libs.iteratee.Enumerator
import language._
import translator.core._

class ProjectRepo(val collection: DefaultCollection) {

  implicit object ProjectBSONReader extends BSONReader[Project] {
    def fromBSON(document: BSONDocument): Project = {
      val doc = document.toTraversable
      Project(
        id = doc.getAs[BSONObjectID]("_id").get.stringify,
        name = doc.getAs[BSONString]("name").get.value,
        token = doc.getAs[BSONString]("token").get.value,
        adminId = doc.getAs[BSONObjectID]("adminId").get.stringify,
        open = doc.getAs[BSONBoolean]("open").get.value,
        repo = doc.getAs[BSONString]("repo").get.value)
    }
  }

  implicit object ProjectBSONWriter extends BSONWriter[Project] {
    def toBSON(project: Project) = BSONDocument(
      "_id" -> BSONObjectID(project.id),
      "name" -> BSONString(project.name),
      "token" -> BSONString(project.token),
      "adminId" -> BSONObjectID(project.adminId),
      "open" -> BSONBoolean(project.open),
      "repo" -> BSONString(project.repo))
  }

  def byId(id: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))) headOption

  def byToken(token: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "token" -> BSONString(token))) headOption

  def byName(name: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "name" -> BSONString(name))) headOption

  def listByIds(ids: List[String]): Future[List[Project]] =
    collection.find(BSONDocument(
      "_id" -> BSONDocument("$in" -> Doc.mkBSONArray(ids)))) toList

  def insert(p: Project): Future[LastError] =
    collection.insert(p)

  def insert(projects: Project*): Future[Int] =
    collection.insert(Enumerator(projects: _*), 100)

  def update(p: Project): Future[LastError] =
    collection.update(BSONDocument(
      "_id" -> BSONObjectID(p.id)), p)

  def remove(p: Project): Future[LastError] =
    collection.remove(BSONDocument(
      "_id" -> BSONObjectID(p.id)))
}
