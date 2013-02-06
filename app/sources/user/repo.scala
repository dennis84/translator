package translator
package user

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers._
import reactivemongo.core.commands.LastError
import play.api.libs.iteratee.Enumerator
import language._
import translator.core._
import translator.project._

class UserRepo(val collection: DefaultCollection) {

  implicit object UserBSONReader extends BSONReader[User] {
    def fromBSON(document: BSONDocument): User = {
      val doc = document.toTraversable
      User(
        id = doc.getAs[BSONObjectID]("_id").get.stringify,
        username = doc.getAs[BSONString]("username").get.value,
        password = doc.getAs[BSONString]("password").get.value,
        email = doc.getAs[BSONString]("email").get.value,
        dbRoles = doc.getAs[BSONArray]("roles").get.iterator.map { _.value match {
          case role: BSONDocument ⇒ Role(
            role.toTraversable.getAs[BSONString]("role").get.value,
            role.toTraversable.getAs[BSONObjectID]("projectId").get.stringify)
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
      "roles" -> user.dbRoles.foldLeft(BSONArray()) { (arr, role) ⇒
        arr ++ BSONArray(BSONDocument(
          "role" -> BSONString(role.role),
          "projectId" -> BSONObjectID(role.projectId)))
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

  def listByProject(p: Project): Future[List[User]] =
    collection.find(BSONDocument(
      "roles.projectId" -> BSONObjectID(p.id))) toList

  def listLike(n: String): Future[List[User]] =
    collection.find(BSONDocument(
      "username" -> BSONRegex("""^%s.*$""" format n, "i"))) toList

  def insert(u: User): Future[LastError] =
    collection.insert(u)

  def insert(users: User*): Future[Int] =
    collection.insert(Enumerator(users: _*), 100)

  def update(u: User): Future[LastError] =
    collection.update(BSONDocument(
      "_id" -> BSONObjectID(u.id)), u)

  def remove(u: User): Future[LastError] =
    collection.remove(BSONDocument(
      "_id" -> BSONObjectID(u.id)))
}
