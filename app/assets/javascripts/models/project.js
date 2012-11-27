define([
  "helpers/form_error"
], function (Error) {

  var module = Backbone.Model.extend({
    url: function () {
      return "/projects"
    },

    validate: function (attrs) {
      if ("" === attrs.name) {
        return new Error("name", "This field must not be empty.")
      }
    },

    defaults: {
      id: null,
      name: "",
      admin: {},
      statistics: {}
    }
  })

  return module
})
