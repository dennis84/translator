define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      if (true === this.isNew()) {
        return "/" + window.project.id + "/languages"
      }

      return "/" + window.project.id + "/languages/" + this.id
    },

    defaults: {
      "id": null,
      "code": "",
      "name": ""
    }
  })

  return module
})
