// staff-dashboard.js

// Toggle expired filter: only one of Month / Year may be filled at a time
function toggleExpInput(type) {
  if (type === 'm') {
    document.getElementById('yInput').disabled = document.getElementById('mInput').value !== '';
  }
  if (type === 'y') {
    document.getElementById('mInput').disabled = document.getElementById('yInput').value !== '';
  }
}

// Auto-dismiss toast notification after 3 seconds
setTimeout(function () {
  var t = document.getElementById('toast');
  if (t) t.style.display = 'none';
}, 3000);
