(function ($) {
  "use strict"

  function maybeManuiplate(value) {
    if ("true" === value) {
      value = true
    } else if ("false" === value) {
      value = false
    }

    return value
  }

  $.fn.serializeObject = function () {
    $("input:checkbox:checked", this).each(function() {
      this.value = "true"
    })

    $("input:checkbox:not(:checked)", this).each(function() {
      this.checked = true
      this.value = "false"
    })

    var arr = _.reduce(this.serializeArray(), function (memo, field) {
      if (_.has(memo, field.name)) {
        var value = memo[field.name]

        if (false === _.isArray(value)) {
          var value = [value]
        }

        value.push(maybeManuiplate(field.value))
        memo[field.name] = value
      } else {
        memo[field.name] = maybeManuiplate(field.value)
      }

      return memo
    }, {})

    return _.reduce(arr, function (memo, value, key) {
      var objField = _.reduceRight(
        key.replace(/\[/g, ".").replace(/\]/g, "").split("."),
        function (memo, p) {
          if (/^[0-9]+$/.test(p)) {
            var n = []
          } else {
            var n = {}
          }

          n[p] = memo
          return n
        }, value)

      $.extend(true, memo, objField)
      return memo
    }, {})
  }
})(jQuery);
