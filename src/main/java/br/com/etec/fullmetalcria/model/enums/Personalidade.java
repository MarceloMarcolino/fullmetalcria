package br.com.etec.fullmetalcria.model.enums;

/**
 * As 6 Personalidades (CDs) que um Cria pode ter.
 * Cada personalidade aplica um bônus e uma penalidade a um atributo.
 *
 * @see <a href="Full Metal Cria/Capítulo 4/CDs e Personalidades.txt">CDs e Personalidades</a>
 */
public enum Personalidade {

    /** +1 Mira, -1 Carapaça */
    ASTUTO("Astuto", "mira", 1, "carapaca", -1),

    /** +1 Dano, -1 Mira */
    BRUTO("Bruto", "dano", 1, "mira", -1),

    /** +1 Bateria, -1 Velocidade */
    CALMO("Calmo", "bateria", 1, "velocidade", -1),

    /** +1 Velocidade, -1 Dano */
    TIMIDO("Tímido", "velocidade", 1, "dano", -1),

    /** Sem bônus ou penalidade */
    HUMILDE("Humilde", null, 0, null, 0),

    /** +1 Carapaça, -1 Durabilidade */
    CUIDADOSO("Cuidadoso", "carapaca", 1, "durabilidade", -1);

    private final String nome;
    private final String atributoBonus;
    private final int valorBonus;
    private final String atributoPenalidade;
    private final int valorPenalidade;

    Personalidade(String nome, String atributoBonus, int valorBonus,
                  String atributoPenalidade, int valorPenalidade) {
        this.nome = nome;
        this.atributoBonus = atributoBonus;
        this.valorBonus = valorBonus;
        this.atributoPenalidade = atributoPenalidade;
        this.valorPenalidade = valorPenalidade;
    }

    public String getNome() { return nome; }
    public String getAtributoBonus() { return atributoBonus; }
    public int getValorBonus() { return valorBonus; }
    public String getAtributoPenalidade() { return atributoPenalidade; }
    public int getValorPenalidade() { return valorPenalidade; }
}
