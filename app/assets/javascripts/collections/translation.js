define([
  "models/translation"
], function (Translation) {

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

    url: function () {
      if (undefined === this.entry) {
        throw new Error("There must be an entry set.")
      }

      return "/" + this.entry.id + "/translations"
    }
  })

  return module
})

