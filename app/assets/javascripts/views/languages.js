define([
  "views/language",
  "text!templates/languages.html"
], function (LanguageView, languagesTemplate) {

  var module = Backbone.View.extend({
    id: "languages",

    events: {
      "click .create": "create"
    },

    initialize: function () {
      this.collection.on("reset", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(languagesTemplate, {}))
      this.collection.each(this.add, this)
      window.app.removePanes()
      window.app.addPane(this.el, "languages", "spaceless4")
    },

    add: function (model) {
      var view = new LanguageView({ model: model })
      this.$el.find("#language-list").append(view.render().el)
    },

    create: function (e) {
      e.preventDefault()
      window.app.removePane(1)
      window.languageController.create()
    }
  })

  return module
})
