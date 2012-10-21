define([
  "text!templates/project.html"    
], function (projectTemplate) {

  var module = Backbone.View.extend({
    render: function () {
      this.$el.html(_.template(projectTemplate, this.model.toJSON()))
      return this
    }
  })

  return module
})
