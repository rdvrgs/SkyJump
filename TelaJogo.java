
// BIBLIOTECAS
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaJogo extends JPanel implements ActionListener {

    // EIXOS DO PERSONAGEM
    int posicao_x_personagem = 260;
    int posicao_y_personagem = 10;

    int velocidade_y_personagem = 0; // VELOCIDADE SUBINDO OU DESCENDO NO INSTANTE
    int gravidade = 1; // FORÇA QUE PUXARÁ O PERSONAGEM PARA BAIXO

    ArrayList<Plataformas> plataformas = new ArrayList<>();

    // CRIAÇÃO DE METODO
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(posicao_x_personagem, posicao_y_personagem, 40, 40);
        g.setColor(Color.GREEN);
        for (Plataformas p : plataformas) {
            g.fillRect(p.x_plataforma, p.y_plataforma, 100, 20);
        }
    }

    // CONSTRUTOR
    public TelaJogo() {

        Random sorteador = new Random(); // SORTEADOR

        for (int i = 0; i < 8; i++) {
            int xSorteado = sorteador.nextInt(440); // SORTEIA NA POSICAO HORIZONTAL QUE CABE NA LARGURA 540px
            int yCalculado = i * 120 + 60; // DISTRUBUIR 8 PLATAFORMAS NO EIXO Y
            plataformas.add(new Plataformas(xSorteado, yCalculado));
        }
    }
}