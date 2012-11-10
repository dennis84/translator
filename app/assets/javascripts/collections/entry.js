define([
  "models/entry",
  "models/entry_filter"
], function (Entry, EntryFilter) {

  var module = Backbone.Collection.extend({
    model: Entry,

    initialize: function () {
      this.filter = new EntryFilter
      this.filter.on("change", this.updateFilter, this)
    },

    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/entries" 
    },

    updateFilter: function (filter) {
      console.log(filter.toJSON())
    }
  })

  return module
})
