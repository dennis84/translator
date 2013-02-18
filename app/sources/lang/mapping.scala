package translator
package lang

import reactivemongo.bson._
import reactivemongo.bson.handlers._

object mapping {

  implicit object LangBSONReader extends BSONReader[Lang] {
    def fromBSON(document: BSONDocument): Lang = {
      val doc = document.toTraversable
      Lang(
        doc.getAs[BSONObjectID]("_id").get.stringify,
        doc.getAs[BSONString]("code").get.value,
        doc.getAs[BSONString]("name").get.value,
        doc.getAs[BSONObjectID]("projectId").get.stringify)
    }
  }

  implicit object LangBSONWriter extends BSONWriter[Lang] {
    def toBSON(lang: Lang) = BSONDocument(
      "_id" -> BSONObjectID(lang.id),
      "code" -> BSONString(lang.code),
      "name" -> BSONString(lang.name),
      "projectId" -> BSONObjectID(lang.projectId))
  }
}
