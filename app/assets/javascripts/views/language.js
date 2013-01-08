define([
  "views/language_edit",
  "text!templates/language.html"
], function (LanguageEditView, languageTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    events: {
      "click td:not(.check)": "open"
    },

    initialize: function () {
      this.model.on("change", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(languageTemplate, this.model.toJSON()))
      this.$el.attr("data-id", this.model.id)
      return this
    },

    open: function (e) {
      e.preventDefault()
      var languageEdit = new LanguageEditView({
        model: this.model,
        collection: this.collection
      })

      window.app.removePane(1)
      window.app.addPane(languageEdit.render().el, "language-edit", "spaceless6")
    }
  })

  return module
})
