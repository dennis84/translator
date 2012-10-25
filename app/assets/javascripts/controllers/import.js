define([
  "models/import",
  "views/import"
], function (ImportModel, ImportView) {

  var module = {
    show: function () {
      var model = new ImportModel
      var view = new ImportView({ model: model })
      view.render()
    }
  }

  return module
})
