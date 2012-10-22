package translator.controllers

object ProjectController extends BaseController {

  def list = SecuredIO { implicit ctx =>
    JsonOk(ctx.projects map (_.toMap))
  }
}
