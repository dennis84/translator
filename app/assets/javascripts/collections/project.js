define(["models/project"], function (Project) {
  var module = Backbone.Collection.extend({
    model: Project,

    url: function () {
      return "/projects"
    }
  })

  return module
})
