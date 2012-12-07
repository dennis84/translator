package translator.models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.dao._
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.Imports._

object EntryDAO
  extends SalatDAO[Entry, ObjectId](collection = MongoConnection()("translator")("entries")) {

  def findAll = find(DBObject()) toList

  def findAllByProject(project: Project) =
    find(MongoDBObject("projectId" -> project.id)) toList

  def findAllByProjectAndFilter(project: Project, filter: Filter) = {
    var entries = find(MongoDBObject("projectId" -> project.id)) toList

    if ("true" == filter.untranslated) {
      entries = entries filter { entry =>
        // translations fixed or return true if no translation by code exists.
        entry.translations.exists(t => t.text == "" && filter.untranslatedLanguages.exists(_ == t.code))
      }
    }

    if ("true" == filter.activatable) {
      entries = entries filter { entry =>
        entry.activatableTranslations.length > 0
      }
    }

    entries
  }
  
  override def findOneById(id: ObjectId) =
    find(MongoDBObject("_id" -> id))
      .sort(orderBy = MongoDBObject("translations.code" -> 1))
      .toList
      .headOption

  def findOneByNameAndProject(name: String, project: Project) =
    findOne(MongoDBObject("name" -> name, "projectId" -> project.id))

  def findAllByProjectAndIds(project: Project, ids: List[ObjectId]) =
    find(MongoDBObject("projectId" -> project.id, "_id" -> MongoDBObject("$in" -> ids))) toList
}
