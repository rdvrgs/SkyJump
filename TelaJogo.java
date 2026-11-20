// BIBLIOTECAS
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

    // EIXOS DO PERSONAGEM
    int posicao_x_personagem = 260;
    int posicao_y_personagem = 800;
    boolean mov_esquerda = false, mov_direita = false; // APURAR O MOVIMENTO DO BONEQUINHO
    boolean olhando_esquerda = false; // PEGAR LADO QUE BONECO OLHOU

    Image img_fundo;
    Image img_plataforma;
    Image img_parado_direita;
    Image img_parado_esquerda;
    Image img_corrida_direita;
    Image img_corrida_esquerda;

    int velocidade_y_personagem = 0; // VELOCIDADE SUBINDO OU DESCENDO NO INSTANTE
    int gravidade = 1; // FORÇA QUE PUXARÁ O PERSONAGEM PARA BAIXO
    int pontuacao_jogador = 0; // COMEÇA EM 0
    Random sorteador = new Random(); // SORTEADOR

    ArrayList<Plataformas> plataformas = new ArrayList<>();

    Timer tempo_contador; // 

    // CRIAÇÃO DE METODO
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(img_fundo, 0, 0, 540, 960, this); // DESENHA O FUNDO NA TELA TODA

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

        g.setColor(Color.GREEN);
        for (Plataformas p : plataformas) {
           g.drawImage(img_plataforma, p.x_plataforma, p.y_plataforma, 100, 20, this);
        }
        // DESENHA O CHAO ENQUANTO O JOGADOR AINDA NAO SUBIU (0 PONTOS)
        if (pontuacao_jogador == 0) {
            g.drawImage(img_plataforma, 0, 860, 540, 70, this);
        }
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20)); // DEFINE FONTE ARIAL, NEGRITO, TAMANHO 16
        g.drawString("Pontos: " + pontuacao_jogador, 20, 30); // Mostra os pontos 
    }

    // CONSTRUTOR
    public TelaJogo() {

        // FOTOS DO BONEQUINHO, PLATAFORMA E FUNDO
        img_fundo = new ImageIcon("Imagens/fundo.png").getImage();
        img_plataforma = new ImageIcon("Imagens/plataforma.png").getImage();
        img_parado_direita = new ImageIcon("Imagens/parado_direita.png").getImage();
        img_parado_esquerda = new ImageIcon("Imagens/parado_esquerda.png").getImage();
        img_corrida_direita = new ImageIcon("Imagens/corrida_direita.png").getImage();
        img_corrida_esquerda = new ImageIcon("Imagens/corrida_esquerda.png").getImage();

        for (int i = 0; i < 8; i++) {
            int xSorteado = sorteador.nextInt(440); // SORTEIA NA POSICAO HORIZONTAL QUE CABE NA LARGURA 540px
            int yCalculado = i * 120 + 60; // DISTRUBUIR 8 PLATAFORMAS NO EIXO Y
            plataformas.add(new Plataformas(xSorteado, yCalculado));
        }

        menu_inicial(); // MOSTRA O MENU INICIAL ANTES DE COMECAR O RELOGIO

        // Definir o Timer como 20 milissegundos por atualização na telaJogo.
        tempo_contador = new Timer(20, this);
        tempo_contador.start();     

        setFocusable(true); // PERMITIR FOCO DO TECLADO
        addKeyListener(this); // AVISAR PAINEL QUE O TelaJogo ESCUTA AS TECLAS CLICADAS.
    }

    // CALCULO DE VELOCIDADES DO MOVIMENTO
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

        // SAIR DE UM LADO DA TELA E APARECER DO OUTRO
        if (posicao_x_personagem < 0)
        {
            posicao_x_personagem = 500;
        }
        else if (posicao_x_personagem > 500)
        {
            posicao_x_personagem = 0;
        }

        velocidade_y_personagem = velocidade_y_personagem + gravidade; // GRAVIDADE AUMENTA VELOCIDAD DA QUEDA
        posicao_y_personagem = posicao_y_personagem + velocidade_y_personagem; // AUMENTAR VELOCIDADE DO PERSONAGEM

        // TESTAR A COLISÃO
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

        // SUBIR A TELA
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

        // COMECO DO JOGO, BONECO NO CHAO
        if (pontuacao_jogador == 0 && posicao_y_personagem >= 820) {
            velocidade_y_personagem = -20;
        }

        // DERROTA OU VITORIA
        if (posicao_y_personagem > 960)
        {
            tempo_contador.stop();
            new File("conteudo_salvo.json").delete(); // APAGAR ARQUIVO SE O BONEQUINHO MORREU

            String[] opcoes = {"Recomeçar", "Sair do Jogo"};

            int escolha = JOptionPane.showOptionDialog(this, "FIM DE JOGO!\nVocê caiu lá para baixo e está eliminado.\nPontos conquistados: " + pontuacao_jogador, "Fim de Jogo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            if (escolha == 0)
            {
                reiniciarJogo();
            }
            else
            {
                System.exit(0);
            }
        }

        if (pontuacao_jogador >= 1000)
        {   
            tempo_contador.stop();
            JOptionPane.showMessageDialog(this, "PARABENS CARA!\nVocê venceu o jogo atingindo os 1000 pontos");
        }

        repaint();
    }

    // SEGURAR NA TECLA FAZ UMA AÇÃO
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
        // SE CLICAR 'A' ou '<-' ANDA PARA ESQUERDA
        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            mov_esquerda = false;
        }
        // 'D' ou '->' ANDA PARA DIREITA
        else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            mov_direita = false;
        }
    }

    public void keyTyped(KeyEvent e)
    {}

    // VOLTA O JOGO PARA O COMECO
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

    // METODO PARA SALVAR ESTADO DO JOGO QUANDO FECHAMOS ELE
    public void salvar_jogo()
    {
        try
        {
            // SALVAR O QUE CRIAMOS DENTRO DA CLASSE EstadoJogo.java
            EstadoJogo estado = new EstadoJogo();
            estado.posicao_x_personagem = posicao_x_personagem;
            estado.posicao_y_personagem = posicao_y_personagem;
            estado.pontuacao_jogador = pontuacao_jogador;
            estado.velocidade_y_personagem = velocidade_y_personagem;
            estado.plataformas = plataformas;

            Gson gson = new Gson(); // CONVERSOR JSON
            FileWriter salvar_json = new FileWriter("conteudo_salvo.json"); // SALVA NO ARQUIVO
            gson.toJson(estado, salvar_json); // ESCREVE NO ARQUIVO EM FORMATO JSON
            salvar_json.close(); // SALVA E FECHA
        }
        catch (Exception erro)
        {
            System.out.println("Ocorreu um erro ao salvar o jogo!");
        }
    }

    // METODO PARA CARREGAR O JOGO QUE FOI SALVO
    public void carregar_jogo()
    {
        try
        {
            Gson gson = new Gson(); // CONVERSON JSON
            FileReader ler_json = new FileReader("conteudo_salvo.json");
            EstadoJogo estado = gson.fromJson(ler_json, EstadoJogo.class);
            ler_json.close();

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
            /* SE NÃO TIVER NADA SALVO, COMEÇA UM JOGO DO ZERO */
            System.out.println("Nenhum jogo salvo.");
        }
    }

    // METODO DE MENU DE OPCOES
    public void menu_inicial()
    {
        File arquivo_salvo = new File("conteudo_salvo.json");

        // SE EXISTIR ALGO SALVO, PEGAR:
        if (arquivo_salvo.exists())
        {
            String[] opcoes = {"CONTINUAR JOGO", "NOVO JOGO", "SAIR DO JOGO"};

            int escolha = JOptionPane.showOptionDialog(this, "Você tem uma partida salva!\nO que deseja fazer agora?", "SKY JUMP - Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

             if (escolha == 0) 
            {
                carregar_jogo(); // CONTINUA DE ONDE PAROU
            } 
            else if (escolha == 1) 
            {
                arquivo_salvo.delete(); // APAGA O SAVE E COMECA DO ZERO
            } 
            else 
            {
                System.exit(0); // SAI DO JOGO
            }
        }

        else
        {
            String[] opcoes = {"NOVO JOGO", "SAIR DO JOGO"};
            
            int escolha = JOptionPane.showOptionDialog(this, "Bem-vindo ao SKY JUMP!", "SKY JUMP - Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);

            if (escolha != 0)
            {
                System.exit(0);
            }
        };
    }
}