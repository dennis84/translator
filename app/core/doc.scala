package translator
package core

import reactivemongo.bson._

object Doc {

  def randomID: String = BSONObjectID.generate.stringify

  def makeBSONArray(list: List[String]): BSONArray =
    list.foldLeft(BSONArray()) { _ ++ BSONObjectID(_) }
}
