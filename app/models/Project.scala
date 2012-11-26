package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Project(
  val name: String,
  val adminId: ObjectId,
  val token: String,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val admin = UserDAO.findOneById(adminId)
  lazy val entries = EntryDAO.findAllByProject(this)

  def progress = {
    LanguageDAO.findAllByProject(this) map { lang =>
      lang.code -> entries.filter { entry =>
        entry.translations.exists { trans => trans.code == lang.code && trans.text != "" }
      }.length.toFloat / entries.length * 100
    } toMap
  }

  def contributors = UserDAO.findAllByProject(this)

  def toMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "admin" -> admin.map(_.toMap).getOrElse(Map()),
    "statistics" -> Map(
      "progress" -> progress,
      "nb_entries" -> entries.length
    ))
}
