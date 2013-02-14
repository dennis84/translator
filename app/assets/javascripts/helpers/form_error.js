define([], function () {

  var Error = function (name, message) {
    this.name = name
    this.message = message
  }

  Error.prototype.render = function (el) {
    var error = "<span class='form-error-message'>" + this.message + "</span>"

    if ("global" === this.name) {
      el.find("form").prepend(error)
    } else {
      el.find("[name=" + this.name + "]").addClass("form-error").after(error)
    }
    
  }

  return Error
})
