define([
  "helpers/form_error",
  "text!templates/user_login.html",
  "text!templates/user_signup.html"
], function (Error, loginTemplate, userSignupTemplate) {

  var module = Backbone.View.extend({
    id: "start",

    events: {
      "click .sign-in": "signIn",
      "click .sign-up": "signUp",
      "click .show-sign-up": "showSignUp",
      "click .show-sign-in": "showSignIn"
    },

    initialize: function () {
      this.model.on("error", this.renderErrors, this)
    },

    render: function (type) {
      var template = ("sign_in" === type) ? loginTemplate : userSignupTemplate

      this.$el.html(_.template(template, this.model.toJSON()))
      $("body").html(this.el)
      this.$("input:text:visible:first").focus()
    },

    renderErrors: function (model, response) {
      this.$(".form-error-message").remove()
      this.$(".form-error").removeClass("form-error")
      var view = this

      _.each(JSON.parse(response.responseText), function (error) {
        new Error(error.name, error.message).render(view.$el)
      })

      this.model.off("sync")
    },

    signIn: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.model.login()
    },

    signUp: function (e) {
      e.preventDefault()
      var data = this.$el.find("form").serializeObject()
      this.model.signUp(data)
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
