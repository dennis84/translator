define([
  "text!templates/entry.html"
], function (entryTemplate) {

  var module = Backbone.View.extend({
    tagName: "li",

    render: function () {
      this.$el.html(_.template(entryTemplate, this.model.toJSON()))
      return this
    }
  })

  return module
})
