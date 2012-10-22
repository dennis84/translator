requirejs.config({
  paths: {
    templates: "../templates",
    text:      "libs/text"
  }
})

_.templateSettings.interpolate = /\{\{(.+?)\}\}/g
_.templateSettings.evaluate = /\{\%(.+?)\%\}/g

require([
  "models/user",
  "routers/application",
  "views/application",
  "controllers/page",
  "controllers/user",
  "controllers/project"
], function (User, AppRouter, ApplicationView, pageController, userController, projectController) {
  window.pageController = pageController
  window.userController = userController
  window.projectController = projectController

  window.user = new User
  window.user.current(function (model) {
    if (true === window.authenticated) {
      window.app = new ApplicationView
      window.app.render()

      window.router = new AppRouter
      Backbone.history.start()
    } else {
      window.userController.login()
    }
  })

  window.user.on("logged_in logged_out", function () {
    location.reload()
  })
})
