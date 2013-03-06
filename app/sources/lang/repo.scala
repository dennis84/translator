package translator
package lang

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import translator.core._
import translator.project._

class LangRepo(val collection: BSONCollection) extends LangHandler {

  def byId(id: String): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))).one[Lang]

  def byCode(p: Project, code: String): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "code" -> BSONString(code))).one[Lang]

  def primary(p: Project): Future[Option[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))).one[Lang]

  def listByProject(p: Project): Future[List[Lang]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))).cursor[Lang].toList

  def insert(lang: Lang): Future[LastError] =
    collection.insert(lang)

  def insert(langs: Lang*): Future[List[LastError]] = {
    val futures = langs.toList.map(l ⇒ insert(l))
    Future.sequence(futures)
  }

  def update(lang: Lang): Future[LastError] =
    collection.update(BSONDocument(
      "_id" -> BSONObjectID(lang.id)), lang)

  def remove(lang: Lang): Future[LastError] =
    collection.remove(BSONDocument(
      "_id" -> BSONObjectID(lang.id)))
}
