package translator
package project

import reactivemongo.bson._
import reactivemongo.bson.handlers._

object mapping {

  implicit object ProjectBSONReader extends BSONReader[Project] {
    def fromBSON(document: BSONDocument): Project = {
      val doc = document.toTraversable
      Project(
        id = doc.getAs[BSONObjectID]("_id").get.stringify,
        name = doc.getAs[BSONString]("name").get.value,
        token = doc.getAs[BSONString]("token").get.value,
        adminId = doc.getAs[BSONObjectID]("adminId").get.stringify,
        open = doc.getAs[BSONBoolean]("open").get.value,
        repo = doc.getAs[BSONString]("repo").get.value)
    }
  }

  implicit object ProjectBSONWriter extends BSONWriter[Project] {
    def toBSON(project: Project) = BSONDocument(
      "_id" -> BSONObjectID(project.id),
      "name" -> BSONString(project.name),
      "token" -> BSONString(project.token),
      "adminId" -> BSONObjectID(project.adminId),
      "open" -> BSONBoolean(project.open),
      "repo" -> BSONString(project.repo))
  }
}
