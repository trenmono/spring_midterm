document.addEventListener("DOMContentLoaded", function () {
  const sidebar = document.getElementById("sidebar");
  const toggle = document.getElementById("sidebarToggle");
  const overlay = document.getElementById("sidebarOverlay");

  if (!sidebar || !toggle) return;

  function setOpen(open) {
    sidebar.classList.toggle("open", open);
    if (overlay) overlay.classList.toggle("show", open);
    toggle.setAttribute("aria-expanded", String(open));
  }

  toggle.addEventListener("click", function () {
    setOpen(!sidebar.classList.contains("open"));
  });

  if (overlay) overlay.addEventListener("click", function () {
    setOpen(false);
  });

  window.addEventListener("resize", function () {
    if (window.innerWidth > 900) setOpen(false);
  });
});
