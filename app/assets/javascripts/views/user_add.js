define([
  "text!templates/user_add.html"
], function (userAddTemplate) {

  var module = Backbone.View.extend({
    id: "user-add",
    className: "user-add",

    render: function () {
      this.$el.html(_.template(userAddTemplate, {}))
      window.app.addPane(this.el, "user-add", "spaceless6")
    }
  })

  return module
})
