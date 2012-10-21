define([
  "views/project",
  "text!templates/projects.html"
], function (ProjectView, projectsTemplate) {

  var module = Backbone.View.extend({
    id: "projects",

    initialize: function () {
      this.collection.on("reset", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(projectsTemplate, {}))
      this.collection.each(this.add, this)
    },

    add: function (model) {
      var view = new ProjectView({ model: model })
      this.$el.find("ul").append(view.render().el)
    }
  })

  return module
})
