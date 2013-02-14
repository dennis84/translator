define([
  "views/project_edit",
  "text!templates/project.html"
], function (ProjectEditView, projectTemplate) {

  var module = Backbone.View.extend({
    id: "project",
    className: "project",

    events: {
      "click .edit": "edit"
    },

    render: function () {
      this.$el.html(_.template(projectTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this, "spaceless10")
    },

    edit: function (e) {
      e.preventDefault()
      var view = new ProjectEditView({ model: this.model })
      view.render()
    }
  })

  return module
})
