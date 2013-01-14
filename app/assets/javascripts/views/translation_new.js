define([
  "text!templates/translation_new.html"
], function (translationNewTemplate) {

  var module = Backbone.View.extend({
    id: "translation-new",
    className: "translation-new pane-edit",

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
      var model = this.model
      this.model.set(this.$el.find("form").serializeObject())

      var trans = this.collection.find(function (m) {
        return m.get("name") === model.get("name")
      })

      if (_.isUndefined(trans)) {
        this.collection.create(this.model, { wait: true })
      } else {
        window.app.addMessage("warning",
          "Translation was not created another translation with same name " +
          "already exists")
      }

      this.model.on("sync", function (model) {
        window.app.addMessage("success", "Translation created")
      })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
