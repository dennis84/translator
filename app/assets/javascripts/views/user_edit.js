define([
  "text!templates/user_edit.html"
], function (userEditTemplate) {

  var module = Backbone.View.extend({
    id: "user-edit",
    className: "user-edit",

    render: function () {
      this.$el.html(_.template(userEditTemplate, {}))
      window.app.addPane(this.el, "user-edit", "spaceless6")
    }
  })

  return module
})
