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
        , file = fileList[0]

      reader.onloadend = function (a, b, c) {
        _.extend(fields, {
          "content": reader.result,
          "type": file.name.substring(file.name.lastIndexOf(".") + 1)
        })

        $.postJSON("/" + window.project.id + "/import", fields, function (data) {
          console.log(data)
        })
      }

      reader.readAsText(file)
    },

    defaults: {
      languages: []
    }
  })

  return module
})
