define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      return "/projects"
    },

    defaults: {
      id: null,
      name: ""
    }
  })

  return module
})
