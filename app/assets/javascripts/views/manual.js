define([
  "text!templates/manual_project.html"
], function (manualTemplate) {

  var module = Backbone.View.extend({
    modal: null,

    events: {
      "click .cancel": "close"
    },

    project: function () {
      this.$el.html(_.template(manualTemplate, {}))
      this.modal = $("<div class='modal'>").html(this.el).modal("show")
      this.modal.on("hidden", function () {
        $(this).remove()
      })
    },

    close: function (e) {
      e.preventDefault()
      this.modal.modal("hide")
    }
  })

  return module
})
