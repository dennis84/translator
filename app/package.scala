import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.WriteConcern

package object translator {

  implicit def objectId2string(id: ObjectId): String = id.toString

  implicit def string2objectId(id: String): ObjectId = new ObjectId(id)

  implicit val customSalatContext = new com.novus.salat.Context {
    val name = "Translator Context"
  }

  implicit val wc = WriteConcern.Safe
}
