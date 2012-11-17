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

    hasChanged: function (diff) {
      return false === _.isEqual({
        code: this.get("code"),
        text: this.get("text")
      }, diff)
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
