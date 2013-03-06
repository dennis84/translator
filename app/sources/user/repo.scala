package translator
package user

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import translator.core._
import translator.project._

class UserRepo(val collection: BSONCollection) extends UserHandler {

  def byId(id: String): Future[Option[User]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))).one[User]

  def byUsername(n: String): Future[Option[User]] =
    collection.find(BSONDocument("username" -> BSONString(n))).one[User]

  def byUsernameAndProject(n: String, p: Project): Future[Option[User]] =
    collection.find(BSONDocument(
      "username" -> BSONString(n),
      "roles.projectId" -> BSONObjectID(p.id))).one[User]

  def byCredentials(n: String, p: String): Future[Option[User]] =
    collection.find(BSONDocument(
      "username" -> BSONString(n),
      "password" -> BSONString(p))).one[User]

  def listByProject(p: Project): Future[List[User]] =
    collection.find(BSONDocument(
      "roles.projectId" -> BSONObjectID(p.id))).cursor[User].toList

  def listLike(n: String): Future[List[User]] =
    collection.find(BSONDocument(
      "username" -> BSONRegex("""^%s.*$""" format n, "i")
    )).cursor[User].toList

  def insert(u: User): Future[LastError] =
    collection.insert(u)

  def insert(users: User*): Future[List[LastError]] = {
    val futures = users.toList.map(u ⇒ insert(u))
    Future.sequence(futures)
  }

  def update(u: User): Future[LastError] =
    collection.update(BSONDocument(
      "_id" -> BSONObjectID(u.id)), u)

  def remove(u: User): Future[LastError] =
    collection.remove(BSONDocument(
      "_id" -> BSONObjectID(u.id)))
}
