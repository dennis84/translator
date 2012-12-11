define([
  "collections/translation",
  "views/translations_edit",
  "text!templates/translation.html"
], function (Translations, TranslationsEditView, translationTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    events: {
      "click td:not(.check)": "open"
    },

    initialize: function () {
      this.model.on("change", this.render, this)
    },
 
    render: function () {
      this.$el.html(_.template(translationTemplate, this.model.toJSON()))
      return this
    },

    open: function (e) {
      e.preventDefault()
      var coll = new Translations
      var view = new TranslationsEditView({ collection: coll })
      coll.fetchByName(this.model.get("name"))
    }
  })

  return module
})
