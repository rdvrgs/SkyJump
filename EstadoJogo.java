import java.util.ArrayList;

public class EstadoJogo {
    
    /*  ==============================================================
    Este arquivo é destinado a criação dos atributos que ficam salvos dentro do estado do jogo, para assim salvar no JSON e retornar ao jogo quando quiser.
    ============================================================== */
    int posicao_x_personagem;
    int posicao_y_personagem;
    int pontuacao_jogador;
    int velocidade_y_personagem;
    ArrayList<Plataformas> plataformas;
}
