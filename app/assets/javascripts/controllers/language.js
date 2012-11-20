define([
  "collections/language",
  "views/languages",
  "views/language_edit"
], function (LanguageCollection, LanguagesView, LanguagesEditView) {

  var module = Backbone.Controller.extend({
    list: function () {
      var coll = new LanguageCollection
      var view = new LanguagesView({ collection: coll })
      coll.fetch()
    },

    edit: function (model) {
      var view = new LanguagesEditView({ model: model })
      view.render()
    }
  })

  return module
})
