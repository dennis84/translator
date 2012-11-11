define([
  "collections/entry",
  "views/entries",
  "views/entry_edit"
], function (EntryCollection, EntriesView, EntryEditView) {

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
      var view = new EntryEditView({ model: model })
      view.render()
    }
  }

  return module
})
