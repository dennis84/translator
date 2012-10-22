define([
  "models/entry"
], function (Entry) {

  var module = Backbone.Collection.extend({
    model: Entry,

    url: function () {
      return "/entries"
    }
  })

  return module
})
