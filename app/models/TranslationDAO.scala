package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object TranslationDAO
  extends SalatDAO[Translation, ObjectId](collection = MongoConnection()("translator")("translations")) {

  def findAllByIds(ids: List[ObjectId]) =
    find(MongoDBObject("_id" -> MongoDBObject("$in" -> ids))).toList.groupBy(_.code).map(_._2).reduce(_ ++ _)
}
