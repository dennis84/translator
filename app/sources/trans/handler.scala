package translator
package trans

import org.joda.time.DateTime
import reactivemongo.bson._

trait TransHandler {

  implicit object TransHandler
    extends BSONDocumentWriter[Trans]
    with BSONDocumentReader[Trans] {

    def write(trans: Trans) = BSONDocument(
      "_id" -> BSONObjectID(trans.id),
      "code" -> trans.code,
      "name" -> trans.name,
      "text" -> trans.text,
      "author" -> trans.author,
      "status" -> trans.status.id,
      "createdAt" -> BSONDateTime(trans.createdAt.getMillis),
      "updatedAt" -> BSONDateTime(trans.updatedAt.getMillis),
      "projectId" -> BSONObjectID(trans.projectId))

    def read(doc: BSONDocument) = Trans(
      doc.getAs[BSONObjectID]("_id").get.stringify,
      doc.getAs[String]("code").get,
      doc.getAs[String]("name").get,
      doc.getAs[String]("text").get,
      doc.getAs[String]("author").get,
      Status(doc.getAs[Int]("status").get),
      new DateTime(doc.getAs[BSONDateTime]("createdAt").get.value),
      new DateTime(doc.getAs[BSONDateTime]("updatedAt").get.value),
      doc.getAs[BSONObjectID]("projectId").get.stringify)
  }
}
