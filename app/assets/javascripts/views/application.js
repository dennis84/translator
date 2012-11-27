define([
  "views/navigation",
  "text!templates/application.html"
], function (NavigationView, applicationTemplate) {

  var module = Backbone.View.extend({
    id: "application",
    className: "application",

    initialize: function () {
      this.navigation = new NavigationView
    },

    render: function () {
      this.$el.html(_.template(applicationTemplate, {}))
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
    },

    addMessage: function (type, message) {
      $(".top-right").notify({
        message: { text: message },
        type: type
      }).show()
    }
  })

  return module
})
