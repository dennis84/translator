package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object EntryDAO
  extends SalatDAO[Entry, ObjectId](collection = MongoConnection()("translator")("entries")) {

  def findAll = find(DBObject()) toList

  def findAllByProject(project: Project) = find(MongoDBObject("projectId" -> project.id)) toList

  def findAllByProjectAndFilter(project: Project, filter: Filter) = {
    find(MongoDBObject("projectId" -> project.id)).toList.map { entry =>
      if ("true" == filter.untranslated) {
        entry.translationsFixed.exists(trans => filter.untranslatedLanguages.exists(_ == trans.code) && trans.text == "") match {
          case true => Some(entry)
          case false => None
        }
      } else {
        Some(entry)
      }
    } flatten
  }

  def findOneByNameAndProject(name: String, project: Project) = findOne(MongoDBObject("name" -> name, "projectId" -> project.id))
}
