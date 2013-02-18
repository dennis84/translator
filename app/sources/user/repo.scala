package translator
package user

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import play.api.libs.iteratee.Enumerator
import translator.core._
import translator.project._

class UserRepo(val collection: DefaultCollection) {

  import mapping._

  def byId(id: String): Future[Option[User]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))).headOption

  def byUsername(n: String): Future[Option[User]] =
    collection.find(BSONDocument("username" -> BSONString(n))).headOption

  def byUsernameAndProject(n: String, p: Project): Future[Option[User]] =
    collection.find(BSONDocument(
      "username" -> BSONString(n),
      "roles.projectId" -> BSONObjectID(p.id))).headOption

  def byCredentials(n: String, p: String): Future[Option[User]] =
    collection.find(BSONDocument(
      "username" -> BSONString(n),
      "password" -> BSONString(p))).headOption

  def listByProject(p: Project): Future[List[User]] =
    collection.find(BSONDocument(
      "roles.projectId" -> BSONObjectID(p.id))).toList

  def listLike(n: String): Future[List[User]] =
    collection.find(BSONDocument(
      "username" -> BSONRegex("""^%s.*$""" format n, "i"))).toList

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
