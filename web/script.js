// Configuração base da API
const urlBase = 'http://localhost:8080';

// Elementos do DOM
const formularioLogin = document.getElementById('formulario-login');
const secaoLogin = document.getElementById('secao-login');
const secaoCadastro = document.getElementById('secao-cadastro'); // Novo
const secaoResultado = document.getElementById('secao-resultado');
const painelDados = document.getElementById('painel-dados');
const botaoTesteApi = document.getElementById('botao-teste-api');
const botaoLimpar = document.getElementById('botao-limpar');
const formCadastro = document.getElementById('formulario-cadastro'); // Novo

// --- 1. LOGIN REAL ---
formularioLogin.addEventListener('submit', async function(evento) {
    evento.preventDefault();
    
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const botao = document.getElementById('botao-entrar');

    botao.innerText = "Entrando...";
    botao.disabled = true;

    try {
        const resposta = await fetch(`${urlBase}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if (!resposta.ok) {
            throw new Error('Falha no login. Verifique email e senha.');
        }

        const dados = await resposta.json();
        
        // SUCESSO!
        alert("Login realizado com sucesso!");
        localStorage.setItem('token_geb', dados.token);

        // Esconde login e cadastro, mostra resultado
        secaoLogin.classList.add('oculto');
        secaoCadastro.classList.add('oculto');
        secaoResultado.classList.remove('oculto');
        painelDados.innerText = "Bem-vindo! Token salvo com sucesso.";

    } catch (erro) {
        alert("Erro: " + erro.message);
    } finally {
        botao.innerText = "Entrar";
        botao.disabled = false;
    }
});

// --- 2. CADASTRO DE USUÁRIO (NOVO) ---
formCadastro.addEventListener('submit', async function(evento) {
    evento.preventDefault();

    const nome = document.getElementById('cad-nome').value;
    const email = document.getElementById('cad-email').value;
    const senha = document.getElementById('cad-senha').value;
    const papel = document.getElementById('cad-papel').value; // Pega o Admin ou Operador

    try {
        const resposta = await fetch(`${urlBase}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, email, senha, papel })
        });

        if (resposta.ok) {
            alert("Sucesso! Usuário cadastrado. \nAgora suba e faça LOGIN com esses dados.");
            // Limpa o formulário
            document.getElementById('cad-nome').value = '';
            document.getElementById('cad-email').value = '';
            document.getElementById('cad-senha').value = '';
        } else {
            const erro = await resposta.text();
            alert("Erro ao cadastrar: " + erro);
        }
    } catch (erro) {
        alert("Erro de conexão: " + erro.message);
    }
});

// --- 3. TESTE DE ROTA PROTEGIDA (VIP) ---
const botaoVip = document.getElementById('botao-vip');

botaoVip.addEventListener('click', async function() {
    painelDados.innerText = "Verificando credenciais...";
    const token = localStorage.getItem('token_geb');

    if (!token) {
        painelDados.innerText = "ERRO: Token não encontrado. Faça login.";
        return;
    }

    try {
        const resposta = await fetch(`${urlBase}/api/privado/dados`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (resposta.status === 403 || resposta.status === 401) {
            throw new Error("Acesso Negado! Token inválido ou expirado.");
        }

        const texto = await resposta.text();
        painelDados.innerText = texto;

    } catch (erro) {
        painelDados.innerText = "Falha: " + erro.message;
    }
});

// --- 4. TESTE DE CONEXÃO (ROTA PÚBLICA) ---
async function testarConexao() {
    painelDados.innerText = "Carregando...";
    try {
        const resposta = await fetch(`${urlBase}/api/publico/status`);
        const dados = await resposta.text();
        painelDados.innerText = dados;
    } catch (erro) {
        painelDados.innerText = "Erro ao conectar: " + erro.message;
    }
}

botaoTesteApi.addEventListener('click', testarConexao);

botaoLimpar.addEventListener('click', function() {
    localStorage.removeItem('token_geb');
    window.location.reload();
});