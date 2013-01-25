define([], function () {

  var module = Backbone.View.extend({
    isMobile: false,

    initialize: function () {
      var screen = $(window)
        , view = this

      if (screen.width() > 767) {
        this.isMobile = false
      } else {
        this.isMobile = true
        Backbone.history.on("route", this.changeRoute, this)
        window.vent.on("application:ready", this.initializeHammer, this)
      }

      screen.on("resize", function () {
        if (screen.width() > 767) {
          this.isMobile = false
        } else {
          this.isMobile = true
          Backbone.history.on("route", this.changeRoute, this)
          view.initializeHammer()
        }
      })
    },

    initializeHammer: function () {
      var hammer = new Hammer(window.app.el)
        , pos = 0

      hammer.onswipe = (function (e) {
        if ("left" === e.direction) {
          pos -= 100;
        } else {
          pos += 100;
        }
        
        if (pos < -200) pos = -200
        if (pos > 0) pos = 0

        window.app.$el.css("margin-left", pos + "%")
      })
    },

    changeRoute: function () {
      window.app.$el.css("margin-left", "-100%")
    }
  })

  return module
})
