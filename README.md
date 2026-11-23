# ☁️ Sky Jump — Jogo 2D usando Java Swing 🏃‍♂️

Trabalho 2 da disciplina de **Programação II (2026/02)**.

Um jogo 2D de plataforma vertical inspirado no *Sky Jump* do jogo *Pou*, desenvolvido em **Java Swing** com sprites próprios e **manutenção de estado entre execuções** utilizando a biblioteca **Gson (JSON)**.

---

## 🎮 Objetivo e Jogabilidade
* **Objetivo:** Subir pelas plataformas evitando cair no fundo da tela até atingir **1.000 pontos** para vencer!
* **Chão inicial:** Enquanto estiver com `0` pontos no início da partida, o personagem quica no chão sem ser eliminado.
* **Persistência automática:** Ao fechar a janela pelo botão **X** ou em **Opções → Sair do jogo**, o progresso da partida (posição, velocidade, pontuação e posições das 8 plataformas) é salvo automaticamente em `conteudo_salvo.json`.
* **Menu inteligente:**
  * Se houver uma partida salva pendente, exibe **CONTINUAR JOGO**, **NOVO JOGO** e **SAIR DO JOGO**.
  * Se não houver um save pendente (ou após vitória/derrota), exibe apenas **NOVO JOGO** e **SAIR DO JOGO**.

---

## ⌨️ Controles
| Tecla / Ação | Função |
| :--- | :--- |
| `A` ou `←` | Move o personagem para a esquerda |
| `D` ou `→` | Move o personagem para a direita |
| Bordas laterais | Atravessa de um lado e reaparece do outro |
| **Opções → Novo Jogo** | Reinicia a partida |
| **Opções → Sair do jogo** (ou botão `X`) | Salva o estado atual em JSON e fecha o jogo |

---

## 📂 Estrutura do Projeto
* **`JanelaJogo.java`**: Classe principal (`main`), configura o `JFrame` (`540x960`), a barra de menu (`JMenuBar`) e o `WindowAdapter` para salvar automaticamente ao fechar a janela.
* **`TelaJogo.java`**: Painel do jogo (`JPanel`), implementa o *Game Loop* a 50 FPS (`Timer` de 20ms), física de gravidade com limite de queda (evitando *tunneling*), detecção de colisão nos pés (`Rectangle.intersects`), câmera com reciclagem de plataformas, sprites, menus (`JOptionPane`) e salvamento/carregamento com **Gson**.
* **`Plataformas.java`**: Classe modelo com as coordenadas `X` e `Y` de cada plataforma.
* **`EstadoJogo.java`**: Classe modelo utilizada pelo **Gson** para serializar e desserializar o estado da partida em JSON.
* **`Imagens/`**: Sprites do personagem (4 poses: parado e correndo para esquerda/direita), plataformas e céu de fundo *(personagem em pixel art criado com auxílio da IA Gemini)*.
* **`lib/gson-2.10.1.jar`**: Biblioteca Google Gson para leitura e escrita do arquivo JSON.

---

## ▶️ Como Executar o Projeto

### Requisitos
* **Java JDK 8 ou superior** instalado.

### Opção 1 — Baixar e Jogar (Sem `git clone` e sem compilar!)
1. Baixe a versão pronta para jogar: **[⬇️ Baixar Sky Jump v1.0](https://github.com/rdvrgs/SkyJump/releases/latest)**
2. Extraia o arquivo `SkyJump-v1.0.zip` em uma pasta.
3. Dê **dois cliques** no arquivo **`SkyJump.jar`** (ou execute `java -jar SkyJump.jar` no terminal).

### Opção 2 — Via Terminal
Primeiro, abra o terminal e entre na pasta do projeto (`cd caminho/para/SkyJump`):

**No Windows (PowerShell / CMD):**
```powershell
cd SkyJump
javac -encoding UTF-8 -cp ".;lib/gson-2.10.1.jar" JanelaJogo.java
java -cp ".;lib/gson-2.10.1.jar" JanelaJogo
```

**No Linux / macOS:**
```bash
cd SkyJump
javac -encoding UTF-8 -cp ".:lib/gson-2.10.1.jar" JanelaJogo.java
java -cp ".:lib/gson-2.10.1.jar" JanelaJogo
```

---

## 👤 Autor
Desenvolvido por **Richard Carcuchinski Vargas**
