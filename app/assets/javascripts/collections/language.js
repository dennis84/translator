define([
  "models/language"
], function (Language) {

  var module = Backbone.Collection.extend({
    model: Language,

    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/languages" 
    }
  })

  return module
})
