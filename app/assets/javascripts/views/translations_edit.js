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
      var data = this.$el.find("form").serializeObject()
        , collection = this.collection

      _.each(data.translations, function (item, id) {
        var translation = collection.get(id)
        if (true === translation.hasChanged(item)) {
          if (window.user.isAdmin()) {
            translation.set(item)
            translation.save()
          } else {
            var index = collection.indexOf(translation) + 1
            var clone = translation.clone()
            clone.set(item)
            collection.create(clone, { wait: true, at: index })
          }
        }
      })
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
