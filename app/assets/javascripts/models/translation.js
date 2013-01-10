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

    reject: function () {
      var model = this
      this.on("sync", function () {
        console.log("destroy")
        model.trigger("update", model)
        model.collection.trigger("update", model)
      })
      this.destroy({ wait: true })
    },

    hasChanged: function (diff) {
      return false === _.isEqual({
        text: this.get("text")
      }, diff)
    },

    isNew: function () {
      return "empty" === this.get("status")
    },

    defaults: {
      id: "",
      code: "",
      name: "",
      text: "",
      author: "",
      status: "empty"
    }
  })

  return module
})
