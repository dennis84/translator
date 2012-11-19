define([
  "text!templates/translation.html"
], function (translationTemplate) {

  var module = Backbone.View.extend({
    events: {
      "click .accept": "accept",
      "click .reject": "reject"
    },

    render: function () {
      this.$el.html(_.template(translationTemplate, this.model.toJSON()))
      return this
    },

    accept: function (e) {
      e.preventDefault()
      console.log("accept")
    },

    reject: function (e) {
      e.preventDefault()
      this.model.destroy()
      this.$el.remove()
    }
  })

  return module
})
