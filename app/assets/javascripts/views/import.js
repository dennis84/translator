define([
  "models/import",
  "collections/language",
  "text!templates/import.html"
], function (ImportModel, LanguageCollection, importTemplate) {

  var module = Backbone.View.extend({
    id: "import",

    events: {
      "click .upload": "upload"
    },

    initialize: function () {
      this.model = new ImportModel
      this.languages = new LanguageCollection
      this.languages.on("reset", this.render, this)
      this.languages.fetch()
    },

    render: function () {
      this.model.set("languages", this.languages.toJSON())
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
