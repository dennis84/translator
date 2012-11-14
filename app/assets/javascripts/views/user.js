define([
  "text!templates/user.html"
], function (userTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    initialize: function () {
      this.model.on("change", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(userTemplate, this.model.toJSON()))
      return this
    }
  })

  return module
})
