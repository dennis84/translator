define([
  "text!templates/project.html"
], function (projectTemplate) {

  var module = Backbone.View.extend({
    id: "project",

    render: function () {
      console.log("yo" + Math.random())
      this.$el.html(_.template(projectTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el, "project", "spaceless10")
    }
  })

  return module
})
