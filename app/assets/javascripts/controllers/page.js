define([
  "views/dashboard"
], function (DashboardView) {

  var module = Backbone.Controller.extend({
    dashboard: function () {
      var view = new DashboardView
      view.render()
    }
  })

  return module
})
