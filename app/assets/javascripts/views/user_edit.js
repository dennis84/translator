define([
  "helpers/form",
  "helpers/form_error",
  "text!templates/user_edit.html"
], function (formHelper, Error, userEditTemplate) {

  var module = Backbone.View.extend({
    id: "user-edit",
    className: "user-edit pane-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function () {
      var data = _.extend(this.model.toJSON(), {
        "formHelper": formHelper,
        "isNew": this.model.isNew()
      })

      this.$el.html(_.template(userEditTemplate, data))
      window.app.removePane(1)
      window.app.addPane(this, "spaceless6")
      $("select").chosen()
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
      if (this.model.isNew()) {
        this.collection.create(this.model, { wait: true })
        var successMessage = "User Created"
      } else {
        this.model.save()
        var successMessage = "User Saved"
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
