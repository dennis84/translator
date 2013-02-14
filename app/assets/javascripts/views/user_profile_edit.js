define([
  "text!templates/user_profile_edit.html"
], function (userProfileEditTemplate) {

  var module = Backbone.View.extend({
    id: "user-profile-edit",
    className: "user-profile-edit pane-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel"
    },

    render: function () {
      this.$el.html(_.template(userProfileEditTemplate, this.model.toJSON()))
      window.app.removePane(1)
      window.app.addPane(this, "spaceless6")
    },

    save: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      this.model.update()
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    }
  })

  return module
})
