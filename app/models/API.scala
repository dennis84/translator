package translator.models

trait Item {

  def response: Map[String, Any]
}

class Collection[A <: Item](val items: List[A]) {

  def response = items map(_.response)
}

object Collection {

  def apply[A, B <: Item](items: List[A])(implicit m: Manifest[B]) = new Collection(items.map { item =>
    manifest[B].erasure.newInstance.asInstanceOf[B]
  })
}
