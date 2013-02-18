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
      this.$el.attr("data-id", this.model.id)
      return this
    },

    open: function (e) {
      e.preventDefault()
      var coll = new Translations
      var trans = new TranslationsEditView({ collection: coll })
      var view = this

      var refresh = function () {
        view.model.on("sync", function (model) {
          model.off("sync")
          $("#translation-list [data-id=" + view.model.id + "]").replaceWith(view.render().el)
        })

        view.model.fetch()
        coll.fetchByName(view.model.get("name"))
      }

      coll.fetchByName(this.model.get("name"))
      coll.on("update destroy", refresh, this)
    }
  })

  return module
})
