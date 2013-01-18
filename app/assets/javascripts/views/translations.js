define([
  "models/translation",
  "views/translation",
  "views/translation_new",
  "views/translation_filter",
  "text!templates/translations.html"
], function (Translation, TranslationView, TranslationNewView, TranslationFilterView, translationsTemplate) {

  var module = Backbone.View.extend({
    id: "translations",

    events: {
      "click .create": "create",
      "click .remove": "removeTranslation",
      "keyup .search": "search",
      "click #clear-filter": "clearFilter"
    },

    initialize: function () {
      this.collection.on("reset", this.reset, this)
      this.collection.on("add", this.add, this)
      this.render()
    },

    render: function () {
      this.$el.html(_.template(translationsTemplate, {}))
      var view = this

      $("#filter", this.$el).popover({
        "html": true,
        "trigger": "manual"
      }).on("click", function (e) {
        var button = this
          , filter = new TranslationFilterView({ model: view.collection.filter })

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
      window.app.addPane(this, "spaceless4")
    },

    reset: function (e) {
      this.$el.find("#translation-list").html("")
      this.collection.each(this.add, this)

      if (this.collection.filter.isEmpty()) {
        this.$el.find("#clear-filter").hide()
      } else {
        this.$el.find("#clear-filter").show()
      }
    },

    add: function (model) {
      var view = new TranslationView({ model: model })
      this.$("#translation-list").append(view.render().el)
    },

    create: function (e) {
      e.preventDefault()
      var view = new TranslationNewView({ model: new Translation, collection: this.collection })
      window.app.removePane(1)
      window.app.addPane(view.render(), "spaceless6")
    },

    removeTranslation: function (e) {
      e.preventDefault()
      var view = this
      this.$(".check input:checked").each(function (i, el) {
        var translation = $(el).closest("tr")
        var model = view.collection.get(translation.attr("data-id"))
        view.collection.remove(model)
        model.destroy()
        $(translation).remove()
      })
    },

    search: function (e) {
      e.preventDefault()
      var term = $(e.currentTarget).val()
      if (term.length > 0) {
        this.collection.search(term)
      } else {
        this.collection.fetch()
      }
    },

    clearFilter: function (e) {
      this.collection.filter.set(this.collection.filter.defaults)
    }
  })

  return module
})
