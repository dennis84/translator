define([], function () {

  var module = Backbone.Model.extend({
    upload: function () {
      console.log("upload")
    }
  })

  return module
})
