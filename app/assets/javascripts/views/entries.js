define([
  "models/entry",
  "views/entry",
  "views/entry_filter",
  "text!templates/entries.html"
], function (Entry, EntryView, EntryFilterView, entriesTemplate) {

  var module = Backbone.View.extend({
    id: "entries",

    events: {
      "click .create": "create",
      "click #clear-filter": "clearFilter"
    },

    initialize: function () {
      this.collection.on("reset", this.reset, this)
      this.collection.on("add", this.add, this)
      this.render()
    },

    render: function () {
      var view = this
      this.$el.html(_.template(entriesTemplate, {}))

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

      window.app.removePanes()
      window.app.addPane(this.el, "entries", "spaceless4")
    },

    reset: function (e) {
      this.$el.find("#entry-list").html("")
      this.collection.each(this.add, this)

      if (this.collection.filter.isEmpty()) {
        this.$el.find("#clear-filter").hide()
      } else {
        this.$el.find("#clear-filter").show()
      }
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
    },

    clearFilter: function (e) {
      this.collection.filter.set(this.collection.filter.defaults)
    }
  })

  return module
})
