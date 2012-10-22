define([
  "views/entry",
  "text!templates/entries.html"
], function (EntryView, entriesTemplate) {

  var module = Backbone.View.extend({
    id: "entries",

    initialize: function () {
      this.collection.on("reset", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(entriesTemplate, {}))
      this.collection.each(this.add, this)
      window.app.removePanes()
      window.app.addPane(this.el, 4)
    },

    add: function (model) {
      var view = new EntryView({ model: model })
      this.$el.find("#entry-list").append(view.render().el)
    }
  })

  return module
})
