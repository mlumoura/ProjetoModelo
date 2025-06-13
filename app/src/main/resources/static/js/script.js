// 🔹 Alterna entre modo escuro e claro quando o usuário clica no botão de tema
function toggleDarkMode() {
    document.body.classList.toggle("dark-mode");

    localStorage.setItem("darkMode", document.body.classList.contains("dark-mode"));

    // 🔹 Atualiza imediatamente a mensagem no menu e no relatório
    let statusModo = document.getElementById("statusModo");
    if (statusModo) {
        let modoEscuro = document.body.classList.contains("dark-mode");
        statusModo.innerText = modoEscuro ? "🌙 Modo Escuro ATIVADO! 🌙" : "☀️ Modo Claro ATIVADO! ☀️";
        statusModo.style.backgroundColor = modoEscuro ? "#333" : "#f0f0f0";
        statusModo.style.color = modoEscuro ? "white" : "black";
    }
}

// 🔹 Antes da página carregar, verifica no `localStorage` se o usuário já ativou o modo escuro anteriormente
if (localStorage.getItem("darkMode") === "true") {
    document.body.classList.add("dark-mode"); // 🔹 Ativa o modo escuro caso já tenha sido salvo
}

document.addEventListener("DOMContentLoaded", () => {
    let darkModeAtivado = localStorage.getItem("darkMode") === "true";

    // 🔹 Aplica o modo escuro ao carregar a página
    if (darkModeAtivado) {
        document.body.classList.add("dark-mode");
    }
});


// 🔹 Função para redirecionar o usuário para os relatórios, garantindo que os parâmetros sejam passados corretamente
function buscarRelatorio(event) {
    event.preventDefault(); // 🔹 Evita que o formulário recarregue a página

    // 🔹 Captura os parâmetros da pesquisa e redireciona para a URL do relatório
    let params = new URLSearchParams(new FormData(document.getElementById("formRelatorio"))).toString();
    window.location.href = "/clientes-view/relatorios-html?" + params;
}





