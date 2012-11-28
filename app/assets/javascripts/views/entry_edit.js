define([
  "helpers/form_error",
  "text!templates/entry_edit.html"
], function (Error, entryEditTemplate) {

  var module = Backbone.View.extend({
    id: "entry-edit",
    className: "entry-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.model.on("error", this.renderError, this)
    },

    render: function () {
      this.$el.html(_.template(entryEditTemplate, this.model.toJSON()))
      return this
    },

    renderError: function (model, error) {
      var view = this
      this.$(".form-error-message").remove()

      if (false === (error instanceof Error)) {
        _.each(JSON.parse(error.responseText), function (error) {
          new Error(error.name, error.message).render(view.$el)
        })
      } else {
        error.render(this.$el)
      }

      this.model.off("sync")
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      
      if (this.model.isValid()) {
        this.collection.create(this.model, { wait: true })
      }

      this.model.on("sync", function () {
        window.app.addMessage("success", "Entry Created")
      })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
