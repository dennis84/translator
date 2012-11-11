define([
  "models/entry",
  "models/entry_filter"
], function (Entry, EntryFilter) {

  var module = Backbone.Collection.extend({
    model: Entry,

    initialize: function () {
      this.filter = new EntryFilter
    },

    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/entries" 
    }
  })

  return module
})
