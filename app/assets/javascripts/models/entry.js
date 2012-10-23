define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (true === this.isNew()) {
        return "/entries"
      }

      return "/entries/" + this.id
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
