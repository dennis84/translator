requirejs.config({
  paths: {
    templates: "../templates",
    text:      "libs/text"
  }
})

$.ajaxSetup({
  // Removes the brackets on uri parsing.
  traditional: true
})

_.templateSettings.interpolate = /\{\{(.+?)\}\}/g
_.templateSettings.evaluate = /\{\%(.+?)\%\}/g

require(["application"], function (Application) {
  Application.initialize()
})
