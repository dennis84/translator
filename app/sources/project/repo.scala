package translator
package project

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import play.api.libs.iteratee.Enumerator
import translator.core._

class ProjectRepo(val collection: DefaultCollection) {

  import mapping._

  def byId(id: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))).headOption

  def byToken(token: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "token" -> BSONString(token))).headOption

  def byName(name: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "name" -> BSONString(name))).headOption

  def listByIds(ids: List[String]): Future[List[Project]] =
    collection.find(BSONDocument(
      "_id" -> BSONDocument("$in" -> Doc.mkBSONArray(ids)))).toList

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
