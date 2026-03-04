package br.com.etec.fullmetalcria.model.enums;

/**
 * Os Rankings do sistema Full Metal Cria.
 * Cada ranking exige um número de Marcos para ser alcançado e concede benefícios.
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Se tornando Full Metal.txt">Se tornando Full Metal</a>
 */
public enum Ranking {

    /** Pode participar de torneios oficiais. Precisa de 7 Marcos para subir. */
    LATAO("Latão", 7, "Pode participar de torneios oficiais"),

    /** +1 Atributo e +1 Técnica. Precisa de 5 Marcos para subir. */
    BRONZE("Bronze", 5, "+1 ponto de Atributo e +1 Técnica"),

    /** +1 Ponto de Comando e +1 Técnica. Precisa de 7 Marcos para subir. */
    PRATA("Prata", 7, "+1 Ponto de Comando e +1 Técnica"),

    /** +1 Ponto de Memória e +1 Técnica. Precisa de 10 Marcos para subir. */
    OURO("Ouro", 10, "+1 Ponto de Memória e +1 Técnica"),

    /** +1 Ponto de Comando e +1 Técnica. Vencer a competição Full Metal Cria. */
    FULL_METAL("Full Metal", 0, "+1 Ponto de Comando e +1 Técnica");

    private final String nome;
    private final int marcosNecessarios;
    private final String beneficio;

    Ranking(String nome, int marcosNecessarios, String beneficio) {
        this.nome = nome;
        this.marcosNecessarios = marcosNecessarios;
        this.beneficio = beneficio;
    }

    public String getNome() { return nome; }
    public int getMarcosNecessarios() { return marcosNecessarios; }
    public String getBeneficio() { return beneficio; }
}
