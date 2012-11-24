define([
  "views/user_profile_edit",
  "text!templates/user_profile.html"
], function (UserProfileEditView, userProfileTemplate) {

  var module = Backbone.View.extend({
    id: "user-profile",
    className: "user-profile",

    events: {
      "click .edit": "edit"
    },

    initialize: function () {
      //this.model.on("change", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(userProfileTemplate, this.model.toJSON()))
      window.app.removePanes()
      window.app.addPane(this.el, "user-profile", "spaceless4")
    },

    edit: function (e) {
      e.preventDefault()
      var view = new UserProfileEditView({ model: this.model })
      view.render()
    }
  })

  return module
})
