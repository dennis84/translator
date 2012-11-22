define([
  "text!templates/user_edit.html"
], function (userEditTemplate) {

  var module = Backbone.View.extend({
    id: "user-edit",
    className: "user-edit",

    events: {
      "click .save": "save"
    },

    render: function () {
      this.$el.html(_.template(userEditTemplate, {}))
      return this
    },

    save: function (e) {
      e.preventDefault()
      var data = this.$el.find("form").serializeObject()
      if (false === _.isArray(data.roles)) {
        data.roles = [data.roles]
      }

      this.model.set(data)
      this.collection.create(this.model)
    }
  })

  return module
})
