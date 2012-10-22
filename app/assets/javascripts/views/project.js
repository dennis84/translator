define([
  "text!templates/project.html"
], function (projectTemplate) {

  var module = Backbone.View.extend({
    id: "project",

    render: function () {
      this.$el.html(_.template(projectTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el)
    }
  })

  return module
})
