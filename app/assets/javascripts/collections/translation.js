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

    url: function () {
      if (_.isUndefined(this.entry)) {
        throw new Error("There must be an entry set.")
      }

      return "/" + this.entry.id + "/translations"
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

            _.each(languages.models, function (lang, index) {
                var r = collection.find(function (trans) {
                  return trans.get("code") === lang.get("code") && trans.get("active") === true
                })

                if (_.isUndefined(r)) {
                  var inactive = collection.find(function (trans) {
                    return trans.get("code") === lang.get("code") && trans.get("active") === false
                  })

                  var index = _.isUndefined(inactive) ? collection.length : collection.indexOf(inactive)

                  var trans = new Translation({ "code": lang.get("code"), "active": true })
                  trans.cid = "fixed-" + index
                  collection.add(trans, { at: index })
                }
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
    }
  })

  return module
})

