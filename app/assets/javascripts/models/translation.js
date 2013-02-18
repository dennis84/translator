define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (true === this.isNew()) {
        return "/" + window.project.id + "/translations"
      }

      return "/" + window.project.id + "/translations/" + this.id
    },

    activate: function () {
      var model = this
      $.postJSON(this.url() + "/activate", {}, function (data) {
        model.set(data)
        model.trigger("update", model)
        model.collection.trigger("update", model)
      })
    },

    hasChanged: function (diff) {
      return false === _.isEqual({
        text: this.get("text")
      }, diff)
    },

    isNew: function () {
      return "empty" === this.get("status")
    },

    reset: function () {
      this.set(this.defaults)
      this.trigger("reset", this)
      return this
    },

    defaults: {
      id: "",
      code: "",
      name: "",
      text: "",
      author: "",
      status: "empty",
      nb_activatable: null,
      nb_must_activated: null,
      nb_fixed: null,
      progress: null,
      progress_fixed: null
    }
  })

  return module
})
