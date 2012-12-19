package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Language(
  val code: String,
  val name: String,
  val projectId: ObjectId,
  @Key("_id") val id: ObjectId = new ObjectId)
