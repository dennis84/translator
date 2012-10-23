define([
  "models/language"
], function (Language) {

  var module = Backbone.Collection.extend({
    model: Language,

    project: null,

    url: function () {
      if (null === this.project) {
        throw new Error("The collection needs a project.")
      }

      return "/" + this.project + "/languages" 
    },

    fetchByProject: function (project, options) {
      this.project = project
      this.fetch(options)
    }
  })

  return module
})
