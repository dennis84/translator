define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      if (true === this.isNew()) {
        return "/" + window.project.id + "/entries"
      }

      return "/" + window.project.id + "/entries/" + this.id
    },

    defaults: {
      "id": null,
      "name": "",
      "description": ""
    }
  })

  return module
})
