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

    addPane: function (html, name, classes) {
      var pane = $(html).appendTo(this.$el).addClass("pane").addClass(name + "-pane").addClass(classes)
    },

    removePane: function (nameOrIndex) {
      if (true === _.isNumber(nameOrIndex)) {
        this.$el.find(".pane").eq(nameOrIndex).remove()
      } else if (true === _.isString(nameOrIndex)) {
        this.$el.find("." + nameOrIndex + "-pane").remove()
      }
    },

    removePanes: function () {
      this.$el.find(".pane").remove()
    }
  })

  return module
})
