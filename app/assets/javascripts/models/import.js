define([], function () {

  var module = Backbone.Model.extend({
    upload: function (file) {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      var reader = new FileReader
      reader.onloadend = function () {
        $.postJSON("/" + window.project.id + "/import", { "content" : reader.result, "language": "de" }, function (data) {
          console.log(data)
        })
      }
    
      reader.readAsText(file)
    }
  })

  return module
})
