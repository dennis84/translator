define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (undefined === this.entry) {
        throw new Error("There must be an entry set.")
      }

      if (true === this.isNew()) {
        return "/" + this.entry.id + "/translations"
      }

      return "/" + this.entry.id + "/translations/" + this.id
    },

    defaults: {
      id: null,
      code: "",
      text: "",
      author: "",
      active: false
    }
  })

  return module
})
