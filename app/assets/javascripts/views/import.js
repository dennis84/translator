define([
  "text!templates/import.html"
], function (importTemplate) {

  var module = Backbone.View.extend({
    id: "import",

    render: function () {
      this.$el.html(_.template(importTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el, "import", "spaceless4")
    }
  })

  return module
})
