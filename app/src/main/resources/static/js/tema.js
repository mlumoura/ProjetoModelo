document.addEventListener("DOMContentLoaded", () => {
    let darkModeAtivado = localStorage.getItem("darkMode") === "true";
    document.body.classList.toggle("dark-mode", darkModeAtivado);
});
