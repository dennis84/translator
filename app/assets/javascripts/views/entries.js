define([
  "models/entry",
  "views/entry",
  "views/entry_filter",
  "views/entry_edit",
  "text!templates/entries.html"
], function (Entry, EntryView, EntryFilterView, EntryEditView, entriesTemplate) {

  var module = Backbone.View.extend({
    id: "entries",

    events: {
      "click .create": "create",
      "click .remove": "remove",
      "click #clear-filter": "clearFilter",
      "keyup .search": "search"
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
        var button = this
          , filter = new EntryFilterView({ model: view.collection.filter })

        filter.on("close", function (view) {
          $(button).popover("destroy")
        })

        $(button).popover("toggle")
        filter.on("ready", function () {
          $(".popover-container").html(filter.render().el)
          $("select").chosen()
        })
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
      this.$("#entry-list").append(view.render().el)
    },

    create: function (e) {
      e.preventDefault()
      var entryEdit = new EntryEditView({ model: new Entry, collection: this.collection })
      window.app.removePane(1)
      window.app.addPane(entryEdit.render().el, "entry-edit", "spaceless6")
    },

    remove: function (e) {
      e.preventDefault()
      var view = this
      this.$(".check input:checked").each(function (i, el) {
        var entry = $(el).closest("tr")
        var model = view.collection.get(entry.attr("data-id"))
        view.collection.remove(model)
        model.destroy()
        $(entry).remove()
      })
    },

    clearFilter: function (e) {
      this.collection.filter.set(this.collection.filter.defaults)
    },

    search: function (e) {
      e.preventDefault()
      var term = $(e.currentTarget).val()
      if (term.length > 0) {
        this.collection.search(term)
      } else {
        this.collection.fetch()
      }
    }
  })

  return module
})
