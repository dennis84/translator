package translator.models

case class Status(val id: Int, val name: Option[String] = None) extends Ordered[Status] {

  def compare(other: Status) = id compare other.id

  def ==(other: Status) = id == other.id

  override def toString = name getOrElse id.toString
}

object Status {

  def Active   = new Status(10, Some("active"))
  def Empty    = new Status(20, Some("empty"))
  def Inactive = new Status(30, Some("inactive"))
}
