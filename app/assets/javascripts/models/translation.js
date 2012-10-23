define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      return "/translations"
    },

    defaults: {
      "id": null,
      "code": "",
      "text": "",
      "author": ""
    }
  })

  return module
})
