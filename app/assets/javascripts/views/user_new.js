define([
  "text!templates/user_new.html"
], function (userNewTemplate) {

  var module = Backbone.View.extend({
    id: "user-new",

    events: {
      "submit form": "register"
    },

    render: function () {
      this.$el.html(_.template(userNewTemplate, this.model.toJSON()))
      $("body").html(this.el)
      this.$("input:text:visible:first").focus()
    },

    register: function (e) {
      e.preventDefault()
      this.model.set(this.$el.find("form").serializeObject())
      console.log("save")
    }
  })

  return module
})
