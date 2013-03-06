package translator
package project

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import translator.core._

class ProjectRepo(val collection: BSONCollection) extends ProjectHandler {

  def byId(id: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))).one[Project]

  def byToken(token: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "token" -> BSONString(token))).one[Project]

  def byName(name: String): Future[Option[Project]] =
    collection.find(BSONDocument(
      "name" -> BSONString(name))).one[Project]

  def listByIds(ids: List[String]): Future[List[Project]] =
    collection.find(BSONDocument(
      "_id" -> BSONDocument("$in" -> Doc.mkBSONArray(ids))
    )).cursor[Project].toList

  def insert(p: Project): Future[LastError] =
    collection.insert(p)

  def insert(projects: Project*): Future[List[LastError]] = {
    val futures = projects.toList.map(p ⇒ insert(p))
    Future.sequence(futures)
  }

  def update(p: Project): Future[LastError] =
    collection.update(BSONDocument(
      "_id" -> BSONObjectID(p.id)), p)

  def remove(p: Project): Future[LastError] =
    collection.remove(BSONDocument(
      "_id" -> BSONObjectID(p.id)))
}
