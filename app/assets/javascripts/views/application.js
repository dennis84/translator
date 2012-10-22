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
    },

    addPane: function (html) {
      var pane = $(html).appendTo(this.$el).addClass("pane")
    },

    removePanes: function () {
      this.$el.find(".pane").remove()
    }
  })

  return module
})
