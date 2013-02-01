package translator

import scala.concurrent._
import reactivemongo.bson.handlers.DefaultBSONHandlers

package object core {
  
  implicit val ec = ExecutionContext.Implicits.global
  implicit val writer = DefaultBSONHandlers.DefaultBSONDocumentWriter
  implicit val handler = DefaultBSONHandlers.DefaultBSONReaderHandler
}
