define([
  "models/entry",
  "models/entry_filter"
], function (Entry, EntryFilter) {

  var module = Backbone.Collection.extend({
    model: Entry,

    initialize: function () {
      this.filter = new EntryFilter
    },

    url: function (term) {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/entries"
    },

    searchUrl: function (term) {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/search/entries?term=" + term
    },

    search: function (term) {
      var collection = this

      $.getJSON(this.searchUrl(term), function (data) {
        var models = []
        _.each(data, function (item) {
          models.push(new Entry(item))
        })

        if (models.length > 0) {
          collection.reset(models)
        }
      })
    }
  })

  return module
})
