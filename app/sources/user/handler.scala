package translator
package user

import reactivemongo.bson._

trait UserHandler {

  implicit object UserHandler
    extends BSONDocumentWriter[User]
    with BSONDocumentReader[User] {

    def write(user: User) = BSONDocument(
      "_id" -> BSONObjectID(user.id),
      "username" -> user.username,
      "password" -> user.password,
      "email" -> user.email,
      "roles" -> user.dbRoles.foldLeft(BSONArray()) { (arr, role) ⇒
        arr ++ BSONArray(BSONDocument(
          "role" -> role.role,
          "projectId" -> BSONObjectID(role.projectId)))
      })

    def read(doc: BSONDocument) = User(
      doc.getAs[BSONObjectID]("_id").get.stringify,
      doc.getAs[String]("username").get,
      doc.getAs[String]("password").get,
      doc.getAs[String]("email").get,
      doc.getAs[BSONArray]("roles").map(array ⇒
        array.values.map { case el: BSONDocument ⇒ Role(
          el.getAs[String]("role").get,
          el.getAs[BSONObjectID]("projectId").get.stringify)
      }.toList).get)

  }
}
