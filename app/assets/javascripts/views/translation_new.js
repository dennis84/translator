define([
  "text!templates/translation_new.html"
], function (translationNewTemplate) {

  var module = Backbone.View.extend({
    id: "translation-new",
    className: "translation-new",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function () {
      this.$el.html(_.template(translationNewTemplate, this.model.toJSON()))
      return this
    },

    renderErrors: function (model, response) {
      window.app.renderErrors(this, response)
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.collection.create(this.model, { wait: true })

      this.model.on("sync", function () {
        window.app.addMessage("success", "Translation Created")
      })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
