define([
  "models/user",
  "views/user",
  "views/user_edit",
  "views/user_add",
  "text!templates/users.html"
], function (User, UserView, UserEditView, UserAddView, usersTemplate) {

  var module = Backbone.View.extend({
    id: "users",

    events: {
      "click .create": "create",
      "click .add": "addUser"
    },

    initialize: function () {
      this.collection.on("reset", this.render, this)
      this.collection.on("add", this.add, this)
    },

    render: function () {
      this.$el.html(_.template(usersTemplate, {}))
      this.collection.each(this.add, this)
      window.app.removePanes()
      window.app.addPane(this.el, "users", "spaceless4")
    },

    add: function (model) {
      var view = new UserView({ model: model })
      this.$el.find("#user-list").append(view.render().el)
    },

    create: function (e) {
      e.preventDefault()
      var userEdit = new UserEditView({
        model: new User,
        collection: this.collection
      })

      window.app.removePane(1)
      window.app.addPane(userEdit.render().el, "user-create", "spaceless6")
    },

    addUser: function (e) {
      e.preventDefault()
      var userAdd = new UserAddView({
        model: new User,
        collection: this.collection
      })

      window.app.removePane(1)
      window.app.addPane(userAdd.render().el, "user-add", "spaceless6")
    }
  })

  return module
})
