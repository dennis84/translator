package test.translator.trans

import scala.concurrent._
import org.specs2.specification.Scope
import test.translator._

trait TransContext extends Scope with TestEnv with Fixtures {
  try {
    Await.result(env.transRepo.collection.drop(), timeout)
    Await.result(env.transRepo.collection.create(), timeout)
    Await.result(env.langRepo.collection.drop(), timeout)
    Await.result(env.langRepo.collection.create(), timeout)
  } catch {
    case _: Throwable => println("Collections could not resetted.")
  }

  val translations = List(
    trans1en, trans1de, trans1de1, trans1fr, trans1es, trans1it, trans1pt1,
    trans2en, trans2de, trans2fr, trans2es,
    trans3en, trans3de, trans3fr,
    trans4en, trans4de, trans4fr)

  val langs = List(lang1, lang2, lang3, lang4, lang5, lang6, lang7, lang8, lang9)

  Await.result(env.transRepo.insert(translations: _*), timeout)
  Await.result(env.langRepo.insert(langs: _*), timeout)
}
