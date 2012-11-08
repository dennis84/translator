define([
  "text!templates/import.html"
], function (importTemplate) {

  var module = Backbone.View.extend({
    id: "import",

    events: {
      "click .upload": "upload"
    },

    render: function () {
      this.$el.html(_.template(importTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el, "import", "spaceless4")
    },

    upload: function (e) {
      e.preventDefault()
      this.model.upload(document.getElementById("file").files, this.$el.find("form").serializeObject())
    }
  })

  return module
})
