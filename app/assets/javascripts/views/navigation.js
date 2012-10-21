define([
  "collections/project",
  "text!templates/navigation.html"
], function (ProjectCollection, navigationTemplate) {

  var module = Backbone.View.extend({
    id: "navigation",
    className: "navigation spaceless2",

    initialize: function () {
      this.projects = new ProjectCollection
      this.projects.on("reset", this.renderProjects, this)
      this.projects.fetch()
    },

    render: function () {
      this.$el.html(_.template(navigationTemplate, {}))
      $("#application").append(this.el)
    },

    renderProjects: function () {
      this.projects.each(this.addProject, this)
    },

    addProject: function (model) {
      console.log("add nav project")
    }
  })

  return module
})
