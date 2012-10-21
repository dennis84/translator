package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Translation(
  val code: String,
  val text: String,
  val authorId: ObjectId,
  @Key("_id") val id: ObjectId = new ObjectId)

case class Entry(
  val name: String,
  val description: String,
  val projectId: ObjectId,
  val translations: List[Translation] = Nil,
  @Key("_id") val id: ObjectId = new ObjectId)
