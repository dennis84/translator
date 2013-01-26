define([
  "models/user"
], function (User) {

  var module = Backbone.Collection.extend({
    model: User,

    url: function () {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      return "/" + window.project.id + "/users"
    },

    addUser: function(model) {
      var collection = this
      $.postJSON("/" + window.project.id + "/add-user", model.toJSON(), function (data) {
        collection.add(model)
      }).error(function (xhr, b, c, d) {
        model.trigger("error", model, xhr)
      })
    }
  })

  return module
})
