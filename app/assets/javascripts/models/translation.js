define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
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

    hasChanged: function (diff) {
      return false === _.isEqual({
        text: this.get("text")
      }, diff)
    },

    isNew: function () {
      return "" == this.id
    },

    defaults: {
      id: "",
      code: "",
      name: "",
      text: "",
      author: "",
      active: false
    }
  })

  return module
})
