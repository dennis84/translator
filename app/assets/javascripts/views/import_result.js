define([
  "views/import_result",
  "text!templates/import_result.html"
], function (ImportResultView, importResultTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    render: function () {
      this.$el.html(_.template(importResultTemplate, this.model.toJSON()))
      this.$el.addClass("status-" + this.model.get("status"))
      return this
    }
  })

  return module
})
