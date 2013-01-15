define([
  "models/tutorial",
  "text!templates/tutorial.html"
], function (Tutorial, tutorialTemplate) {

  var module = Backbone.View.extend({
    modal: null,

    initialize: function () {
      this.model = new Tutorial
      this.render()
    },

    render: function () {
      this.$el.html(_.template(tutorialTemplate, this.model.toJSON()))
      this.modal = $("<div class='modal'>").html(this.el)
      this.$el.find("input:text:visible:first").focus()
      this.modal.on("hidden", function () {
        $(this).remove()
      })
    },

    show: function () {
      this.modal.modal("show")
    },

    hide: function () {
      this.modal.modal("hide")
    }
  })

  return module
})
