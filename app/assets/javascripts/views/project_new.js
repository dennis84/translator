define([
  "text!templates/project_new.html"
], function (projectNewTemplate) {

  var module = Backbone.View.extend({
    id: "project-new",
    className: "project-new",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    render: function () {
      this.$el.html(_.template(projectNewTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el, "project-new", "spaceless4")
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.collection.create(this.model, { wait: true })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
