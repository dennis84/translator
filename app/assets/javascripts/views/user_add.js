define([
  "text!templates/user_add.html"
], function (userAddTemplate) {

  var module = Backbone.View.extend({
    id: "user-add",
    className: "user-add pane-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function () {
      this.$el.html(_.template(userAddTemplate, {}))
      window.app.removePane(1)
      window.app.addPane(this, "spaceless6")

      this.$(".username").typeahead({
        source: function (query, process) {
          return $.getJSON("/usernames?username=" + query, function (data) {
            return process(data)
          })
        }
      })

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
      this.collection.addUser(this.model)

      this.model.on("sync", function (model) {
        model.off("sync")
        window.app.addMessage("success", "User Added")
      })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
