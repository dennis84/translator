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
      this.$el.html(_.template(entryEditTemplate, this.model.toJSON()))
      window.app.addPane(this.el, "entry-edit", "spaceless6")
    },

    save: function (e) {
      e.preventDefault()
      var data = this.$el.find("form").serializeObject()
        , changes = this.model.changedTranslations(data)

      if (false !== changes) {
        this.model.set("translations", changes)
        this.model.save()
        window.app.addMessage("success", "Saved")
      }
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
