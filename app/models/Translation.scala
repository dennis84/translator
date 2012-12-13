package translator.models

import com.novus.salat.annotations._
import com.mongodb.casbah.Imports._

case class Translation(
  val code: String,
  val name: String,
  val text: String,
  val projectId: ObjectId,
  val authorId: ObjectId,
  val status: Status,
  @Key("_id") val id: ObjectId = new ObjectId) {

  lazy val project = ProjectDAO.findOneById(projectId)

  lazy val author = UserDAO.findOneById(authorId)

  lazy val nbWords = text.split(" ").filterNot(_ == "").length

  lazy val progress = project map { project =>
    val languages = LanguageDAO.findAllByProject(project)
    val translations = TranslationDAO.findAllByProjectAndName(project, name)

    languages.filter { lang =>
      translations exists { trans => trans.code == lang.code && trans.text != "" && trans.status == Status.Active }
    }.length.toFloat / languages.length * 100
  } getOrElse 0

  lazy val activatableTranslations = project map { project =>
    TranslationDAO.findAllByProjectAndName(project, name) filter (_.status == Status.Inactive)
  } getOrElse List.empty[Translation]

  def toMap = Map(
    "id" -> id.toString,
    "code" -> code,
    "name" -> name,
    "text" -> text,
    "author" -> author.map(_.username).getOrElse("unknown"),
    "status" -> status.toString,
    "nb_activatable" -> activatableTranslations.length,
    "progress" -> progress
  )
}

object EmptyTranslation {

  def apply(code: String, t: Translation) = Translation(code, t.name, "", t.projectId, t.authorId, Status.Empty)
}
