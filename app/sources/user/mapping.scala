package translator
package user

import reactivemongo.bson._
import reactivemongo.bson.handlers._

object mapping {

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
}
