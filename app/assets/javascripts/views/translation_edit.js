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
        "admin": window.user.isAdmin()
      })

      this.$el.html(_.template(translationEditTemplate, data))
      return this
    },

    accept: function (e) {
      e.preventDefault()
      this.model.activate()
    },

    reject: function (e) {
      e.preventDefault()
      this.model.destroy()
      this.$el.remove()
    }
  })

  return module
})
