define([
  "models/language",
  "views/language",
  "views/language_edit",
  "text!templates/languages.html"
], function (Language, LanguageView, LanguageEditView, languagesTemplate) {

  var module = Backbone.View.extend({
    id: "languages",

    events: {
      "click .create": "create",
      "click .remove": "removeLanguage"
    },

    initialize: function () {
      this.collection.on("reset", this.render, this)
      this.collection.on("add", this.add, this)
    },

    render: function () {
      this.$el.html(_.template(languagesTemplate, {}))
      this.collection.each(this.add, this)
      window.app.removePanes()
      window.app.addPane(this, "spaceless4")
    },

    add: function (model) {
      var view = new LanguageView({ model: model })
      this.$el.find("#language-list").append(view.render().el)
    },

    create: function (e) {
      e.preventDefault()
      var languageEdit = new LanguageEditView({
        model: new Language,
        collection: this.collection
      })

      window.app.removePane(1)
      window.app.addPane(languageEdit.render(), "spaceless6")
    },

    removeLanguage: function (e) {
      e.preventDefault()
      var view = this
      this.$(".check input:checked").each(function (i, el) {
        var lang = $(el).closest("tr")
        var model = view.collection.get(lang.attr("data-id"))
        view.collection.remove(model)
        model.destroy()
        $(lang).remove()
      })
    }
  })

  return module
})
