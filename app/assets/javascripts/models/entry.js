define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      return "/entries"
    },

    defaults: {
      "id": null,
      "name": "",
      "description": "",
      "translations": []
    }
  })

  return module
})
