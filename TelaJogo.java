/* ==============================================================
BIBLIOTECAS USADAS
============================================================== */
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.JOptionPane;
import java.awt.Image;
import javax.swing.ImageIcon;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;


public class TelaJogo extends JPanel implements ActionListener, KeyListener {

    /* ==============================================================
    VARIAVEIS DO PERSONAGEM:
    - Posição no eixo X e Y
    - Pegar o movimento dele para os lados
    - Guardar o lado que ele está virado
    ============================================================== */
    int posicao_x_personagem = 260;
    int posicao_y_personagem = 800;
    boolean mov_esquerda = false, mov_direita = false;
    boolean olhando_esquerda = false;

    /* ==============================================================
    IMAGENS (SPRITES) DESENHADAS NA TELA DO JOGO (FUNDO, PLATAFORMA E AS 4 POSES DO BONEQUINHO)
    ============================================================== */
    Image img_fundo;
    Image img_plataforma;
    Image img_parado_direita;
    Image img_parado_esquerda;
    Image img_corrida_direita;
    Image img_corrida_esquerda;

    /* ==============================================================
    ÍCONES DO BONEQUINHO REDIMENSIONADOS PARA AS JANELINHAS DE MENU, VITÓRIA E DERROTA
    ============================================================== */
    ImageIcon img_tela_derrota;
    ImageIcon img_tela_vitoria;

    /* ==============================================================
    VARIAVEIS DIVERSAS: VELOCIDADE SUBINDO E DESCENDO, FORÇA DA GRAVIDADE PARA BAIXO E PONTUAÇÃO
    ============================================================== */
    int velocidade_y_personagem = 0;
    int gravidade = 1;
    int pontuacao_jogador = 0;

    /* ==============================================================
    GERAR AS PLATAFORMAS EM POSIÇÕES ALEATÓRIAS | CRIACAO DE ARRAY PARA VARIAS PLATAFORMAS | TEMPO DE ATUALIZAÇÃO PARA RODAR BEM, SERA DEFINIDO COMO 50 FPS.
    ============================================================== */
    Random sorteador = new Random();
    ArrayList<Plataformas> plataformas = new ArrayList<>();
    Timer tempo_contador;

    /* ==============================================================
    CRIAÇÃO DO PAINTCOMPONENT | PINTAR O FUNDO DA TELA | FOTO DO PERSONAGEM COM BASE NOS MOVIMENTOS | DESENHO DAS PLATAFORMAS | ESTÉTICA DA PONTUAÇÃO
    ============================================================== */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ============================================
        g.drawImage(img_fundo, 0, 0, 540, 960, this);

        // ============================================
        if (mov_esquerda == true) 
        {
            g.drawImage(img_corrida_esquerda, posicao_x_personagem, posicao_y_personagem, 40, 40, this);
        } 
        else if (mov_direita == true) 
        {
            g.drawImage(img_corrida_direita, posicao_x_personagem, posicao_y_personagem, 40, 40, this);
        } 
        else if (olhando_esquerda == true) 
        {
            g.drawImage(img_parado_esquerda, posicao_x_personagem, posicao_y_personagem, 40, 40, this);
        } 
        else 
        {
            g.drawImage(img_parado_direita, posicao_x_personagem, posicao_y_personagem, 40, 40, this);
        }

        // ============================================
        for (Plataformas p : plataformas) {
           g.drawImage(img_plataforma, p.x_plataforma, p.y_plataforma, 100, 20, this);
        }
        
        if (pontuacao_jogador == 0) {
            g.drawImage(img_plataforma, 0, 860, 540, 70, this);
        }

