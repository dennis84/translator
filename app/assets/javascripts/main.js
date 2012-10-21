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
  "controllers/application",
  "controllers/user"
], function (User, AppRouter, appController, userController) {
  window.appController = appController
  window.userController = userController

  window.user = new User
  window.user.current(function (model) {
    if (true === window.authenticated) {
      window.router = new AppRouter
      Backbone.history.start()
    } else {
      window.userController.login()
    }
  })

  window.user.on("logged_in", function () {
    location.reload()
  })
})
