define([
  "helpers/form_error",
  "text!templates/project_new.html"
], function (Error, projectNewTemplate) {

  var module = Backbone.View.extend({
    id: "project-new",
    className: "project-new pane-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function () {
      this.$el.html(_.template(projectNewTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el, "project-new", "spaceless4")
    },

    renderErrors: function (model, response) {
      window.app.renderErrors(this, response)
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.collection.create(this.model, { wait: true })

      this.model.on("sync", function () {
        window.app.addMessage("success", "Project Created")
        window.man.project()
      })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
