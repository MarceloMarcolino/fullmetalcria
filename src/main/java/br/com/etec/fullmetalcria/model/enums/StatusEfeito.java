package br.com.etec.fullmetalcria.model.enums;

/**
 * Os 5 Status de Batalha que podem afetar um Cria durante o combate.
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Status da Batalha.txt">Status da Batalha</a>
 */
public enum StatusEfeito {

    /** Perde 1 Durabilidade por Rodada */
    EM_CHAMAS("Em Chamas", "Perde 1 Durabilidade por Rodada"),

    /** -2 Velocidade */
    CONGELADO("Congelado", "-2 Velocidade"),

    /** -2 Carapaça (temporariamente suspenso no ar) */
    NO_AR("No Ar", "-2 Carapaça"),

    /** Começa o turno com 1 Ponto de Comando a menos */
    PARALISADO("Paralisado", "Começa o turno com 1 Ponto de Comando a menos"),

    /** -1 Dano */
    ENVENENADO("Envenenado", "-1 Dano");

    private final String nome;
    private final String descricao;

    StatusEfeito(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
}
