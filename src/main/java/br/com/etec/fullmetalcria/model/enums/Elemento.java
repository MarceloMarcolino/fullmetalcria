package br.com.etec.fullmetalcria.model.enums;

/**
 * Os 6 Elementos disponíveis no universo Full Metal Cria.
 * O tipo forte causa +1 Dano e recebe -1 Dano contra o tipo fraco.
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Os Elementos e os Cristais.txt">Os Elementos</a>
 */
public enum Elemento {

    AGUA("Água"),
    FOGO("Fogo"),
    TERRA("Terra"),
    VENTO("Vento"),
    ELETRICO("Elétrico"),
    NEUTRO("Neutro");

    private final String nome;

    Elemento(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }

    /**
     * Verifica se este elemento tem vantagem sobre o elemento alvo.
     * Água > Fogo, Fogo > Vento, Vento > Terra, Terra > Elétrico, Elétrico > Água.
     * Neutro não participa de vantagem/desvantagem.
     *
     * @param alvo o elemento do oponente
     * @return true se este elemento tem vantagem sobre o alvo
     */
    public boolean temVantagemSobre(Elemento alvo) {
        if (this == NEUTRO || alvo == NEUTRO) return false;

        return switch (this) {
            case AGUA -> alvo == FOGO;
            case FOGO -> alvo == VENTO;
            case VENTO -> alvo == TERRA;
            case TERRA -> alvo == ELETRICO;
            case ELETRICO -> alvo == AGUA;
            default -> false;
        };
    }

    /**
     * Verifica se este elemento tem desvantagem contra o elemento alvo.
     *
     * @param alvo o elemento do oponente
     * @return true se este elemento tem desvantagem contra o alvo
     */
    public boolean temDesvantagemContra(Elemento alvo) {
        return alvo.temVantagemSobre(this);
    }

    /**
     * Calcula o modificador de dano elemental.
     *
     * @param alvo o elemento do oponente
     * @return +1 se vantagem, -1 se desvantagem, 0 se neutro
     */
    public int modificadorDano(Elemento alvo) {
        if (temVantagemSobre(alvo)) return 1;
        if (temDesvantagemContra(alvo)) return -1;
        return 0;
    }
}
