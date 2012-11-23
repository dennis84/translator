define([
  "views/translations",
  "text!templates/entry.html"
], function (TranslationsView, entryTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    events: {
      "click td:not(.check)": "open"
    },

    initialize: function () {
      this.model.on("change", this.render, this)
      window.translations.on("sync", this.refresh, this)
    },
    
    refresh: function (model) {
      if (model.entry.id !== this.model.id) {
        return
      }

      this.model.fetch()
      this.model.on("sync", function () {
        $("#entry-list [data-id=" + this.model.id + "]").replaceWith(this.render().el)
      }, this)
    },

    render: function () {
      this.$el.html(_.template(entryTemplate, this.model.toJSON()))
      this.$el.attr("data-id", this.model.id)
      return this
    },

    open: function (e) {
      e.preventDefault()
      window.translations.entry = this.model
      window.translations.reset()
      var view = new TranslationsView({ collection: window.translations })
      window.translations.fetchFixed()
    }
  })

  return module
})
