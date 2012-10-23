define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (true === this.isNew()) {
        return "/languages"
      }

      return "/languages/" + this.id
    },

    defaults: {
      "id": null,
      "code": "",
      "name": ""
    }
  })

  return module
})
