define([
  "collections/entry",
  "collections/translation",
  "views/entries",
  "views/translations"
], function (EntryCollection, TranslationCollection, EntriesView, TranslationsView) {

  var module = {
    list: function () {
      var coll = new EntryCollection
      var view = new EntriesView({ collection: coll })
      coll.fetch()

      coll.filter.on("change", function () {
        coll.fetch({ data: coll.filter.toJSON() })
      })
    },

    edit: function (model) {
      var coll = new TranslationCollection
      coll.entry = model

      var translations = new TranslationsView({ collection: coll })
      coll.fetchFixed()
    }
  }

  return module
})
