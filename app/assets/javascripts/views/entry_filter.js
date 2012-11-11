define([
  "text!templates/entry_filter.html"
], function (entryFilterTemplate) {

  var module = Backbone.View.extend({
    events: {
      "change input[data-config]": "toggleConfig",
      "click .apply": "apply",
      "click .cancel": "cancel"
    },

    render: function () {
      this.$el.html(_.template(entryFilterTemplate, {}))
      return this
    },

    toggleConfig: function (e) {
      e.preventDefault()
      var config = $(e.currentTarget).attr("data-config")
      $(config).toggle()
    },

    apply: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.trigger("close", this)
    },

    cancel: function (e) {
      e.preventDefault()
      this.trigger("close", this)
    }
  })

  return module
})
