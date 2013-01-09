package translator.models

import translator.utils.git._
import translator.ProjectContext

object TranslationRepo {

  def write(t: Translation)(implicit ctx: ProjectContext[_]) = WithRepo(ctx) { git =>
    ""
  }
}
