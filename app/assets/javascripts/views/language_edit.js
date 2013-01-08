define([
  "text!templates/language_edit.html"
], function (languageEditTemplate) {

  var module = Backbone.View.extend({
    id: "language-edit",
    className: "language-edit pane-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function () {
      this.$el.html(_.template(languageEditTemplate, this.model.toJSON()))
      return this
    },

    renderErrors: function (model, response) {
      window.app.renderErrors(this, response)
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())

      if (this.model.isNew()) {
        this.collection.create(this.model, { wait: true })
        var successMessage = "Language Created"
      } else {
        this.model.save()
        var successMessage = "Language Saved"
      }

      this.model.on("sync", function () {
        window.app.addMessage("success", successMessage)
      })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
