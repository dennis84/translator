define([
  "text!templates/sync.html"
], function (syncTemplate) {

  var module = Backbone.View.extend({
    render: function () {
      this.$el.html(_.template(syncTemplate, {}))
      window.app.removePanes()
      window.app.addPane(this.el, "sync", "spaceless10")
    }
  })

  return module
})
