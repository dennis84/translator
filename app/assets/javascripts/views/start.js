define([
  "text!templates/user_login.html",
  "text!templates/user_new.html"
], function (loginTemplate, userNewTemplate) {

  var module = Backbone.View.extend({
    id: "start",

    events: {
      "click .sign-in": "signIn",
      "click .sign-up": "signUp",
      "click .show-sign-up": "showSignUp",
      "click .show-sign-in": "showSignIn"
    },

    render: function (type) {
      var template = ("sign_in" === type) ? loginTemplate : userNewTemplate

      this.$el.html(_.template(template, this.model.toJSON()))
      $("body").html(this.el)
      this.$("input:text:visible:first").focus()
    },

    signIn: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.model.login()
    },

    signUp: function (e) {
      e.preventDefault()
      var data = this.$el.find("form").serializeObject()
        , model = this.model

      $.postJSON("/sign-up", data, function (data) {
        window.authenticated = true
        model.set("password", "")
        model.set("password2", "")
        model.trigger("logged_in", model)
      })
    },

    showSignUp: function (e) {
      e.preventDefault()
      this.render("sign_up")
      this.delegateEvents()
    },

    showSignIn: function (e) {
      e.preventDefault()
      this.render("sign_in")
      this.delegateEvents()
    }
  })

  return module
})
