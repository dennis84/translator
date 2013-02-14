define([
  "models/import",
  "collections/language",
  "views/import_result",
  "text!templates/import.html"
], function (ImportModel, LanguageCollection, ImportResultView, importTemplate) {

  var module = Backbone.View.extend({
    id: "import",

    nbProcessed: 0,
    nbSkipped: 0,
    nbInserted: 0,

    events: {
      "click .upload": "upload"
    },

    initialize: function () {
      this.model = new ImportModel
      this.model.translations.on("add", this.add, this)
      this.languages = new LanguageCollection
      this.languages.on("reset", this.render, this)
      this.languages.fetch()
    },

    render: function () {
      this.model.set("languages", this.languages.toJSON())
      this.$el.html(_.template(importTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this, "spaceless10")
    },

    upload: function (e) {
      e.preventDefault()
      this.model.upload(
        document.getElementById("file").files,
        this.$el.find("form").serializeObject())

      window.app.addMessage("success", "Import Success")
    },

    updateStats: function (model) {
      this.nbProcessed ++
      this.$("#nb-processed").html(this.nbProcessed)

      if ("skipped" === model.get("status")) {
        this.nbSkipped ++
        this.$("#nb-skipped").html(this.nbSkipped)
      } else {
        this.nbInserted ++
        this.$("#nb-inserted").html(this.nbInserted)
      }
    },

    add: function(model) {
      this.updateStats(model)
      var view = new ImportResultView({ model: model })
      this.$("#import-result-list").append(view.render().el)
    }
  })

  return module
})
