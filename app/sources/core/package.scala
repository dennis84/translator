package translator

import scala.concurrent._
import reactivemongo.bson.handlers.DefaultBSONHandlers

package object core {
  
  implicit val ec = ExecutionContext.Implicits.global
  implicit val writer = DefaultBSONHandlers.DefaultBSONDocumentWriter
  implicit val handler = DefaultBSONHandlers.DefaultBSONReaderHandler

  implicit class OptionIsFuture[A](val o: Option[A]) extends AnyVal {
    def future = o.map(Future.successful).getOrElse(Future.failed(new Exception))
  }

  implicit class FlatableFuture[A](val f: Future[A]) extends AnyVal {
    def flatten[B] = f.flatMap[B] { case x: Future[B] ⇒ x }
  }
}
