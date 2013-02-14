define([
  "text!templates/dashboard.html"
], function (dashboardTemplate) {

  var module = Backbone.View.extend({
    render: function () {
      this.$el.html(_.template(dashboardTemplate, {}))
      window.app.removePanes()
      window.app.addPane(this, "spaceless10")
    }
  })

  return module
})
