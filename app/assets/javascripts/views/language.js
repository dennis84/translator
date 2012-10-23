define([
  "text!templates/language.html"
], function (languageTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    events: {
      "click td": "open"
    },

    render: function () {
      this.$el.html(_.template(languageTemplate, this.model.toJSON()))
      return this
    },

    open: function (e) {
      e.preventDefault()
      window.app.removePane(1)
      window.languageController.edit(this.model)
    }
  })

  return module
})
