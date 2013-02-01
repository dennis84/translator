package translator
package core

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers._
import language._

class UserRepo(val collection: DefaultCollection) {

  implicit object UserBSONReader extends BSONReader[User] {
    def fromBSON(document: BSONDocument): User = {
      val doc = document.toTraversable
      User(
        id = doc.getAs[BSONObjectID]("_id").get.stringify,
        username = doc.getAs[BSONString]("username").get.value,
        password = doc.getAs[BSONString]("password").get.value,
        email = doc.getAs[BSONString]("email").get.value,
        dbRoles = doc.getAs[BSONArray]("dbRoles").get.toTraversable.iterator.map { _.value match {
          case role: BSONDocument ⇒ Role(
            role.toTraversable.getAs[BSONString]("role").get.value,
            role.toTraversable.getAs[BSONString]("projectId").get.value)
          case _ ⇒ null
        }}.toList)
    }
  }

  implicit object UserBSONWriter extends BSONWriter[User] {
    def toBSON(user: User) = BSONDocument(
      "_id" -> BSONObjectID(user.id),
      "username" -> BSONString(user.username),
      "password" -> BSONString(user.password),
      "email" -> BSONString(user.email),
      "dbRoles" -> user.dbRoles.foldLeft(BSONArray()) { (arr, doc) ⇒
        arr ++ BSONDocument(
          "role" -> BSONString(doc.role),
          "projectId" -> BSONObjectID(doc.projectId))
      })
  }

  def byId(id: String): Future[Option[User]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))) headOption

  def byUsername(n: String): Future[Option[User]] =
    collection.find(BSONDocument("username" -> BSONString(n))).headOption

  def byUsernameAndProject(n: String, p: Project): Future[Option[User]] =
    collection.find(BSONDocument(
      "username" -> BSONString(n),
      "roles.projectId" -> BSONObjectID(p.id))) headOption

  def byCredentials(n: String, p: String): Future[Option[User]] =
    collection.find(BSONDocument(
      "username" -> BSONString(n),
      "password" -> BSONString(p))) headOption

  def list(p: Project): Future[List[User]] =
    collection.find(BSONDocument(
      "roles.projectId" -> BSONObjectID(p.id))) toList

  def listLike(n: String): Future[List[User]] =
    collection.find(BSONDocument(
      "username" -> BSONString("""/^%s.*$/""" format n))) toList
}
