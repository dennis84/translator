define([
  "text!templates/login.html"
], function (loginTemplate) {

  var module = Backbone.View.extend({
    id: "login",

    events: {
      "submit form": "login"
    },

    render: function () {
      this.$el.html(_.template(loginTemplate, this.model.toJSON()))
      $("body").html(this.el)
      this.$("input:text:visible:first").focus()
    },

    login: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.model.login()
    }
  })

  return module
})
