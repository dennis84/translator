define([
  "views/project"
], function (ProjectView) {

  var module = Backbone.Controller.extend({
    show: function () {
      var view  = new ProjectView({ model: window.project })
      view.render()
    }
  })

  return module
})
