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
      "click .add": "addUser",
      "click .remove": "removeUser"
    },

    initialize: function () {
      this.collection.on("reset", this.render, this)
      this.collection.on("add", this.add, this)
    },

    render: function () {
      this.$el.html(_.template(usersTemplate, {}))
      this.collection.each(this.add, this)
      window.app.removePanes()
      window.app.addPane(this, "spaceless4")
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
      userEdit.render()
    },

    addUser: function (e) {
      e.preventDefault()
      var userAdd = new UserAddView({
        model: new User,
        collection: this.collection
      })
      userAdd.render()
    },

    removeUser: function (e) {
      e.preventDefault()
      var view = this
      this.$(".check input:checked").each(function (i, el) {
        var user = $(el).closest("tr")
        var model = view.collection.get(user.attr("data-id"))

        model.on("sync", function () {
          model.off("sync")
          view.collection.remove(model)
          $(user).remove()
        })

        model.destroy()
      })
    }
  })

  return module
})
