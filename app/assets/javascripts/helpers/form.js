define([], function () {

  var module = {
    options: function (options, values) {
      var output = ""
      _.each(options, function (label, value) {
        var selected = (true === _.include(values, value)) ? "selected='selected'" : ""
        output += "<option value='" + value + "' " + selected + ">" + label + "</option>"
      })

      return output
    }
  }

  return module
})
