define([
  "collections/project",
  "text!templates/navigation.html",
  "text!templates/navigation_project.html",
  "text!templates/navigation_admin.html"
], function (ProjectCollection, navigationTemplate, navigationProjectTemplate, navigationAdminTemplate) {

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
      window.projects.on("reset", this.addProjects, this)
      window.project.on("change", this.updateProject, this)
    },

    render: function () {
      this.$el.html(_.template(navigationTemplate, {}))
      $("#application").append(this.el)
    },

    addProjects: function () {
      this.$el.find("#nav-list").append("<li class='nav-header'>Projects</li>")
      window.projects.each(this.addProject, this)
    },

    addProject: function (model, index) {
      this.$el.find("#nav-list").append(
        "<li><a href='#!/" + model.id + "'><i class='icon-sign-blank' style='color: " + this.colors[index] + "'></i> " + model.get("name") + "</a></li>")
    },

    updateProject: function (model) {
      var projectTemplate = $(_.template(navigationProjectTemplate, model.toJSON())).addClass("nav-project")
      this.$el.find(".nav-project").remove()
      this.$el.append(projectTemplate)

      if (true === window.user.equals(window.project.get("admin"))) {
        var adminTemplate = $(_.template(navigationAdminTemplate, model.toJSON())).addClass("nav-admin")
        this.$el.find(".nav-admin").remove()
        this.$el.append(adminTemplate)
      } else {
        this.$el.find(".nav-admin").remove()
      }
    }
  })

  return module
})
