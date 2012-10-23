define([
  "models/language",
  "collections/language",
  "views/languages",
  "views/language_edit"
], function (Language, LanguageCollection, LanguagesView, LanguagesEditView) {

  var module = {
    list: function (project) {
      var coll = new LanguageCollection
      var view = new LanguagesView({ collection: coll })
      coll.fetchByProject(project)
    },

    edit: function (model) {
      var view = new LanguagesEditView({ model: model })
      view.render()
    },

    create: function () {
      var model = new Language
      var view = new LanguagesEditView({ model: model })
      view.render()
    }
  }

  return module
})
