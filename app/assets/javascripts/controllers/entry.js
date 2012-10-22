define([
  "collections/entry",
  "views/entries"
], function (EntryCollection, EntriesView) {

  var module = {
    list: function (project) {
      var coll = new EntryCollection
      var view = new EntriesView({ collection: coll })
      coll.fetch({ data: { "project": project } })
    }
  }

  return module
})
