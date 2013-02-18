package translator
package lang

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import play.api.libs.iteratee.Enumerator
import translator.core._
import translator.project._

class LangRepo(val collection: DefaultCollection) {

  import mapping._

  def byId(id: String): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))).headOption

  def byCode(p: Project, code: String): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "code" -> BSONString(code))).headOption

  def primary(p: Project): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))).headOption

  def listByProject(p: Project): Future[List[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))).toList

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
