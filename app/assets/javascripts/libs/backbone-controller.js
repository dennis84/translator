var Controller = Backbone.Controller = function(options) {
  this.initialize.apply(this, arguments)
}

_.extend(Controller.prototype, Backbone.Events, {
  initialize: function () {}
})

var ctor = function() {}

var extend = function (protoProps, classProps) {
  var child = inherits(this, protoProps, classProps)
  child.extend = this.extend
  return child
}

var inherits = function(parent, protoProps, staticProps) {
  var child

  if (protoProps && protoProps.hasOwnProperty('constructor')) {
    child = protoProps.constructor
  } else {
    child = function() { parent.apply(this, arguments) }
  }
  _.extend(child, parent)
  ctor.prototype = parent.prototype
  child.prototype = new ctor()
  if (protoProps) _.extend(child.prototype, protoProps)
  if (staticProps) _.extend(child, staticProps)
  child.prototype.constructor = child
  child.__super__ = parent.prototype

  return child
}

Controller.extend = extend
