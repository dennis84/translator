package translator

import scala.concurrent._

package object core {
  
  implicit val ec = ExecutionContext.Implicits.global
}
