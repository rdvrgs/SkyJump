// BIBLIOTECAS
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JanelaJogo {

    public static void main(String[] args) {

        JFrame janela_jogo = new JFrame(); // CRIACAO DA JANELA

        janela_jogo.setSize(540, 960); // DIMENSOES
        /*
         * ========================================
         * O ponto (0,0) do Size fica no canto superior esquerdo
         * ===========================================
         */

        janela_jogo.setTitle("SKY JUMP"); // TITULO
        janela_jogo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // FECHA AO CLICAR NO X
        janela_jogo.setLocationRelativeTo(null); // ABRIR A JANELA DO JOGO NO MEIO DA TELA
        janela_jogo.setResizable(false); // NAO PERMITIR ALTERAÇÃO NO TAMANHO DA TELA
        
        TelaJogo tela_jogo = new TelaJogo(); // CRIAR PAINEL DO JOGO
        janela_jogo.add(tela_jogo); // ADICIONAR O PAINEL DENTRO DO JOGO


        // ===========================================
        // Barra do Topo
        JMenuBar barra_topo = new JMenuBar();
        JMenu menu_opcoes = new JMenu("Opções");
        JMenuItem reinicio_jogo = new JMenuItem("Novo Jogo");
        JMenuItem sair_jogo = new JMenuItem("Sair do jogo");
        menu_opcoes.add(reinicio_jogo);
        menu_opcoes.add(sair_jogo);
        barra_topo.add(menu_opcoes);
        janela_jogo.setJMenuBar(barra_topo);

        // Ações dos botões
        sair_jogo.addActionListener(e -> {
            tela_jogo.salvar_jogo();
            System.exit(0);
        });
        reinicio_jogo.addActionListener(e -> tela_jogo.reiniciarJogo()); // REINICIA A PARTIDA

        janela_jogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                tela_jogo.salvar_jogo();
            }
        });

        // ===========================================

        janela_jogo.setVisible(true); // JANELA APARECE NA TELA
    }
}
