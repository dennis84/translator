
var map = function () {
  emit({ name: this.name }, { count: 1 })
}

var reduce = function(key, values) {
  var result = { count : 0 };
  values.forEach(function(value){
    result.count += value.count;
  })
  return result;
}

var r = db.translations.mapReduce(map, reduce, { out : { "inline": 1 }});
printjson(r)

// r.forEach(function (doc) {
//   print(doc.name)
// })
