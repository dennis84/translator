define([
  "models/entry",
  "views/entry",
  "views/entry_filter",
  "text!templates/entries.html"
], function (Entry, EntryView, EntryFilterView, entriesTemplate) {

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

      var view = this

      $("#filter", this.$el).popover({
        "html": true,
        "trigger": "manual"
      }).on("click", function (e) {
        var self = this
          , filter = new EntryFilterView({ model: view.collection.filter })

        filter.on("close", function (view) {
          $(self).popover("destroy")
        })

        $(this).popover("toggle")
        $(".popover-container").html(filter.render().el)
        $("select").chosen()
      })
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
