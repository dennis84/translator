define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      return "/users"
    },

    current: function (func) {
      var model = this
      $.getJSON("/user", function (data) {
        model.set(data)
        window.authenticated = true
      }).complete(function () {
        func(model)
      })
    },

    currentByProject: function (project) {
      var model = this
      $.getJSON("/" + project + "/user", function (data) {
        model.set(data)
      })
    },

    login: function () {
      var model = this
      $.postJSON("/authenticate", this.toJSON(), function (data) {
        window.authenticated = true
        model.set("password", "")
        model.set("password2", "")
        model.trigger("logged_in", model)
      })
    },

    logout: function () {
      var model = this
      $.deleteJSON("/logout", this.toJSON(), function (data) {
        window.authenticated = false
        model.set("username", "")
        model.set("password", "")
        model.set("password2", "")
        model.trigger("logged_out", model)
      })
    },

    equals: function (other) {
      return this.id === other.id
    },

    defaults: {
      id: null,
      username: "",
      password: "",
      password2: "",
      roles: []
    }
  })

  return module
})
