package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Project(
  val name: String,
  val adminId: ObjectId,
  val token: String,
  @Key("_id") val id: ObjectId = new ObjectId)

case class ProjectView(
  val model: Project,
  val admin: User,
  val progress: Map[String, Float],
  val nbEntries: Int,
  val nbWords: Int) {

  def serialize = Map(
    "id" -> model.id.toString,
    "name" -> model.name,
    "statistics" -> Map(
      "progress" -> progress,
      "nb_entries" -> nbEntries,
      "nb_words" -> nbWords
    ))

  case class Statistics(
    val progress: Map[String, Float],
    val nbEntries: Int,
    val nbWords: Int)
}
