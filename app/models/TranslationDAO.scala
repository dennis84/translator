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
}
