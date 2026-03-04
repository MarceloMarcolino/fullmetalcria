package br.com.etec.fullmetalcria.model.enums;

/**
 * Os 4 tipos de Chassi disponíveis para montar um Cria.
 * Cada chassi define os atributos-base e regras de distribuição de pontos.
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Guia dos Chassis.txt">Guia dos Chassis</a>
 */
public enum Chassi {

    /**
     * Artes marciais (karatê, muay thai). Equilibrado em ATK/DEF/SPD.
     * Distribuição: +2 pontos em QUALQUER atributo.
     */
    SHOTO(14, 3, 3, 3, 3, 5, "Pode distribuir +2 pontos em qualquer atributo"),

    /**
     * Especialista em ataques à distância. Dano máximo em pouco tempo, mas frágil.
     * Distribuição: +2 pontos APENAS entre Mira, Velocidade e Dano.
     */
    SHOOTER(11, 4, 3, 1, 3, 6, "Pode distribuir +2 pontos entre Mira, Velocidade e Dano"),

    /**
     * Temática animal (garras, cascos, etc.). Tanque com alta Durabilidade.
     * Distribuição: +2 pontos APENAS entre Durabilidade, Dano e Carapaça.
     */
    BEAST(15, 4, 3, 3, 2, 5, "Pode distribuir +2 pontos entre Durabilidade, Dano e Carapaça"),

    /**
     * Focado em armamento. Similar ao Shoto, mas otimizado para uso de armas.
     * Distribuição: +2 pontos em qualquer atributo EXCETO Dano.
     */
    LANCER(13, 3, 3, 3, 1, 6, "Pode distribuir +2 pontos em qualquer atributo exceto Dano");

    private final int durabilidade;
    private final int mira;
    private final int velocidade;
    private final int carapaca;
    private final int dano;
    private final int bateria;
    private final String regraDistribuicao;

    Chassi(int durabilidade, int mira, int velocidade, int carapaca, int dano, int bateria,
           String regraDistribuicao) {
        this.durabilidade = durabilidade;
        this.mira = mira;
        this.velocidade = velocidade;
        this.carapaca = carapaca;
        this.dano = dano;
        this.bateria = bateria;
        this.regraDistribuicao = regraDistribuicao;
    }

    public int getDurabilidade() { return durabilidade; }
    public int getMira() { return mira; }
    public int getVelocidade() { return velocidade; }
    public int getCarapaca() { return carapaca; }
    public int getDano() { return dano; }
    public int getBateria() { return bateria; }
    public String getRegraDistribuicao() { return regraDistribuicao; }
}
