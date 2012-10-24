define([
  "models/entry"
], function (Entry) {

  var module = Backbone.Collection.extend({
    model: Entry,

    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/entries" 
    }
  })

  return module
})
