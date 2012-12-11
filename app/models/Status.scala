package translator.models

case class Status(val id: Int, val name: Option[String] = None) extends Ordered[Status] {

  def compare(other: Status) = id compare other.id

  def ==(other: Status) = id == other.id

  def ==(other: String) = name == other

  override def toString = name getOrElse id.toString
}

object Status {

  def Active   = new Status(10, Some("active"))
  def Inactive = new Status(20, Some("inactive"))
  def Empty    = new Status(30, Some("empty"))
}
