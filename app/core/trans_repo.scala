package translator
package core

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers._
import reactivemongo.core.commands.LastError
import play.api.libs.iteratee.Enumerator
import language._

class TransRepo(val collection: DefaultCollection) {

  implicit object TransBSONReader extends BSONReader[Trans] {
    def fromBSON(document: BSONDocument): Trans = {
      val doc = document.toTraversable
      Trans(
        doc.getAs[BSONObjectID]("_id").get.stringify,
        doc.getAs[BSONString]("code").get.value,
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("text").get.value,
        doc.getAs[BSONString]("author").get.value,
        Status(doc.getAs[BSONInteger]("status").get.value),
        doc.getAs[BSONObjectID]("projectId").get.stringify)
    }
  }

  implicit object TransBSONWriter extends BSONWriter[Trans] {
    def toBSON(trans: Trans) = BSONDocument(
      "_id" -> BSONObjectID(trans.id),
      "code" -> BSONString(trans.code),
      "name" -> BSONString(trans.name),
      "text" -> BSONString(trans.text),
      "author" -> BSONString(trans.author),
      "status" -> BSONInteger(trans.status.id),
      "projectId" -> BSONObjectID(trans.projectId))
  }

  def byId(id: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))) headOption

  def byName(p: Project, name: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name))) headOption

  def byNameAndCode(p: Project, name: String, code: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name),
      "code" -> BSONString(code))) headOption

  def activated(p: Project, name: String, code: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name),
      "code" -> BSONString(code),
      "status" -> BSONInteger(Status.Active.id))) headOption

  def listByProject(p: Project): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))) toList

  def listByName(p: Project, name: String): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name))) toList

  def listByNames(p: Project, names: List[String]) =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONDocument("$in" -> names.foldLeft(BSONArray()) { (arr, name) ⇒
        arr ++ BSONArray(BSONString(name))
      }))) toList

  def listByIds(p: Project, ids: List[String]): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "_id" -> BSONDocument("$in" -> Doc.mkBSONArray(ids)))) toList

  def listActive(p: Project, code: String): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "code" -> BSONString(code),
      "status" -> BSONInteger(Status.Active.id))) toList

  def listLike(p: Project, t: String): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "$or" -> BSONDocument(
        "name" -> BSONString("""/^.*%s.*/""" format t),
        "text" -> BSONString("""/^.*%s.*/""" format t)))) toList

  def insert(trans: Trans): Future[LastError] =
    collection.insert(trans)

  def insert(trans: Trans*): Future[Int] =
    collection.insert(Enumerator(trans: _*), 100)

  def update(trans: Trans): Future[LastError] =
    collection.update(BSONDocument("_id" -> BSONObjectID(trans.id)), trans)

  // def removeEntry(project: Project, name: String) =
  //   find(MongoDBObject(
  //     "projectId" -> project.id,
  //     "name" -> name
  //   )).toList foreach(remove(_))
}
