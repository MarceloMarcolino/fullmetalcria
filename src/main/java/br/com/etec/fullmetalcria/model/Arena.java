package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.Elemento;

/**
 * Representa uma Arena de batalha no universo Full Metal Cria.
 * Cada arena possui um elemento associado que concede +1 de bônus às técnicas desse elemento.
 *
 * Arenas (determinadas por 1d6):
 * 1 - Riacho Chuvoso (Água)
 * 2 - Rocha Magmática (Fogo)
 * 3 - Deserto Pedregoso (Terra)
 * 4 - Pico do Mundo (Vento)
 * 5 - Usina Tempestuosa (Elétrico)
 * 6 - Neutro (Neutro)
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Arena.txt">Arena</a>
 */
public class Arena {

    private String id;
    private String nome;
    private Elemento elemento;

    public Arena() {}

    public Arena(String nome, Elemento elemento) {
        this.nome = nome;
        this.elemento = elemento;
    }

    /**
     * Gera uma arena aleatória simulando a rolagem de 1d6.
     *
     * @param resultado valor de 1 a 6 (resultado do d6)
     * @return a Arena correspondente ao resultado
     */
    public static Arena gerarPorD6(int resultado) {
        return switch (resultado) {
            case 1 -> new Arena("Riacho Chuvoso", Elemento.AGUA);
            case 2 -> new Arena("Rocha Magmática", Elemento.FOGO);
            case 3 -> new Arena("Deserto Pedregoso", Elemento.TERRA);
            case 4 -> new Arena("Pico do Mundo", Elemento.VENTO);
            case 5 -> new Arena("Usina Tempestuosa", Elemento.ELETRICO);
            case 6 -> new Arena("Arena Neutra", Elemento.NEUTRO);
            default -> throw new IllegalArgumentException("Resultado do d6 deve ser entre 1 e 6: " + resultado);
        };
    }

    /**
     * Calcula o bônus de arena para uma técnica com dado elemento.
     * Quando a arena tem o mesmo elemento, a técnica recebe +1.
     *
     * @param elementoTecnica o elemento da técnica sendo usada
     * @return +1 se combinar com a arena, 0 caso contrário
     */
    public int bonusTecnica(Elemento elementoTecnica) {
        if (this.elemento == Elemento.NEUTRO) return 0;
        return (this.elemento == elementoTecnica) ? 1 : 0;
    }

    // Getters e Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Elemento getElemento() { return elemento; }
    public void setElemento(Elemento elemento) { this.elemento = elemento; }

    @Override
    public String toString() {
        return String.format("Arena{nome='%s', elemento=%s}", nome, elemento.getNome());
    }
}
