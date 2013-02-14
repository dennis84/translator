define([
  "helpers/form",
  "text!templates/project_edit.html"
], function (formHelper, projectEditTemplate) {

  var module = Backbone.View.extend({
    id: "project-edit",
    className: "project-edit",
    modal: null,

    events: {
      "click .cancel": "cancel",
      "click .save": "save"
    },

    render: function () {
      var data = _.extend(this.model.toJSON(), {
        "formHelper": formHelper
      })

      this.$el.html(_.template(projectEditTemplate, data))
      this.modal = $("<div class='modal'>").html(this.el).modal("show")
      this.modal.on("hidden", function () {
        $(this).remove()
      })
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.model.save()
      window.app.addMessage("success", "Project updated")
    },

    cancel: function (e) {
      e.preventDefault()
      this.modal.modal("hide")
    }
  })

  return module
})
