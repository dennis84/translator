define([
  "helpers/form_error",
  "text!templates/user_edit.html"
], function (Error, userEditTemplate) {

  var module = Backbone.View.extend({
    id: "user-edit",
    className: "user-edit",

    events: {
      "click .save": "save"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function () {
      this.$el.html(_.template(userEditTemplate, this.model.toJSON()))
      return this
    },

    renderErrors: function (model, response) {
      window.app.renderErrors(this, response)
    },

    save: function (e) {
      e.preventDefault()
      var data = this.$el.find("form").serializeObject()
      if (false === _.isArray(data.roles)) {
        data.roles = [data.roles]
      }

      this.model.set(data)
      this.collection.create(this.model, { wait: true })

      this.model.on("sync", function () {
        window.app.addMessage("success", "User Created")
      })
    }
  })

  return module
})
