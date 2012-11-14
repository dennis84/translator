define([], function () {

  var module = Backbone.Router.extend({
    routes: {
      "":                     "dashboard",
      "!/:project":           "project",
      "!/:project/entries":   "entries",
      "!/:project/languages": "languages",
      "!/:project/import":    "importer",
      "!/:project/users":     "users"
    },

    dashboard: function () {
      window.pageController.dashboard()
    },

    importer: function () {
      window.importController.show()
    },

    project: function (projectId) {
      this.withProject(projectId, function (project) {
        return window.projectController.show()
      })
    },

    entries: function (projectId) {
      this.withProject(projectId, function (project) {
        return window.entryController.list()
      })
    },

    languages: function (projectId) {
      this.withProject(projectId, function (project) {
        return window.languageController.list()
      })
    },

    users: function (projectId) {
      this.withProject(projectId, function (project) {
        return window.userController.list()
      })
    },

    withProject: function (id, func) {
      var model = window.projectList.get(id)
      window.user.currentByProject(id)

      if (undefined !== model) {
        window.project.set(model)
      }

      window.projectList.on("reset", function () {
        var model = window.projectList.get(id)
        window.project.set(model)
      })

      if (false === window.project.isNew()) {
        func(window.project)
      } else {
        window.project.on("change", function (project) {
          func(project)
        })
      }
    }
  })

  return module
})
