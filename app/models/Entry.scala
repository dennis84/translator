package translator.models

import translator.models.Implicits._

case class Entry(
  val trans: Translation,
  val project: Project,
  val langs: List[Language],
  val children: List[Translation],
  val nbActivatable: Int,
  val nbMustActivated: Int,
  val nbUntranslated: Int,
  val progress: Float) {

  def nbUntranslated(langs: List[String]) =
    children.filterUntranslated(langs).length

  def serialize = Map(
    "id" -> trans.id.toString,
    "code" -> trans.code,
    "name" -> trans.name,
    "nb_activatable" -> nbActivatable,
    "nb_must_activated" -> nbMustActivated,
    "nb_untranslated" -> nbUntranslated,
    "progress" -> progress)
}

object Entry {

  def apply(
    t: Translation,
    p: Project,
    l: List[Language],
    c: List[Translation]
  ): Entry = Entry(
    trans = t,
    project = p,
    langs = l,
    children = c.fixed(l),
    nbActivatable = c.filterActivatable.length,
    nbMustActivated = c.filterMustActivated.length,
    nbUntranslated = c.filterUntranslated.length,
    progress = c.filterCompleted.length.toFloat / l.length * 100)
}
