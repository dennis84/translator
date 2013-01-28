define([
  "views/user_edit",
  "text!templates/user.html"
], function (UserEditView, userTemplate) {

  var module = Backbone.View.extend({
    tagName: "tr",

    events: {
      "click td:not(.check)": "open"
    },

    initialize: function () {
      this.model.on("change", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(userTemplate, this.model.toJSON()))
      return this
    },

    open: function (e) {
      e.preventDefault()
      var userEdit = new UserEditView({
        model: this.model,
        collection: this.collection
      })
      userEdit.render()
    }
  })

  return module
})
