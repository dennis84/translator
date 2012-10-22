define([
  "views/project"
], function (ProjectView) {

  var module = {
    show: function (id) {
      var view  = new ProjectView
      var model = window.projectList.get(id)

      if (undefined !== model) {
        view.model = model
        view.render()
        return
      }

      window.projectList.on("reset", function () {
        view.model = window.projectList.get(id)
        view.render()
      })
    }
  }

  return module
})
