package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.Elemento;

/**
 * Representa uma Técnica especial que um Cria pode usar em combate.
 * Cada Cria começa conhecendo 2 Técnicas e pode aprender mais ao subir de Ranking.
 *
 * Regras elementais:
 * - Técnicas de um elemento só podem ser aprendidas por Crias daquele tipo
 * - Caso contrário, custam +1 Bateria
 * - Um Cria NUNCA pode aprender técnicas do elemento que tem vantagem sobre o dele
 * - Crias Neutro podem aprender qualquer tipo, mas pagam +1 Bateria para elementais
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Técnicas.txt">Técnicas</a>
 */
public class Tecnica {

    private String id;
    private String nome;
    private Elemento elemento;
    private int custoBateria;
    private String efeito;

    public Tecnica() {}

    public Tecnica(String nome, Elemento elemento, int custoBateria, String efeito) {
        this.nome = nome;
        this.elemento = elemento;
        this.custoBateria = custoBateria;
        this.efeito = efeito;
    }

    /**
     * Calcula o custo real de bateria para um Cria com determinado elemento.
     * Se o elemento da técnica não combina com o Cria, custa +1.
     *
     * @param elementoCria o elemento do Cria que vai usar a técnica
     * @return custo efetivo de bateria
     */
    public int custoEfetivo(Elemento elementoCria) {
        if (this.elemento == Elemento.NEUTRO) return custoBateria;
        if (this.elemento == elementoCria) return custoBateria;
        return custoBateria + 1; // +1 custo para elemento diferente
    }

    /**
     * Verifica se um Cria com dado elemento pode aprender esta técnica.
     * Um Cria não pode aprender técnicas do elemento que tem vantagem sobre o dele.
     *
     * @param elementoCria o elemento do Cria
     * @return true se o Cria pode aprender esta técnica
     */
    public boolean podeSerAprendidaPor(Elemento elementoCria) {
        if (this.elemento == Elemento.NEUTRO) return true;
        if (elementoCria == Elemento.NEUTRO) return true;
        // Não pode aprender do elemento que tem vantagem sobre o seu
        return !this.elemento.temVantagemSobre(elementoCria);
    }

    // Getters e Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Elemento getElemento() { return elemento; }
    public void setElemento(Elemento elemento) { this.elemento = elemento; }

    public int getCustoBateria() { return custoBateria; }
    public void setCustoBateria(int custoBateria) { this.custoBateria = custoBateria; }

    public String getEfeito() { return efeito; }
    public void setEfeito(String efeito) { this.efeito = efeito; }

    @Override
    public String toString() {
        return String.format("Tecnica{nome='%s', elem=%s, bat=%d}",
                nome, elemento.getNome(), custoBateria);
    }
}
