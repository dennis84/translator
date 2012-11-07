define([
  "text!templates/import.html"
], function (importTemplate) {

  var module = Backbone.View.extend({
    id: "import",

    events: {
      "change #file": "upload"
    },

    render: function () {
      this.$el.html(_.template(importTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el, "import", "spaceless4")
    },

    upload: function (e) {
      e.preventDefault()
      this.model.upload(e.currentTarget.files[0])
    }
  })

  return module
})
