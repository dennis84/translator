define([
  "views/translation_edit",
  "text!templates/translations_edit.html"
], function (TranslationEditView, translationsEditTemplate) {

  var module = Backbone.View.extend({
    id: "translations-edit",
    className: "translations-edit pane-edit",

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
      window.app.addPane(this, "spaceless6")
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
            translation.save(item)
            translation.on("sync", function (model) {
              model.off("sync")
              collection.trigger("update", translation)
            })
            var successMessage = "Translation Saved"
          } else {
            var index = collection.indexOf(translation) + 1
            var clone = translation.clone()
            clone.set(item)
            clone.set("status", "empty")
            collection.create(clone, { wait: true, at: index })
            var successMessage = "Translation Suggestion Saved"
          }

          window.app.addMessage("success", successMessage)
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
