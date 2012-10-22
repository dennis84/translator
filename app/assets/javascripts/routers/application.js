define([], function () {

  var module = Backbone.Router.extend({
    routes: {
      "":                   "dashboard",
      "@/:project":         "project",
      "@/:project/entries": "entries"
    },

    initialize: function (options) {
      Backbone.history.on("route", function (obj, name, args) {
        var maybeId = _.first(args)
        if (undefined === maybeId || 24 !== maybeId.length || false === maybeId.match(/^[a-z0-9]+/)) {
          return;
        }

        var model = window.projectList.get(maybeId)

        if (undefined !== model) {
          window.project.set(model)
        }

        window.projectList.on("reset", function () {
          var model = window.projectList.get(maybeId)
          window.project.set(model)
        })
      })
    },

    dashboard: function () {
      window.pageController.dashboard()
    },

    project: function (project) {
      window.projectController.show(project)
    },

    entries: function (project) {
      window.entryController.list(project)
    }
  })

  return module
})
