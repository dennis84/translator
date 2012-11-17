define([
  "views/translation",
  "text!templates/translations.html"
], function (TranslationView, translationsTemplate) {

  var module = Backbone.View.extend({
    id: "translations",
    className: "translations",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.collection.on("reset", this.reset, this)
      this.render()
    },

    render: function () {
      this.$el.html(_.template(translationsTemplate, {}))
      window.app.addPane(this.el, "translations", "spaceless6")
    },

    reset: function () {
      this.collection.each(this.add, this)
    },

    add: function (model) {
      var view = new TranslationView({ model: model })
      this.$("#translation-list").append(view.render().el)
    },

    save: function (e) {
      e.preventDefault()
      var collection = this.collection
      var data = this.$el.find("form").serializeObject()
        _.each(data.translations, function (item, id) {
        var translation = collection.get(id)
        if (true === translation.hasChanged(item)) {
          translation.set(item)
          translation.save()
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
