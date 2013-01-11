define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (true === this.isNew()) {
        return "/projects"
      }

      return "/projects/" + this.id
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
