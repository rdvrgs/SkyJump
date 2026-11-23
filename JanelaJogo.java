/* ==============================================================
BIBLIOTECAS USADAS
============================================================== */
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JanelaJogo {

    public static void main(String[] args) {

        /* ==============================================================
        CRIAÇÃO DA JANELA, SEU TAMANHO E SEU TITULO
        ============================================================== */
        JFrame janela_jogo = new JFrame();
        janela_jogo.setSize(540, 960);
        janela_jogo.setTitle("SKY JUMP");
        /*
         * ========================================
         * O ponto (0,0) do Size fica no canto superior esquerdo
         * ===========================================
         */


        /* ==============================================================
        FECHAR JANELA AO CLICAR NO X | ABRIR ELA NO MEIO DA TELA E NÃO PODER MODIFICAR SEU TAMANHO/ESCALA
        ============================================================== */
        janela_jogo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela_jogo.setLocationRelativeTo(null);
        janela_jogo.setResizable(false);
        

        /* ==============================================================
        CRIAÇÃO DO PAINEL E ADICIONAR ELE DENTRO DO JOGO
        ============================================================== */
        TelaJogo tela_jogo = new TelaJogo();
        janela_jogo.add(tela_jogo);


        /* ==============================================================
        CRIAÇÃO DA BARRA DO TOPO E SEUS ITENS | ADIÇÃO DOS ITENS DENTRO DO MENU CRIADO
        ============================================================== */
        JMenuBar barra_topo = new JMenuBar();
        JMenu menu_opcoes = new JMenu("Opções");
        JMenuItem reinicio_jogo = new JMenuItem("Novo Jogo");
        JMenuItem sair_jogo = new JMenuItem("Sair do jogo");
        menu_opcoes.add(reinicio_jogo);
        menu_opcoes.add(sair_jogo);
        barra_topo.add(menu_opcoes);
        janela_jogo.setJMenuBar(barra_topo);

        /* ==============================================================
        AÇÕES DOS BOTÕES | SAIR DO JOGO SALVA ELE E FECHA | REINICIO FAZ A AÇÃO DE REINICIAR | CLICAR NO X TAMBÉM SALVA O JOGO 
        ============================================================== */
        sair_jogo.addActionListener(e -> {
            tela_jogo.salvar_jogo();
            System.exit(0);
        });
        reinicio_jogo.addActionListener(e -> tela_jogo.reiniciarJogo());

        janela_jogo.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                tela_jogo.salvar_jogo();
            }
        });

        janela_jogo.setVisible(true); // JANELA APARECE NA TELA
    }
}
