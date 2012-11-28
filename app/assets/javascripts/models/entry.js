define([
  "helpers/form_error"
], function (Error) {

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

    validate: function (attrs) {
      if ("" === attrs.name) {
        return new Error("name", "This field must not be empty")
      }
    },

    defaults: {
      "id": null,
      "name": "",
      "description": "",
      "progress": ""
    }
  })

  return module
})
