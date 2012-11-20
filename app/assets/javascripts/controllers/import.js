define([
  "models/import",
  "views/import"
], function (ImportModel, ImportView) {

  var module = Backbone.Controller.extend({
    show: function () {
      var model = new ImportModel
      var view = new ImportView({ model: model })
    }
  })

  return module
})