        // ============================================
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Pontos: " + pontuacao_jogador, 20, 30);
    }

    /* ==============================================================
    CRIAÇÃO DO CONSTRUTOR:
    - Carregar imagens e icones do bonequinho, das plataformas e do fundo do jogo
    - Criar as 8 plataformas em locais aleatórios
    - Mostrar menu inicial antes do jogo começar
    - Definir a atualização como 50 FPS
    - Permitir foco do teclado e escutar as teclas clicadas
    ============================================================== */
    public TelaJogo() {

        // Pegar fotos da pasta de Imagens e cria-las
        img_fundo = new ImageIcon("Imagens/fundo.png").getImage();
        img_plataforma = new ImageIcon("Imagens/plataforma.png").getImage();
        img_parado_direita = new ImageIcon("Imagens/parado_direita.png").getImage();
        img_parado_esquerda = new ImageIcon("Imagens/parado_esquerda.png").getImage();
        img_corrida_direita = new ImageIcon("Imagens/corrida_direita.png").getImage();
        img_corrida_esquerda = new ImageIcon("Imagens/corrida_esquerda.png").getImage();
        img_tela_derrota = new ImageIcon(img_parado_direita.getScaledInstance(64, 64, Image.SCALE_SMOOTH));
        img_tela_vitoria =  new ImageIcon(img_corrida_direita.getScaledInstance(64, 64, Image.SCALE_SMOOTH));

        // Cria as 8 plataformas, sorteando o eixo X entre 0 e 439, de forma que não saia da tela | Distribui o Y de 120 em 120 pixels, para ficar com espaço certinho entre eles
        for (int i = 0; i < 8; i++) {
            int xSorteado = sorteador.nextInt(440);
            int yCalculado = i * 120 + 60;
            plataformas.add(new Plataformas(xSorteado, yCalculado));
        }

        // ============================================
        menu_inicial();

        // Definir o Timer como  20milissegundos por atualização na telaJogo.
        tempo_contador = new Timer(20, this);
        tempo_contador.start();     

        // ============================================
        setFocusable(true);
        addKeyListener(this); 
    }

    /* ==============================================================
    CRIAÇÃO DO ACTION PERFORMED | VERIFICAR MOVIMENTOS
    - Se foi para a esquerda, personagem anda 10 posicoes para esquerda | Se foi para direita, anda 10 posicoes para direita
    - Se sair da tela por um lado, retorna pelo outro
    - Gravidade aumenta a velocidade de queda
    - Velocidade atualiza a posição Y do bonequinho, fazendo ele subir ou descer
    ============================================================== */
    public void actionPerformed(ActionEvent e)
    {
        if (mov_esquerda == true)
        {
            posicao_x_personagem = posicao_x_personagem - 10;
        }
        if (mov_direita == true)
        {
            posicao_x_personagem = posicao_x_personagem + 10;
        }

        // ============================================
        if (posicao_x_personagem < 0)
        {
            posicao_x_personagem = 500;
        }
        else if (posicao_x_personagem > 500)
        {
            posicao_x_personagem = 0;
        }

        // ============================================
        velocidade_y_personagem = velocidade_y_personagem + gravidade;

        // LIMITA A VELOCIDADE MÁXIMA DE QUEDA EM 15px POR QUADRO, EVITANDO QUE O BONECO ATRAVESSE AS PLATAFORMAS CAINDO RÁPIDO
        // Testando, vi esse bug que as vezes atravessava algumas plataformas. Assim, a velocidade é até 15, evitando que ele atravesse.
        if (velocidade_y_personagem > 15)
        {
            velocidade_y_personagem = 15;
        }

        posicao_y_personagem = posicao_y_personagem + velocidade_y_personagem;

        /* ==============================================================
        VERIFICAR SE O PERSONAGEM ENCOSTOU EM ALGO
        - Se o personagem estiver caindo, verifica se ele bateu em alguma plataforma:
            - Cria duas áreas de colisão, uma nos pés do bonequinho e outra no topo da plataforma. 
            - Se encostarem, muda a velocidade Y para -20 para pular.
        ============================================================== */
        if (velocidade_y_personagem > 0) // TESTE SE ESTA CAINDO
        {
            for (Plataformas p : plataformas) // BATEU EM ALGUMA PLATAFORMA?
            {
                Rectangle retangulo = new Rectangle(posicao_x_personagem, posicao_y_personagem + 30, 40, 10); // POSICAO PERSONAGEM

                Rectangle retangulo2 = new Rectangle(p.x_plataforma, p.y_plataforma, 100, 10); // POSICAO PLATAFORMA

                if (retangulo.intersects(retangulo2)) // ENCOSTARAM?
                {
                    velocidade_y_personagem = -20; // PERSONAGEM PULA 
                }
            }
        }

        /* ==============================================================
        SE O PERSONAGEM SUBIU MAIS DO QUE METADE DA TELA:
        - Deixar ele sempre no meio da tela
        - Buscar entre as 8 plataformas, elas descem e ele sobe
        - Quando uma plataforma passa do fundo da tela, lá em baixo, ela retorna no topo em novo X e soma +10 pontos para o jogador
        ============================================================== */
        if (posicao_y_personagem < 400)
        {
            posicao_y_personagem = 400; // PERSONAGEM FICA NO MEIO 

            for (Plataformas p : plataformas) // PASSAR PELAS 8
            {
                p.y_plataforma = p.y_plataforma - velocidade_y_personagem; // PLATAFORMAS DESCEM E PERSONAGEM SOBE

                // PLATAFORMA REAPARECE EM CIMA E JOGADOR GANHA 10 PONTOS.
                if (p.y_plataforma > 960)
                {
                    p.y_plataforma = 0;
                    p.x_plataforma = sorteador.nextInt(440);
                    pontuacao_jogador =  pontuacao_jogador + 10;
                }
            }
        }

        /* ==============================================================
        VERIFICAÇÕES DO BONEQUINHO
        - Se estiver no início do jogo e encostar no chão, ele pula sem morrer
        - Se ele passar do limite da tela, ele morreu, dai:   
            - Apaga o json, pois ele morreu
            - Exibe o menu do que deseja fazer
        - Se ele conquistar os 1000 de pontuação:
            - Apaga o json, pois ele venceu
            - Exibe o menu do que ele deseja fazer
        ============================================================== */
        if (pontuacao_jogador == 0 && posicao_y_personagem >= 820) {
            velocidade_y_personagem = -20;
        }

        // ======================================================
        if (posicao_y_personagem > 960)
        {
            tempo_contador.stop();
            new File("conteudo_salvo.json").delete(); // APAGAR ARQUIVO SE O BONEQUINHO MORREU

            String[] opcoes = {"Recomeçar", "Sair do Jogo"};

            int escolha = JOptionPane.showOptionDialog(this, "FIM DE JOGO!\nVocê caiu lá para baixo e está eliminado.\nPontos conquistados: " + pontuacao_jogador, "Fim de Jogo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, img_tela_derrota, opcoes, opcoes[0]);

            if (escolha == 0)
            {
                reiniciarJogo();
            }
            else
            {
                System.exit(0);
            }
        }

        // ======================================================
        if (pontuacao_jogador >= 1000)
        {   
            tempo_contador.stop();
            String[] opcoes = {"Jogar Novamente", "Sair do Jogo"};
            new File("conteudo_salvo.json").delete();
            int escolha = JOptionPane.showOptionDialog(this, "PARABÉNS!\nVocê venceu o jogo atingindo " + pontuacao_jogador + " pontos!", "Vitória!" ,JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, img_tela_vitoria, opcoes, opcoes[0]);

            if (escolha == 0)
            {
                reiniciarJogo();
            }
            else
            {
                System.exit(0);
            }
        }

        repaint();
    }

    /* ==============================================================
    VERIFICA A TECLA PRESSIONADA E TECLA CLICADA
    ============================================================== */
    public void keyPressed(KeyEvent e)
    {
        // SE CLICAR 'A' ou '<-' ANDA PARA ESQUERDA
        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            mov_esquerda = true;
            olhando_esquerda = true;
        }
        // 'D' ou '->' ANDA PARA DIREITA
        else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            mov_direita = true;
            olhando_esquerda = false;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        // SE SOLTAR 'A' ou '<-', PARA DE ANDAR PARA ESQUERDA
        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            mov_esquerda = false;
        }
        // SE SOLTAR 'D' ou '->', PARA DE ANDAR PARA DIREITA
        else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            mov_direita = false;
        }
    }

    public void keyTyped(KeyEvent e)
    {}

    /* ==============================================================
    REINICIO DO JOGO
    - Faz com que o bonequinho fique no meio em baixo da tela
    - Com velocidade e pontuação zerados
    - Começa a atualizar de novo
    - Zera os movimentos
    ============================================================== */
    public void reiniciarJogo() 
    {
        // Posições, velocidade, pontuacao e tempo reiniciados
        posicao_x_personagem = 260;
        posicao_y_personagem = 800;
        velocidade_y_personagem = 0;
        pontuacao_jogador = 0;
        tempo_contador.start();

        // PARA COMEÇAR COM MOVIMENTOS ZERADOS
        mov_esquerda = false;
        mov_direita = false;
    }

    /* ==============================================================
    METODO PARA SALVAR O JOGO
    - Salva o progresso dentro da classe de EstadoJogo
    - Converter para o JSON
    - Salvar no arquivo "conteudo_salvo.json" em formato JSON
    ============================================================== */
    public void salvar_jogo()
    {
        try
        {
            EstadoJogo estado = new EstadoJogo();
            estado.posicao_x_personagem = posicao_x_personagem;
            estado.posicao_y_personagem = posicao_y_personagem;
            estado.pontuacao_jogador = pontuacao_jogador;
            estado.velocidade_y_personagem = velocidade_y_personagem;
            estado.plataformas = plataformas;

            Gson gson = new Gson();
            FileWriter salvar_json = new FileWriter("conteudo_salvo.json");
            gson.toJson(estado, salvar_json);
            salvar_json.close();
        }
        catch (Exception erro)
        {
            System.out.println("Ocorreu um erro ao salvar o jogo!");
        }
    }

    /* ==============================================================
    METODO PARA CARREGAR O JOGO
    - Le o arquivo "conteudo_salvo.json" e passa para a classe de EstadoJogo
    - Converter o JSON
    - Carrega o progresso que foi salvo dentro da classe de EstadoJogo
    ============================================================== */
    public void carregar_jogo()
    {
        try
        {
            Gson gson = new Gson();
            FileReader ler_json = new FileReader("conteudo_salvo.json");
            EstadoJogo estado = gson.fromJson(ler_json, EstadoJogo.class);
            ler_json.close();

            // VERIFICA SE O JSON É VÁLIDO | NÃO ESTÁ VAZIO, NÃO DEU GAME OVER E NÃO VENCEU E DAI RESTAURA O JOGO
            if (estado != null && estado.posicao_y_personagem <= 960 && estado.pontuacao_jogador < 1000)
            {
                posicao_x_personagem = estado.posicao_x_personagem;
                posicao_y_personagem = estado.posicao_y_personagem;
                velocidade_y_personagem = estado.velocidade_y_personagem;
                pontuacao_jogador = estado.pontuacao_jogador;
                plataformas = estado.plataformas;
            }
        }
        catch (Exception erro)
        {   
            // SE NÃO TIVER NADA SALVO, COMEÇA UM JOGO DO ZERO
            System.out.println("Nenhum jogo salvo.");
        }
    }

    /* ==============================================================
    METODO PARA INFORMAÇÕES DO MENU
    - Pega o JSON
    - Verifica se tem algo salvo nele. Se tiver, exibe o menu com CONTINUAR JOGO
    - Se não tiver nada, exibe somente as outras opções
    ============================================================== */
    public void menu_inicial()
    {
        File arquivo_salvo = new File("conteudo_salvo.json");

        if (arquivo_salvo.exists())
        {
            String[] opcoes = {"CONTINUAR JOGO", "NOVO JOGO", "SAIR DO JOGO"};

            int escolha = JOptionPane.showOptionDialog(this, "Você tem uma partida salva!\nO que deseja fazer agora?", "SKY JUMP - Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, img_tela_vitoria, opcoes, opcoes[0]);

            if (escolha == 0) 
            {
                carregar_jogo();
            } 
            else if (escolha == 1) 
            {
                arquivo_salvo.delete();
            } 
            else 
            {
                System.exit(0);
            }
        }

        else
        {
            String[] opcoes = {"NOVO JOGO", "SAIR DO JOGO"};
            
            int escolha = JOptionPane.showOptionDialog(this, "Bem-vindo ao SKY JUMP!", "SKY JUMP - Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, img_tela_vitoria, opcoes, opcoes[0]);

            if (escolha != 0)
            {
                System.exit(0);
            }
        }
    }
}