define([], function () {

  var module = Backbone.Model.extend({
    upload: function (file) {
      if (window.project.isNew()) {
        throw new Error("There must be a current project")
      }

      var form = new FormData
      form.append("file", file)

      $.ajax({
        url: "/" + window.project.id + "/import",
        data: form,
        cache: false,
        processData: false,
        contentType: false,
        type: "POST",
        success: function (data) {
          console.log(data)
          console.log("upload")
        }
      })
    }
  })

  return module
})
