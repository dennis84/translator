define([], function () {

  var module = Backbone.Router.extend({
    routes: {
      "":                     "dashboard",
      "@/:project":           "project",
      "@/:project/entries":   "entries",
      "@/:project/languages": "languages"
    },

    dashboard: function () {
      window.pageController.dashboard()
    },

    project: function (projectId) {
      this.resolveProject(projectId)

      if (false === window.project.isNew()) {
        return window.projectController.show()
      }

      window.project.on("change", function (project) {
        return window.projectController.show(projectId)
      })
    },

    entries: function (projectId) {
      this.resolveProject(projectId)

      if (false === window.project.isNew()) {
        return window.entryController.list()
      }

      window.project.on("change", function (project) {
        return window.entryController.list()
      })
    },

    languages: function (projectId) {
      this.resolveProject(projectId)

      if (false === window.project.isNew()) {
        return window.languageController.list()
      }

      window.project.on("change", function (project) {
        return window.languageController.list()
      })
    },

    resolveProject: function (id) {
      var model = window.projectList.get(id)

      if (undefined !== model) {
        window.project.set(model)
      }

      window.projectList.on("reset", function () {
        var model = window.projectList.get(id)
        window.project.set(model)
      })
    }
  })

  return module
})
