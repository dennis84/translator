define([
  "collections/project",
  "text!templates/navigation.html",
  "text!templates/navigation_project.html"
], function (ProjectCollection, navigationTemplate, navigationProjectTemplate) {

  var module = Backbone.View.extend({
    id: "navigation",
    className: "navigation spaceless2",

    colors: [
      "#cc99ff",
      "#99eeff",
      "#00ff00",
      "#ff00cc",
      "#ffff00",
      "#0000ff",
      "#9c6f3a",
      "#ccaa99",
      "#00ffcc",
      "#99aaff"
    ],

    initialize: function () {
      window.projectList.on("reset", this.addProjects, this)
      window.project.on("change", this.updateProject, this)
    },

    render: function () {
      this.$el.html(_.template(navigationTemplate, {}))
      $("#application").append(this.el)
    },

    addProjects: function () {
      this.$el.find("#nav-list").append("<li class='nav-header'>Projects</li>")
      window.projectList.each(this.addProject, this)
    },

    addProject: function (model, index) {
      this.$el.find("#nav-list").append(
        "<li><a href='#!/" + model.id + "'><i class='icon-sign-blank' style='color: " + this.colors[index] + "'></i> " + model.get("name") + "</a></li>")
    },

    updateProject: function (model) {
      var template = $(_.template(navigationProjectTemplate, model.toJSON())).addClass("nav-project")
      this.$el.find(".nav-project").remove()
      this.$el.append(template)
    }
  })

  return module
})
