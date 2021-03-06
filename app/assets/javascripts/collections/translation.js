define([
  "models/translation",
  "models/translation_filter",
  "collections/language"
], function (Translation, TranslationFilter, LanguageCollection) {

  var module = Backbone.Collection.extend({
    model: Translation,

    initialize: function () {
      this.filter = new TranslationFilter
    },

    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/translations" 
    },

    searchUrl: function (term) {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/search/translations?term=" + term
    },

    fetchByName: function (name) {
      var collection = this
      $.getJSON(this.url() + "?name=" + name, function (data) {
        var models = _.map(data, function (item) {
          return new Translation(item)
        })

        collection.reset(models)
      })
    },

    search: function (term) {
      var collection = this

      $.getJSON(this.searchUrl(term), function (data) {
        var models = []
        _.each(data, function (item) {
          models.push(new Translation(item))
        })

        if (models.length > 0) {
          collection.reset(models)
        }
      })
    }
  })

  return module
})
