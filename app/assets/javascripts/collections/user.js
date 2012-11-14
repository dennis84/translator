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
    }
  })

  return module
})
