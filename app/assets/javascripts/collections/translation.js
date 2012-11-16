define([
  "models/translation"
], function (Translation) {

  var module = Backbone.Collection.extend({
    model: Translation,

    url: function () {
      if (undefined === this.entry) {
        throw new Error("There must be an entry set.")
      }

      return "/" + this.entry.id + "/translations"
    }
  })

  return module
})

