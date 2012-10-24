define([
  "text!templates/entry.html"
], function (entryTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    events: {
      "click td": "open"
    },

    initialize: function () {
      this.model.on("change", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(entryTemplate, this.model.toJSON()))
      return this
    },

    open: function (e) {
      e.preventDefault()
      window.app.removePane(1)
      window.entryController.edit(this.model)
    }
  })

  return module
})
