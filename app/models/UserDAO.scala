package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection

object UserDAO
  extends SalatDAO[DbUser, ObjectId](collection = MongoConnection()("translator")("users")) {

  def list(project: DbProject): List[User] =
    find(MongoDBObject("roles.projectId" -> project.id)).toList map(makeUser(_))

  def byId(id: ObjectId): Option[User] = findOneById(id).map(makeUser(_))

  def byUsername(username: String): Option[User] =
    findOne(MongoDBObject("username" -> username)) map(makeUser(_))

  def byCredentials(username: String, password: String): Option[User] =
    findOne(MongoDBObject(
      "username" -> username,
      "password" -> password)) map(makeUser(_))

  private def makeUser(u: DbUser): User =
    User(u.username, u.password, u.email, rawRoles = u.roles, id = u.id)
}
