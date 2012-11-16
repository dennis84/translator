define([
  "collections/translation",
  "views/translations",
  "text!templates/entry_edit.html"
], function (TranslationCollection, TranslationsView, entryEditTemplate) {

  var module = Backbone.View.extend({
    id: "entry-edit",
    className: "entry-edit",

    events: {
      "click .save": "save",
      "click .cancel": "cancel",
      "click .accept": "accept",
      "click .reject": "reject"
    },

    initialize: function () {
      
    },

    render: function () {
      this.$el.html(_.template(entryEditTemplate, this.model.toJSON()))
  
      var translationCollection = new TranslationCollection
      this.translationsView = new TranslationsView({
        collection: translationCollection,
        el: this.$("#translation-container")
      })

      translationCollection.entry = this.model
      translationCollection.fetch()
      
      window.app.addPane(this.el, "entry-edit", "spaceless6")
    },

    save: function (e) {
      e.preventDefault()
      var data = this.$el.find("form").serializeObject()
        , changes = this.model.changedTranslations(data)

      if (false !== changes) {
        this.model.set("translations", changes)
        this.model.save()
        window.app.addMessage("success", "Saved")
      }
    },

    cancel: function (e) {
      e.preventDefault()
      this.$el.remove()
    },

    accept: function (e) {
      e.preventDefault()
      console.log("accept")
    },

    reject: function (e) {
      e.preventDefault()
      var id = $(e.currentTarget).closest(".translation-request").attr("data-id")
      var translations = _.filter(this.model.get("translations"), function (translation) {
        return translation.id !== id
      })

      this.model.set("translations", translations)
    }
  })

  return module
})
