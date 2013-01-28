define([
  "models/user",
  "models/project",
  "collections/language",
  "collections/project",
  "collections/translation",
  "collections/user",
  "views/application",
  "views/application_mobile",
  "views/user_profile",
  "views/start",
  "views/dashboard",
  "views/project",
  "views/project_new",
  "views/translations",
  "views/languages",
  "views/users",
  "views/import",
  "views/sync",
  "views/manual"
], function (User, Project, Languages, Projects, Translations, Users,
  ApplicationView, ApplicationMobileView, UserProfileView,
  StartView, DashboardView, ProjectView, ProjectNewView, TranslationsView,
  LanguagesView, UsersView, ImportView, SyncView, ManualView) {

  var Router = Backbone.Router.extend({
    routes: {
      "":                        "dashboard",
      "profile":                 "profile",
      "new-project":             "newProject",
      "!/:project":              "project",
      "!/:project/translations": "translations",
      "!/:project/languages":    "languages",
      "!/:project/import":       "importer",
      "!/:project/users":        "users",
      "!/:project/sync":         "sync"
    }
  })

  var withProject = function (id, func) {
    var model = window.projects.get(id)
    window.user.on("sync", function () {
      window.project.set(model)
      func(window.project)
      window.user.off("sync")
    })

    window.user.currentByProject(id)
  }

  var initialize = function () {
    window.router = new Router

    window.languages    = new Languages
    window.projects     = new Projects
    window.translations = new Translations
    window.users        = new Users

    window.user = new User
    window.project = new Project
    window.man = new ManualView

    window.vent = _.extend({}, Backbone.Events)
    window.mobileApp = new ApplicationMobileView

    router.on("route:dashboard", function () {
      var view = new DashboardView
      view.render()
    })

    router.on("route:profile", function () {
      var view = new UserProfileView({ model: window.user })
      view.render()
    })

    router.on("route:newProject", function () {
      var view = new ProjectNewView({ model: new Project, collection: window.projects })
      view.render()
    })

    router.on("route:project", function (projectId) {
      withProject(projectId, function (project) {
        window.project.on("change", function () {
          var view = new ProjectView({ model: window.project })
          view.render()
        }, this)

        window.project.fetch()
      })
    })

    router.on("route:translations", function (projectId) {
      withProject(projectId, function (project) {
        var view = new TranslationsView({ collection: window.translations })

        window.translations.filter.on("change", function () {
          window.translations.fetch({ data: window.translations.filter.toJSON() })
        })

        window.translations.fetch()
      })
    })

    router.on("route:languages", function (projectId) {
      withProject(projectId, function (project) {
        var view = new LanguagesView({ collection: window.languages })
        window.languages.fetch()
      })
    })

    router.on("route:users", function (projectId) {
      withProject(projectId, function (project) {
        var view = new UsersView({ collection: window.users })
        window.users.fetch()
      })
    })

    router.on("route:importer", function (projectId) {
      withProject(projectId, function (project) {
        var view = new ImportView
      })
    })

    router.on("route:sync", function (projectId) {
      withProject(projectId, function (project) {
        var view = new SyncView
        view.render()
      })
    })

    window.user.current(function (model) {
      if (true === window.authenticated) {
        window.projects.on("reset", function () {
          window.app = new ApplicationView
          window.app.render()
          Backbone.history.start()
        })
        window.projects.fetch()
      } else {
        var start = new StartView({ model: window.user })
        start.render("sign_in")
      }
    })

    window.user.on("logged_in logged_out", function () {
      location.reload()
    })
  }

  return {
    initialize: initialize
  }
})
