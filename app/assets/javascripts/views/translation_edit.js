define([
  "text!templates/translation_edit.html"
], function (translationEditTemplate) {

  var module = Backbone.View.extend({
    events: {
      "click .accept": "accept",
      "click .reject": "reject"
    },

    render: function () {
      var data = _.extend(this.model.toJSON(), {
        "admin": window.user.isAdmin(),
        "created_at": this.formatDate(this.model.get("created_at")),
        "updated_at": this.formatDate(this.model.get("updated_at"))
      })

      this.$el.html(_.template(translationEditTemplate, data))
      return this
    },

    accept: function (e) {
      e.preventDefault()
      this.model.activate()
      window.app.addMessage("success", "Translation Accepted")
    },

    reject: function (e) {
      e.preventDefault()
      this.model.destroy({ wait: true })
      this.$el.remove()
      window.app.addMessage("success", "Translation Suggestion Removed")
    },

    formatDate: function (timestamp) {
      var d = new Date(timestamp)
      return d.toString("yyyy-MM-dd")
    }
  })

  return module
})
