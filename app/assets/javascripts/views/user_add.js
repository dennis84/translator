define([
  "text!templates/user_add.html"
], function (userAddTemplate) {

  var module = Backbone.View.extend({
    id: "user-add",
    className: "user-add pane-edit",

    render: function () {
      this.$el.html(_.template(userAddTemplate, {}))
      return this
    }
  })

  return module
})
