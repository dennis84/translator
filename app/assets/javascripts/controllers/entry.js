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
    },

    edit: function (model) {
      var view = new EntryEditView({ model: model })
      view.render()
    },

    create: function () {
    }
  }

  return module
})
