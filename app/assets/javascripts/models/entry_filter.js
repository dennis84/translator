define([], function () {

  var module = Backbone.Model.extend({
    defaults: {
      untranslated: false,
      untranslated_languages: []
    }
  })

  return module
})
