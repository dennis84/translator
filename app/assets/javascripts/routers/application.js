define([], function () {

  var module = Backbone.Router.extend({
    routes: {
      "": "home"
    },

    home: function () {
      window.appController.home()
    }
  })

  return module
})
