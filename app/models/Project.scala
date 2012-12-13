package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Project(
  val name: String,
  val adminId: ObjectId,
  val token: String,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val admin = UserDAO.findOneById(adminId)

  lazy val translations = TranslationDAO.findAllByProject(this)

  lazy val progress = LanguageDAO.findAllByProject(this) map { lang =>
    lang.code -> (translations.filter { trans =>
      trans.code == lang.code && trans.text != "" && trans.status == Status.Active
    }.length.toFloat) / translations.map(_.name).distinct.length * 100
  } toMap

  def nbWords = LanguageDAO.findAllByProject(this).map { lang =>
    lang.code -> (translations.filter { trans =>
      trans.code == lang.code && (trans.status == Status.Active || trans.status == Status.Empty)
    }.map(_.nbWords).reduceLeft(_ + _))
  } toMap

  def contributors = UserDAO.findAllByProject(this)

  def toMap = Map(
    "id" -> id.toString,
    "name" -> name,
    "admin" -> admin.map(_.toMap).getOrElse(Map()),
    "statistics" -> Map(
      "progress" -> progress,
      "nb_entries" -> translations.map(_.name).distinct.length,
      "nb_words" -> nbWords
    ))
}
