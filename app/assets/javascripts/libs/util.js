_.pick = function (obj, n) {
  var count = 0
  for (var i in obj) {
    if (count++ === n) {
      return obj[i]
    }
  }
}

_.drop = function (obj, n) {
  var count = 0
  for (var i in obj) {
    if (count++ === n) {
      delete obj[i]
      return true
    }
  }

  return false
}
