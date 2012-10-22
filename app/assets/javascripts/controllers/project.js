define([
  "views/project"
], function (ProjectView) {

  var module = {
    show: function (id) {
      var view  = new ProjectView({ model: window.project })
      view.render()
    }
  }

  return module
})
