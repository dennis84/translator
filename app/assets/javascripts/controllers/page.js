define([
  "views/dashboard"
], function (DashboardView) {

  var module = {
    dashboard: function () {
      var view = new DashboardView
      view.render()
    }
  }

  return module
})
