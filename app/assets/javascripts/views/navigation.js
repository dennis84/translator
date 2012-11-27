define([
  "collections/project",
  "text!templates/navigation.html",
  "text!templates/navigation_project.html",
  "text!templates/navigation_admin.html"
], function (ProjectCollection, navigationTemplate, navigationProjectTemplate, navigationAdminTemplate) {

  var module = Backbone.View.extend({
    id: "navigation",
    className: "navigation spaceless2",

    initialize: function () {
      window.project.on("change", this.updateProject, this)
      window.projects.on("add", this.addProject, this)
    },

    render: function () {
      this.$el.html(_.template(navigationTemplate, {}))
      this.$("#nav-list").append("<li class='nav-header'>Projects</li>")
      window.projects.each(this.addProject, this)
      $("#application").append(this.el)
    },

    addProject: function (model) {
      this.$("#nav-list").append(
        "<li><a href='#!/" + model.id + "'><i class='icon-sign-blank' style='color: " + this.colorById(model.id) + "'></i> " + model.get("name") + "</a></li>")
    },

    updateProject: function (model) {
      var projectTemplate = $(_.template(navigationProjectTemplate, model.toJSON())).addClass("nav-project")
      this.$(".nav-project").remove()
      this.$el.append(projectTemplate)

      if (true === window.user.equals(window.project.get("admin"))) {
        var adminTemplate = $(_.template(navigationAdminTemplate, model.toJSON())).addClass("nav-admin")
        this.$(".nav-admin").remove()
        this.$el.append(adminTemplate)
      } else {
        this.$(".nav-admin").remove()
      }
    },

    colorById: function (id) {
      return [
        "#7EBDB9",
        "#BDB751",
        "#BD5E3D",
        "#9F50BD",
        "#554BBD",
        "#6397BD",
        "#79BD8F",
        "#68BD47",
        "#BDB87C",
        "#BDA0AE"
      ][id.match(/\d+/g).join("") % 10]
    }
  })

  return module
})
