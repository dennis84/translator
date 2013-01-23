package translator.core

import com.mongodb.casbah.Imports._

object Implicits {

  implicit def objectId2string(id: ObjectId): String = id.toString

  implicit def string2objectId(id: String): ObjectId = new ObjectId(id)

  implicit def project2DbProject(p: Project) = p.encode

  implicit def translation2DbTranslation(t: Translation) = t.encode

  implicit def language2DbLanguage(l: Language) = l.encode

  implicit def user2DbUser(u: User) = u.encode

  implicit def list2TranslationCollection(list: List[Translation]) =
    new TranslationCollection(list)
}
