package translator
package core

import reactivemongo.bson._

object Doc {

  def mkID: String = BSONObjectID.generate.stringify

  def mkBSONArray(list: List[String]): BSONArray =
    list.foldLeft(BSONArray()) { _ ++ BSONObjectID(_) }

  def mkToken: String = java.util.UUID.randomUUID.toString
}
