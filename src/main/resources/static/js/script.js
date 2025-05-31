function toggleDarkMode() {
    document.body.classList.toggle("dark-mode");
    localStorage.setItem("darkMode", document.body.classList.contains("dark-mode"));
}

// 🔹 Mantém o tema escuro antes da página carregar
if (localStorage.getItem("darkMode") === "true") {
    document.body.classList.add("dark-mode");
}

function buscarRelatorio(event) {
    event.preventDefault(); // 🔹 Evita recarregar a página e apagar os resultados

    let params = new URLSearchParams(new FormData(document.getElementById("formRelatorio"))).toString();
    window.location.href = "/clientes-view/relatorios-html?" + params;
}

// 🔹 Mantém o modo escuro ativo se o usuário já tinha ativado antes
window.onload = function () {
    console.log("Script carregado!");

    // 🔹 Mantém o tema que o usuário já tinha escolhido
    let darkModeAtivado = localStorage.getItem("darkMode") === "true";
    if (darkModeAtivado) {
        document.body.classList.add("dark-mode");
    }
};



