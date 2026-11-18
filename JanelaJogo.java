import javax.swing.JFrame;

public class JanelaJogo {

    public static void main(String[] args) {

        JFrame janelaJogo = new JFrame(); // CRIACAO DA JANELA

        janelaJogo.setSize(540, 960); // DIMENSOES
        /*
         * ========================================
         * O ponto (0,0) do Size fica no canto superior esquerda.
         * ===========================================
         */

        janelaJogo.setTitle("SKY JUMP"); // TITULO
        janelaJogo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // FECHA AO CLICAR NO X
        janelaJogo.setLocationRelativeTo(null); // ABRIR A JANELA DO JOGO NO MEIO DA TELA
        janelaJogo.setResizable(false); // NAO PERMITIR ALTERAÇÃO NO TAMANHO DA TELA
        janelaJogo.setVisible(true); // JANELA APARECE NA TELA

    }
}
