define([
  "views/navigation"
], function (NavigationView) {

  var module = Backbone.View.extend({
    id: "application",
    className: "application",

    initialize: function () {
      this.navigation = new NavigationView
    },

    render: function () {
      $("body").html(this.el)
      this.navigation.render()
    }
  })

  return module
})
