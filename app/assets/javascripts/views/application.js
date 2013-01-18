define([
  "helpers/form_error",
  "views/navigation",
  "text!templates/application.html"
], function (Error, NavigationView, applicationTemplate) {

  var module = Backbone.View.extend({
    id: "application",
    className: "application",
    panes: [],

    initialize: function () {
      this.navigation = new NavigationView
    },

    render: function () {
      this.$el.html(_.template(applicationTemplate, {}))
      $("body").html(this.el)
      this.navigation.render()
    },

    renderErrors: function (view, response) {
      view.$(".form-error-message").remove()
      this.$(".form-error").removeClass(".form-error")

      _.each(JSON.parse(response.responseText), function (error) {
        new Error(error.name, error.message).render(view.$el)
      })

      view.model.off("sync")
    },

    addPane: function (view, classes) {
      var pane = $(view.el).appendTo(this.$el)
        .addClass("pane")
        .addClass(classes)

      this.panes.push(view)
    },

    removePane: function (n) {
      var view = _.pick(this.panes, n)
      if (false === _.isUndefined(view)) {
        _.drop(this.panes, n)
        this.removeView(view)
      }
    },

    removePanes: function () {
      _.each(this.panes, this.removeView, this)
      this.panes = []
    },

    removeView: function (view) {
      view.undelegateEvents()
      view.$el.removeData().unbind()
      view.remove()
      Backbone.View.prototype.remove.call(view)
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
