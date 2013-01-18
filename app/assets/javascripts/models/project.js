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
      progress: "",
      nb_words: "",
      open: "",
      repo: ""
    }
  })

  return module
})
