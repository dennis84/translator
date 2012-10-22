define([], function () {

  var module = Backbone.Router.extend({
    routes: {
      "": "dashboard",
      ":project": "project"
    },

    dashboard: function () {
      window.pageController.dashboard()
    },

    project: function (name) {
      window.projectController.show(name)
    }
  })

  return module
})
