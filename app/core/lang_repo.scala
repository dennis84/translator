package translator
package core

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers._
import reactivemongo.core.commands.LastError
import play.api.libs.iteratee.Enumerator
import language._

class LangRepo(val collection: DefaultCollection) {

  implicit object LangBSONReader extends BSONReader[Lang] {
    def fromBSON(document: BSONDocument): Lang = {
      val doc = document.toTraversable
      Lang(
        doc.getAs[BSONObjectID]("_id").get.stringify,
        doc.getAs[BSONString]("code").get.value,
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONObjectID]("projectId").get.stringify)
    }
  }

  implicit object LangBSONWriter extends BSONWriter[Lang] {
    def toBSON(lang: Lang) = BSONDocument(
      "_id" -> BSONObjectID(lang.id),
      "code" -> BSONString(lang.code),
      "name" -> BSONString(lang.name),
      "projectId" -> BSONObjectID(lang.projectId))
  }

  def byId(id: String): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))) headOption

  def byCode(p: Project, code: String): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "code" -> BSONString(code))) headOption

  def primary(p: Project): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))) headOption

  def listByProject(p: Project): Future[List[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))) toList

  def insert(lang: Lang): Future[LastError] =
    collection.insert(lang)

  def insert(lang: Lang*): Future[Int] =
    collection.insert(Enumerator(lang: _*), 100)

  def update(lang: Lang): Future[LastError] =
    collection.update(BSONDocument(
      "_id" -> BSONObjectID(lang.id)), lang)

  def remove(lang: Lang): Future[LastError] =
    collection.remove(BSONDocument(
      "_id" -> BSONObjectID(lang.id)))
}
