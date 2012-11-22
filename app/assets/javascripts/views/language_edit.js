define([
  "text!templates/language_edit.html"
], function (languageEditTemplate) {

  var module = Backbone.View.extend({
    id: "language-edit",
    className: "language-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    render: function () {
      this.$el.html(_.template(languageEditTemplate, this.model.toJSON()))
      return this
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.collection.create(this.model)
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
