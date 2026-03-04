package br.com.etec.fullmetalcria.model.enums;

/**
 * Os slots disponíveis para equipar Peças em um Cria.
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Peças.txt">Peças</a>
 */
public enum SlotPeca {

    /** Braços, cabeça, parte superior do corpo */
    PARTE_SUPERIOR("Parte Superior"),

    /** Pernas, cauda, parte inferior do corpo */
    PARTE_INFERIOR("Parte Inferior");

    private final String descricao;

    SlotPeca(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }
}
