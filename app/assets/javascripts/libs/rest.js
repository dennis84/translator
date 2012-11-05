!(function () {
  function jsonRequest(method, url, data, success) {
    return $.ajax({
      "type": method,
      "url": url,
      "data": data,
      "success": success
    })
  }

  $.postJSON = function (url, data, success) {
    return jsonRequest("POST", url, data, success)
  }

  $.putJSON = function (url, data, success) {
    return jsonRequest("PUT", url, success)
  }

  $.deleteJSON = function (url, success) {
    return jsonRequest("DELETE", url, {}, success)
  }
})(jQuery)
