define([
  "helpers/form",
  "collections/language",
  "text!templates/entry_filter.html"
], function (formHelper, LanguageCollection, entryFilterTemplate) {

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

      this.$el.html(_.template(entryFilterTemplate, data ))
      return this
    },

    apply: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.trigger("close", this)
    },

    cancel: function (e) {
      e.preventDefault()
      this.trigger("close", this)
    }
  })

  return module
})
