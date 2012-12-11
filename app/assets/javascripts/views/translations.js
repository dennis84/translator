define([
  "models/translation",
  "views/translation",
  "text!templates/translations.html"
], function (Translation, TranslationView, translationsTemplate) {

  var module = Backbone.View.extend({
    id: "translations",

    initialize: function () {
      this.collection.on("reset", this.reset, this)
      this.collection.on("add", this.add, this)
      this.render()
    },

    render: function () {
      this.$el.html(_.template(translationsTemplate, {}))
      window.app.removePanes()
      window.app.addPane(this.el, "translations", "spaceless4")
    },

    reset: function (e) {
      this.$el.find("#translation-list").html("")
      this.collection.each(this.add, this)
    },

    add: function (model) {
      var view = new TranslationView({ model: model })
      this.$("#translation-list").append(view.render().el)
    }
  })

  return module
})
