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

    activate: function () {
      var model = this
      $.postJSON(this.url() + "/activate", {}, function (data) {
        model.set(data)
      })
    },

    clone: function() {
      var clone = new this.constructor(this.attributes)
      clone.entry = this.entry
      clone.id = null

      return clone
    },

    hasChanged: function (diff) {
      return false === _.isEqual({
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
