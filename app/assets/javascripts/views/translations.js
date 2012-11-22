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
      this.collection.on("fetched_fixed", this.reset, this)
      this.render()
    },

    render: function () {
      this.$el.html(_.template(translationsTemplate, {}))
      window.app.removePane(1)
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
      var view = this
        , collection = this.collection
        , data = this.$el.find("form").serializeObject()

      _.each(data.translations, function (item, id) {
        var translation = collection.get(id)
        if (undefined === translation) {
          translation = collection.getByCid(id)
        }

        if (true === translation.hasChanged(item)) {
          if (window.user.isAdmin()) {
            translation.set(item)
            translation.save()
          } else {
            var clone = translation.clone()
            clone.set(item)
            collection.create(clone)
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
