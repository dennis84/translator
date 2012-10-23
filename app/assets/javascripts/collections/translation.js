define([
  "models/translation"
], function (Translation) {

  var module = Backbone.Collection.extend({
    model: Translation,

    url: function () {
      return "/translation"
    }
  })

  return module
})

