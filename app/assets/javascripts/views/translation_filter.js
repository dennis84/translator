define([
  "helpers/form",
  "collections/language",
  "text!templates/translation_filter.html"
], function (formHelper, LanguageCollection, translationFilterTemplate) {

  var module = Backbone.View.extend({
    events: {
      "click .apply": "apply",
      "click .cancel": "cancel"
    },

    initialize: function () {
      this.languages = new LanguageCollection
      this.languages.on("reset", this.ready, this)
      this.languages.fetch()
    },

    ready: function () {
      this.trigger("ready", this)
    },

    render: function () {
      var languages = {}
      _.each(this.languages.toJSON(), function (lang) {
        languages[lang.code] = lang.name
      })

      var data = _.extend(this.model.toJSON(), {
        "formHelper": formHelper,
        "languages": languages
      })

      this.$el.html(_.template(translationFilterTemplate, data))
      return this
    },

    apply: function (e) {
      e.preventDefault()
      var data = this.$el.find("form").serializeObject()
      this.model.set(data)
      this.trigger("close", this)
    },

    cancel: function (e) {
      e.preventDefault()
      this.trigger("close", this)
    }
  })

  return module
})
