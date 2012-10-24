define([
  "collections/language"
], function (LanguageCollection) {

  var module = Backbone.Model.extend({
    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      if (true === this.isNew()) {
        return "/" + window.project.id + "/entries"
      }

      return "/" + window.project.id + "/entries/" + this.id
    },

    fillLanguages: function () {
      var model = this
        , languages = new LanguageCollection
        , translations = []

      languages.on("reset", function () {
        languages.each(function (language) {
          translations.push({
            "code": language.get("code"),
            "text": "",
            "author": "",
            "active": true          
          })
        })

        model.trigger("filled", model)
      })

      languages.fetch()
      this.set("translations", translations)
      return this
    },

    defaults: {
      "id": null,
      "name": "",
      "description": "",
      "translations": []
    }
  })

  return module
})
