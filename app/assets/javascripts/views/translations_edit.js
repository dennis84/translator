define([
  "views/translation_edit",
  "text!templates/translations_edit.html"
], function (TranslationEditView, translationsEditTemplate) {

  var module = Backbone.View.extend({
    id: "translations-edit",
    className: "translations-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.collection.on("reset", this.reset, this)
      this.collection.on("add", this.reset, this)
      this.render()
    },

    render: function () {
      this.$el.html(_.template(translationsEditTemplate, {}))
      window.app.removePane(1)
      window.app.addPane(this.el, "translations-edit", "spaceless6")
    },

    reset: function () {
      this.$("#translation-edit-list").html("")
      this.collection.each(this.add, this)
    },

    add: function (model) {
      var view = new TranslationEditView({ model: model })
      this.$("#translation-edit-list").append(view.render().el)
    },

    save: function (e) {
      e.preventDefault()
      console.log(this.$el.find("form").serializeObject())
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
