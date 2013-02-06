package translator
package trans

case class Status(
  val id: Int,
  val name: Option[String] = None) extends Ordered[Status] {

  def compare(other: Status) = id compare other.id

  def ==(other: Status) = id == other.id

  override def toString = name getOrElse id.toString
}

object Status {

  def apply(id: Int): Status = (for {
    s ← all.find(_.id == id)
  } yield s) getOrElse new Status(id)

  def Active   = new Status(10, Some("active"))
  def Empty    = new Status(20, Some("empty"))
  def Inactive = new Status(30, Some("inactive"))

  def Imported = new Status(100, Some("imported"))
  def Skipped  = new Status(110, Some("skipped"))

  def all = List(Active, Empty, Inactive)
}
