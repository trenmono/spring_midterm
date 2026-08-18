// cart.js

// Auto-dismiss toast notification after 3 seconds
setTimeout(function () {
  var t = document.getElementById('toast');
  if (t) t.style.display = 'none';
}, 3000);
