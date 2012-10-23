define([
  "models/entry"
], function (Entry) {

  var module = Backbone.Collection.extend({
    model: Entry,

    project: null,

    url: function () {
      if (null === this.project) {
        throw new Error("The collection needs a project.")
      }

      return "/" + this.project + "/entries" 
    },

    fetchByProject: function (project, options) {
      this.project = project
      this.fetch(options)
    }
  })

  return module
})
