package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection

object UserDAO
  extends SalatDAO[DbUser, ObjectId](collection = MongoConnection()("translator")("users")) {

  def byId(id: ObjectId): Option[User] = findOneById(id).map(makeUser(_))

  def findAll: List[User] = find(DBObject()).toList map(makeUser(_))

  def findAllByProject(project: DbProject): List[User] =
    find(MongoDBObject("roles.projectId" -> project.id)).toList map(makeUser(_))

  def findOneByUsername(username: String): Option[User] =
    findOne(MongoDBObject("username" -> username)) map(makeUser(_))

  def findOneByUsernameAndPassword(username: String, password: String): Option[User] =
    findOne(MongoDBObject(
      "username" -> username,
      "password" -> password)) map(makeUser(_))

  def makeUser(u: DbUser): User =
    User(u.username, u.password, u.email, rawRoles = u.roles, id = u.id)
}
