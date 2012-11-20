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
  PageController, UserController, ProjectController, EntryController, LanguageController, ImportController) {

  window.pageController = new PageController
  window.userController = new UserController
  window.projectController = new ProjectController
  window.entryController = new EntryController
  window.languageController = new LanguageController
  window.importController = new ImportController

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
