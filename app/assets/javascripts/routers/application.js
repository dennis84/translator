define([], function () {

  var module = Backbone.Router.extend({
    routes: {
      "": "dashboard",
      "@/:project": "project"
    },

    dashboard: function () {
      window.pageController.dashboard()
    },

    project: function (id) {
      window.projectController.show(id)
    }
  })

  return module
})
