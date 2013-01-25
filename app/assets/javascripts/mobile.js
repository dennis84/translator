var hammer = new Hammer($("body").get(0))
  , pos = 0

hammer.onswipe = (function (e) {
  if ("left" === e.direction) {
    pos -= 100;
  } else {
    pos += 100;
  }
  
  if (pos < -200) pos = -200
  if (pos > 0) pos = 0

  $("#application").css("margin-left", pos + "%")
})
