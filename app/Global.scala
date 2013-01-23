package translator

import play.api._
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import translator.core.Environment

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    RegisterJodaTimeConversionHelpers()
  }
}
