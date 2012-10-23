define([
  "views/entry",
  "text!templates/entries.html"
], function (EntryView, entriesTemplate) {

  var module = Backbone.View.extend({
    id: "entries",

    events: {
      "click .create": "create"
    },

    initialize: function () {
      this.collection.on("reset", this.render, this)
    },

    render: function () {
      this.$el.html(_.template(entriesTemplate, {}))
      this.collection.each(this.add, this)
      window.app.removePanes()
      window.app.addPane(this.el, "entries", "spaceless4")
    },

    add: function (model) {
      var view = new EntryView({ model: model })
      this.$el.find("#entry-list").append(view.render().el)
    },

    create: function (e) {
      e.preventDefault()
      window.entryController.create()
    }
  })

  return module
})
