define([
  "collections/user",
  "views/login",
  "views/users",
  "views/user_edit",
  "views/user_add"
], function (UserCollection, LoginView, UsersView, UserEditView, UserAddView) {

  var module = {
    login: function () {
      var view = new LoginView({ model: window.user })
      view.render()
    },

    list: function () {
      var coll = new UserCollection
      var view = new UsersView({ collection: coll })
      coll.fetch()
    },

    edit: function (model) {
      var view = new UserEditView({ model: model })
      view.render()
    },

    add: function () {
      var view = new UserAddView
      view.render()
    }
  }

  return module
})
