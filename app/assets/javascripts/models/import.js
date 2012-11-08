define([], function () {

  var module = Backbone.Model.extend({
    upload: function (fileList, fields) {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      if (fileList.length != 1) {
        throw new Error("You must select one file")
      }

      var reader = new FileReader

      reader.onloadend = function () {
        _.extend(fields, {
          "content": reader.result
        })

        $.postJSON("/" + window.project.id + "/import", fields, function (data) {
          console.log(data)
        })
      }
    
      reader.readAsText(fileList[0])
    }
  })

  return module
})
