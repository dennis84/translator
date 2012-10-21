define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      return "/users"
    },

    current: function (func) {
      var self = this
      $.getJSON("/user", function (data) {
        self.set(data)
        window.authenticated = true
      }).complete(function () {
        func(self)
      })
    },

    login: function () {
      var self = this
      $.ajax({
        "type": "POST",
        "url": "/authenticate",
        "data": this.toJSON(),
        "dataType": "json",
        "success": function (data) {
          window.authenticated = true
          self.set("password", "")
          self.set("password2", "")
          self.trigger("logged_in", self)
        }
      })
    },

    logout: function () {
      var self = this
      $.ajax({
        "type": "DELETE",
        "url": "/logout",
        "data": this.toJSON(),
        "dataType": "json",
        "success": function (data) {
          window.authenticated = false
          self.set("username", "")
          self.set("password", "")
          self.set("password2", "")
          self.trigger("logged_out", self)
        }
      })
    },

    defaults: {
      id: null,
      username: "",
      password: "",
      password2: ""
    }
  })

  return module
})
