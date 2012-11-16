define([
  "text!templates/translation.html"
], function (translationTemplate) {

  var module = Backbone.View.extend({
    className: "translation",

    render: function () {
      this.$el.html(_.template(translationTemplate, this.model.toJSON()))
      return this
    }
  })

  return module
})
