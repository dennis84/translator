define([
  "collections/project",
  "text!templates/navigation.html"
], function (ProjectCollection, navigationTemplate) {

  var module = Backbone.View.extend({
    id: "navigation",
    className: "navigation spaceless2",

    initialize: function () {
      this.projects = new ProjectCollection
      this.projects.on("reset", this.addProjects, this)
      this.projects.fetch()
    },

    render: function () {
      this.$el.html(_.template(navigationTemplate, {}))
      $("#application").append(this.el)
    },

    addProjects: function () {
      this.$el.find("#nav-list").append("<li class='nav-header'>Projects</li>")
      this.projects.each(this.addProject, this)
    },

    addProject: function (model) {
      this.$el.find("#nav-list").append("<li><a href='#" + model.get("name") + "'>" + model.get("name") + "</a></li>")
    }
  })

  return module
})
