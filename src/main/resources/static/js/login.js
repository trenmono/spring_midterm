// login.js

// Switch between Sign In and Register tabs
function switchTab(t) {
  document.getElementById('loginForm').style.display = (t === 'login') ? 'block' : 'none';
  document.getElementById('regForm').style.display  = (t === 'reg')   ? 'block' : 'none';
}

// Auto-dismiss toast notification after 3 seconds
setTimeout(function () {
  var t = document.getElementById('toast');
  if (t) t.style.display = 'none';
}, 3000);
