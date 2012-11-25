define([
  "models/user",
  "models/project",
  "collections/entry",
  "collections/language",
  "collections/project",
  "collections/translation",
  "collections/user",
  "views/application",
  "views/user_profile",
  "views/login",
  "views/dashboard",
  "views/project",
  "views/entries",
  "views/languages",
  "views/users",
  "views/import"
], function (User, Project, Entries, Languages, Projects, Translations, Users,
  ApplicationView, UserProfileView, LoginView, DashboardView, ProjectView,
  EntriesView, LanguagesView, UsersView, ImportView) {

  var Router = Backbone.Router.extend({
    routes: {
      "":                     "dashboard",
      "profile":              "profile",
      "!/:project":           "project",
      "!/:project/entries":   "entries",
      "!/:project/languages": "languages",
      "!/:project/import":    "importer",
      "!/:project/users":     "users"
    }
  })

  var withProject = function (id, func) {
    var model = window.projects.get(id)
    window.user.currentByProject(id)

    window.project.set(model)
    func(window.project)
  }

  var initialize = function () {
    window.router = new Router

    window.entries      = new Entries
    window.languages    = new Languages
    window.projects     = new Projects
    window.translations = new Translations
    window.users        = new Users

    window.user = new User
    window.project = new Project

    router.on("route:dashboard", function () {
      var view = new DashboardView
      view.render()
    })

    router.on("route:profile", function () {
      var view = new UserProfileView({ model: window.user })
      view.render()
    })

    router.on("route:project", function (projectId) {
      withProject(projectId, function (project) {
        var view = new ProjectView({ model: window.project })
        view.render()
      })
    })

    router.on("route:entries", function (projectId) {
      withProject(projectId, function (project) {
        var view = new EntriesView({ collection: window.entries })

        window.entries.filter.on("change", function () {
          window.entries.fetch({ data: window.entries.filter.toJSON() })
        })

        window.entries.fetch()
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
        view.render()
      })
    })

    window.user.current(function (model) {
      if (true === window.authenticated) {
        window.projects.on("reset", function () {
          console.log("projetcs")
          window.app = new ApplicationView
          window.app.render()
          Backbone.history.start()
        })
        window.projects.fetch()
      } else {
        var login = new LoginView({ model: window.user })
        login.render()
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
