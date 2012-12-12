define([], function () {

  var module = Backbone.Model.extend({
    isEmpty: function () {
      return _.isEqual(this.attributes, this.defaults)
    },

    defaults: {
      untranslated: false,
      untranslated_languages: [],
      activatable: false
    }
  })

  return module
})
