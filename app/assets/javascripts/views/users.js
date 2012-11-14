define([
  "models/user",
  "views/user",
  "text!templates/users.html"
], function (User, UserView, usersTemplate) {

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
      var model = new User
        , view  = this

      window.app.removePane(1)
      window.userController.edit(model)
      model.on("sync", function () {
        view.collection.add(model)
      }, this)
    },

    addUser: function (e) {
      e.preventDefault()
      window.app.removePane(1)
      window.userController.add()
    }
  })

  return module
})
