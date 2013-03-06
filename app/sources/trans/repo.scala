package translator
package trans

import scala.concurrent._
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import reactivemongo.core.commands.LastError
import translator.core._
import translator.project._

class TransRepo(val collection: BSONCollection) extends TransHandler {

  def byId(id: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "_id" -> BSONObjectID(id))).one[Trans]

  def byName(p: Project, name: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name))).one[Trans].map { maybeTrans ⇒
        maybeTrans.map(_.withProject(p))
      }

  def byNameAndCode(p: Project, name: String, code: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name),
      "code" -> BSONString(code))).one[Trans].map { maybeTrans ⇒
        maybeTrans.map(_.withProject(p))
      }

  def activated(p: Project, name: String, code: String): Future[Option[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name),
      "code" -> BSONString(code),
      "status" -> BSONInteger(Status.Active.id))).one[Trans].map { maybeTrans ⇒
        maybeTrans.map(_.withProject(p))
      }

  def listByProject(p: Project): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id))).cursor[Trans].toList.map { list ⇒
        list.map(_.withProject(p))
      }

  def listByName(p: Project, name: String): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONString(name))).cursor[Trans].toList.map { list ⇒
        list.map(_.withProject(p))
      }

  def listByNames(p: Project, names: List[String]) =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "name" -> BSONDocument("$in" -> names.foldLeft(BSONArray()) { (arr, name) ⇒
        arr ++ BSONArray(BSONString(name))
      }))).cursor[Trans].toList.map { list ⇒
        list.map(_.withProject(p))
      }

  def listByIds(p: Project, ids: List[String]): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "_id" -> BSONDocument("$in" -> Doc.mkBSONArray(ids))
    )).cursor[Trans].toList.map { list ⇒
      list.map(_.withProject(p))
    }

  def listActive(p: Project, code: String): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "code" -> BSONString(code),
      "status" -> BSONInteger(Status.Active.id)
    )).cursor[Trans].toList.map { list ⇒
      list.map(_.withProject(p))
    }

  def listLike(p: Project, t: String): Future[List[Trans]] =
    collection.find(BSONDocument(
      "projectId" -> BSONObjectID(p.id),
      "$or" -> BSONArray(
        BSONDocument("name" -> BSONRegex("""^.*%s.*""" format t, "i")),
        BSONDocument("text" -> BSONRegex("""^.*%s.*""" format t, "i"))
      ))).cursor[Trans].toList.map { list ⇒
        list.map(_.withProject(p))
      }

  def insert(trans: Trans): Future[LastError] =
    collection.insert(trans)

  def insert(trans: Trans*): Future[List[LastError]] = {
    val futures = trans.toList.map(t ⇒ insert(t))
    Future.sequence(futures)
  }

  def update(trans: Trans): Future[LastError] =
    collection.update(BSONDocument("_id" -> BSONObjectID(trans.id)), trans)

  def remove(trans: Trans): Future[LastError] =
    collection.remove(BSONDocument(
      "_id" -> BSONObjectID(trans.id)))
}
