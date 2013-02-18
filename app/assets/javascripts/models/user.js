define([], function () {

  var module = Backbone.Model.extend({
    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      if (true === this.isNew()) {
        return "/" + window.project.id + "/users"
      }

      return "/" + window.project.id + "/users/" + this.id
    },
    
    update: function () {
      var model = this
        , clone = this.clone()

      clone.url = function () {
        return "/user"
      }

      clone.save()
      clone.on("sync", function (m) {
        m.off("sync")
        model.set("password", "")
      }, this)
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
        model.trigger("sync", model)
      })
    },

    login: function () {
      var model = this
      $.postJSON("/authenticate", this.toJSON(), function (data) {
        window.authenticated = true
        model.set("password", "")
        model.set("password2", "")
        model.trigger("logged_in", model)
      }).error(function (response) {
        model.trigger("error", model, response)
      })
    },

    signUp: function (data) {
      var model = this
      $.postJSON("/signup", data, function (data) {
        window.authenticated = true
        model.set("password", "")
        model.set("password2", "")
        model.trigger("logged_in", model)
      }).error(function (response) {
        model.trigger("error", model, response)
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

    isAdmin: function () {
      return _.include(this.get("roles"), "ROLE_ADMIN")
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
