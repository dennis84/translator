define([
  "views/dashboard"
], function (DashboardView) {

  var module = {
    dashboard: function () {
      var obj = Backbone.Object.extend({
      })
      
      console.log(new obj())
      var view = new DashboardView
      view.render()
    }
  }

  return module
})
