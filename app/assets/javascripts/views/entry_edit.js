define([
  "text!templates/entry_edit.html"
], function (entryEditTemplate) {

  var module = Backbone.View.extend({
    id: "entry-edit",
    className: "entry-edit",

    render: function () {
      this.$el.html(_.template(entryEditTemplate, this.model.toJSON()))
      $("#entry-edit").remove()
      window.app.addPane(this.el, 6)
    }
  })

  return module
})
