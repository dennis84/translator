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
        , languages = new LanguageCollection
        , languagesReady = false
        , translationsReady = false
        , handle = function () {
            if (!translationsReady || !languagesReady) {
              return
            }

            var langCodes = _.map(languages.models, function (lang) {
                  return lang.get("code")
                })
              , transCodes = _.map(collection.models, function (trans) {
                  return trans.get("code")
                })

            _.map(_.difference(langCodes, transCodes), function (code, index) {
              var trans = new Translation({ "code": code, "active": true })
              trans.cid = "fixed-" + index
              collection.add(trans)
            })

            collection.trigger("fetched_fixed", collection)
            collection.off("reset", null, collection)
            languages.off("reset", null, collection)
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

