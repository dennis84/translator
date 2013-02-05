package translator
package core

import play.api.libs.json._

case class Entry(
  trans: Trans,
  project: Project,
  langs: List[Lang] = Nil,
  children: List[Trans] = Nil) {

  def toJson = Json.obj(
    "name" -> trans.name)

  override def toString =
    trans.name + ": [" + children.map(_.code).mkString(", ") + "]"
}
