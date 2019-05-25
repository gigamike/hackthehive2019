$('#message').keyup(function () {
  var max = 500;
  var len = $(this).val().length;
  if (len >= max) {
    $('#charNum').html(' you have reached the limit');
  } else {
    var char = max - len;
    $('#charNum').html(char + ' characters left');
  }
});
