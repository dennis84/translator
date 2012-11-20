define([
  "models/entry",
  "collections/entry",
  "collections/translation",
  "views/entries",
  "views/translations",
  "views/entry_edit"
], function (Entry, EntryCollection, TranslationCollection, EntriesView, TranslationsView, EntryEditView) {

  var module = Backbone.Controller.extend({
    initialize: function () {
      this.entries = new EntryCollection
      this.translations = new TranslationCollection
    },

    list: function () {
      var view = new EntriesView({ collection: this.entries })
      var controller = this
      this.entries.fetch()

      this.entries.filter.on("change", function () {
        controller.entries.fetch({ data: controller.entries.filter.toJSON() })
      })

      this.translations.on("change", function () {
        controller.entries.fetch()
      })
    },

    create: function () {
      var controller = this
      var model = new Entry
      var view = new EntryEditView({ model: model })
      view.render()

      model.on("sync", function () {
        controller.entries.add(model)
        model.off("sync")
      }, this)
    },

    edit: function (model) {
      this.translations.reset()
      this.translations.entry = model
      var view = new TranslationsView({ collection: this.translations })
      this.translations.fetchFixed()
    }
  })

  return module
})
