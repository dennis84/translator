define([
  "models/translation",
  "collections/language"
], function (Translation, LanguageCollection) {

  var module = Backbone.Collection.extend({
    model: Translation,

    initialize: function () {
      this.on("reset", function (collection) {
        collection.each(this.extendModel, this)
      }, this)

      this.on("add", this.extendModel, this)
    },

    extendModel: function (model) {
      model.entry = this.entry
    },

    fetchFixed: function () {
      var collection = this
      var languages = new LanguageCollection
      var languagesReady = false
      var translationsReady = false
      var handle = function () {
        if (translationsReady && languagesReady) {
          var langCodes = _.map(languages.models, function (lang) {
            return lang.get("code")
          })

          var transCodes = _.map(collection.models, function (trans) {
            return trans.get("code")
          })

          _.map(_.difference(langCodes, transCodes), function (code) {
            collection.add(new Translation({ "code": code, "active": true }))
          })

          collection.trigger("fetched_fixed", collection)
        }
      }

      this.on("reset", function () {
        translationsReady = true
        handle()
      }, this)

      languages.on("reset", function () {
        languagesReady = true
        handle()
      }, this)

      this.fetch()
      languages.fetch()
    },

    url: function () {
      if (undefined === this.entry) {
        throw new Error("There must be an entry set.")
      }

      return "/" + this.entry.id + "/translations"
    }
  })

  return module
})

