define([
  "views/login"
], function (LoginView) {

  var module = {
    login: function () {
      var view = new LoginView({ model: window.user })
      view.render()
    }
  }

  return module
})
