package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object TranslationDAO
  extends SalatDAO[Translation, ObjectId](collection = MongoConnection()("translator")("translations")) {

  def findAll = find(DBObject()) toList

  def findAllByProject(project: Project) =
    find(MongoDBObject("projectId" -> project.id)) toList

  def findAllByProjectAndCode(project: Project, code: String) =
    find(MongoDBObject("projectId" -> project.id, "code" -> code)) toList

  def findAllByProjectAndName(project: Project, name: String) = {
    val trans = find(MongoDBObject("projectId" -> project.id, "name" -> name)) toList
    val langs = LanguageDAO.findAllByProject(project).map(_.code)
    val diff  = langs.diff(trans.filter(_.status == Status.Active).map(_.code))

    trans match {
      case Nil   => List.empty[Translation]
      case trans => {
        val unsorted = (trans ++ diff.map { code =>
          EmptyTranslation(code, trans.head)
        }) sortBy (_.status.id)

        langs map { l =>
          unsorted.filter(_.code == l)
        } flatten
      }
    }
  }
}
