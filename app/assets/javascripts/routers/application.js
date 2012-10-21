define([], function () {

  var module = Backbone.Router.extend({
    routes: {
      "": "dashboard",
      ":project": "project"
    },

    home: function () {
      window.pageController.dashboard()
    },

    project: function (name) {
      console.log("show project: " + name)
    }
  })

  return module
})
