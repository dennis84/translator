define([
  "views/application"
], function (ApplicationView) {

  var module = {
    home: function () {
      var view = new ApplicationView
      view.render()
    }
  }

  return module
})
