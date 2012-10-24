define([
  "models/entry",
  "views/entry",
  "text!templates/entries.html"
], function (Entry, EntryView, entriesTemplate) {

  var module = Backbone.View.extend({
    id: "entries",

    events: {
      "click .create": "create"
    },

    initialize: function () {
      this.collection.on("reset", this.render, this)
      this.collection.on("add", this.add, this)
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
      var view = this
        , model = new Entry()

      model.on("filled", function () {
        window.app.removePane(1)
        window.entryController.edit(model)
      })
      model.fillLanguages()
      model.on("sync", function (model) {
        view.collection.add(model)
        model.off("sync")
      }, this)
    }
  })

  return module
})
