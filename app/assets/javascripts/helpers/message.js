define([
  "text!templates/helpers/message.html"
], function (messageTemplate) {

  function Message(type, message) {
    this.type = type
    this.message = message
  }

  Message.prototype.render = function () {
    this.el  = _.template(messageTemplate, { "type": this.type, "message": this.message })
    this.$el = $(this.el)

    return this
  }

  return Message
})
