package translator
package project

import reactivemongo.bson._

trait ProjectHandler {

  implicit object ProjectHandler
    extends BSONDocumentWriter[Project]
    with BSONDocumentReader[Project] {

    def write(project: Project) = BSONDocument(
      "_id" -> BSONObjectID(project.id),
      "name" -> project.name,
      "token" -> project.token,
      "adminId" -> BSONObjectID(project.adminId),
      "open" -> project.open,
      "repo" -> project.repo)

    def read(doc: BSONDocument) = Project(
      doc.getAs[BSONObjectID]("_id").get.stringify,
      doc.getAs[String]("name").get,
      doc.getAs[String]("token").get,
      doc.getAs[BSONObjectID]("adminId").get.stringify,
      doc.getAs[Boolean]("open").get,
      doc.getAs[String]("repo").get)
  }
}
