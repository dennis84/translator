requirejs.config({
  paths: {
    templates: "../templates",
    text:      "libs/text"
  }
})

$.ajaxSetup({
  // Removes the brackets on uri parsing.
  traditional: true
})

_.templateSettings.interpolate = /\{\{(.+?)\}\}/g
_.templateSettings.evaluate = /\{\%(.+?)\%\}/g

require([
  "models/user",
  "models/project",
  "collections/project",
  "routers/application",
  "views/application",
  "controllers/page",
  "controllers/user",
  "controllers/project",
  "controllers/entry",
  "controllers/language",
  "controllers/import"
], function (User, Project, ProjectCollection, AppRouter, ApplicationView,
  pageController, userController, projectController, entryController, languageController, importController) {

  window.pageController = pageController
  window.userController = userController
  window.projectController = projectController
  window.entryController = entryController
  window.languageController = languageController
  window.importController = importController

  window.project = new Project

  window.user = new User
  window.user.current(function (model) {
    if (true === window.authenticated) {
      window.projectList = new ProjectCollection
      window.app = new ApplicationView
      window.app.render()

      window.projectList.fetch()

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
