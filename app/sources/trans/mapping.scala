package translator
package trans

import reactivemongo.bson._
import reactivemongo.bson.handlers._

object mapping {

  implicit object TransBSONReader extends BSONReader[Trans] {
    def fromBSON(document: BSONDocument): Trans = {
      val doc = document.toTraversable
      Trans(
        doc.getAs[BSONObjectID]("_id").get.stringify,
        doc.getAs[BSONString]("code").get.value,
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONString]("text").get.value,
        doc.getAs[BSONString]("author").get.value,
        Status(doc.getAs[BSONInteger]("status").get.value),
        doc.getAs[BSONObjectID]("projectId").get.stringify)
    }
  }

  implicit object TransBSONWriter extends BSONWriter[Trans] {
    def toBSON(trans: Trans) = BSONDocument(
      "_id" -> BSONObjectID(trans.id),
      "code" -> BSONString(trans.code),
      "name" -> BSONString(trans.name),
      "text" -> BSONString(trans.text),
      "author" -> BSONString(trans.author),
      "status" -> BSONInteger(trans.status.id),
      "projectId" -> BSONObjectID(trans.projectId))
  }
}
