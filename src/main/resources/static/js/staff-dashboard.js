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

// ── Pie Chart for Top Sales ──────────────────────────────────────────────────
(function () {
  var canvas = document.getElementById('staffSalesPieChart');
  if (!canvas || typeof staffCategorySalesData === 'undefined' || !staffCategorySalesData.length) return;

  var palette = [
    '#6366f1', '#f59e0b', '#10b981', '#ef4444', '#3b82f6',
    '#8b5cf6', '#ec4899', '#14b8a6', '#f97316', '#84cc16'
  ];

  var labels = staffCategorySalesData.map(function (d) { return d.label; });
  var values = staffCategorySalesData.map(function (d) { return d.value; });
  var colors = labels.map(function (_, i) { return palette[i % palette.length]; });

  new Chart(canvas, {
    type: 'doughnut',
    data: {
      labels: labels,
      datasets: [{
        data: values,
        backgroundColor: colors,
        borderWidth: 2,
        borderColor: '#ffffff'
      }]
    },
    options: {
      responsive: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: function (ctx) {
              var total = ctx.dataset.data.reduce(function (a, b) { return a + b; }, 0);
              var pct = total > 0 ? Math.round(ctx.parsed / total * 100) : 0;
              return ' ' + ctx.label + ': ' + ctx.parsed + ' units (' + pct + '%)';
            }
          }
        }
      }
    }
  });

  // Render custom legend
  var legend = document.getElementById('staffPieLegend');
  if (legend) {
    var total = values.reduce(function (a, b) { return a + b; }, 0);
    staffCategorySalesData.forEach(function (d, i) {
      var pct = total > 0 ? Math.round(d.value / total * 100) : 0;
      var li = document.createElement('li');
      li.style.cssText = 'display:flex; align-items:center; gap:10px; margin-bottom:10px;';
      li.innerHTML =
        '<span style="display:inline-block; width:14px; height:14px; border-radius:3px; background:' + colors[i] + '; flex-shrink:0;"></span>' +
        '<span style="flex:1; font-size:0.92rem; color:#374151;">' + d.label + '</span>' +
        '<strong style="font-size:0.92rem; color:#1e293b;">' + d.value + ' <span style="color:#94a3b8; font-weight:400;">(' + pct + '%)</span></strong>';
      legend.appendChild(li);
    });
  }
})();
