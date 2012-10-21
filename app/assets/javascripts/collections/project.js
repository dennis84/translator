define(["models/project"], function (project) {
  var module = Backbone.Collection.extend({
    model: project,

    url: function () {
      return "/projects"
    }
  })

  return module
})
