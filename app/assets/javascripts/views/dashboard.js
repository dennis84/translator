define([
  "text!templates/dashboard.html"
], function (dashboardTemplate) {

  var module = Backbone.View.extend({
    render: function () {
      this.$el.html(_.template(dashboardTemplate, {}))
      $("#application").append(this.el)
    }
  })

  return module
})
