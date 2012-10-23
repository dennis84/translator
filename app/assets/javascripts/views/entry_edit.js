define([
  "text!templates/entry_edit.html"
], function (entryEditTemplate) {

  var module = Backbone.View.extend({
    id: "entry-edit",
    className: "entry-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    render: function () {
      var data = this.model.toJSON()
      this.$el.html(_.template(entryEditTemplate, data))
      $("#entry-edit").remove()
      window.app.addPane(this.el, "entry-edit", "spaceless6")
    },

    save: function (e) {
      e.preventDefault()
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
