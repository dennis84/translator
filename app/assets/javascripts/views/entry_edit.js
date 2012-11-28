define([
  "text!templates/entry_edit.html"
], function (entryEditTemplate) {

  var module = Backbone.View.extend({
    id: "entry-edit",
    className: "entry-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function () {
      this.$el.html(_.template(entryEditTemplate, this.model.toJSON()))
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
