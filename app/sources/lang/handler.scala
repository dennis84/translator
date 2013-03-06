package translator
package lang

import reactivemongo.bson._

trait LangHandler {

  implicit object LangHandler
    extends BSONDocumentWriter[Lang]
    with BSONDocumentReader[Lang] {

    def write(lang: Lang) = BSONDocument(
      "_id" -> BSONObjectID(lang.id),
      "code" -> lang.code,
      "name" -> lang.name,
      "projectId" -> BSONObjectID(lang.projectId))

    def read(doc: BSONDocument) = Lang(
      doc.getAs[BSONObjectID]("_id").get.stringify,
      doc.getAs[String]("code").get,
      doc.getAs[String]("name").get,
      doc.getAs[BSONObjectID]("projectId").get.stringify)
  }
}
