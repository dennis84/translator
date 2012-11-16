define([
  "views/translation",
  "text!templates/translations.html"
], function (TranslationView, translationsTemplate) {

  var module = Backbone.View.extend({
    id: "translations",

    initialize: function () {
      this.collection.on("reset", this.reset, this)
    },

    render: function () {
      this.$el.html(_.template(translationsTemplate, {}))
      $(".scollable").append(this.el)
      return this
    },

    reset: function () {
      this.collection.each(this.add, this)
    },

    add: function (model) {
      var view = new TranslationView({ model: model })
      this.$el.append(view.render().el)
    }
  })

  return module
})
