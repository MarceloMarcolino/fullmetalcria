package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.SlotPeca;

/**
 * Representa uma Peça equipável em um Cria.
 * Cada Cria possui 3 Pontos de Memória para equipar peças.
 * Cada peça só pode ser ativada uma vez por combate e pode ser quebrada.
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Peças.txt">Peças</a>
 */
public class Peca {

    private String id;
    private String nome;
    private int memoria;
    private SlotPeca slot;
    private String efeito;
    private boolean quebrada;
    private boolean ativada;

    public Peca() {}

    public Peca(String nome, int memoria, SlotPeca slot, String efeito) {
        this.nome = nome;
        this.memoria = memoria;
        this.slot = slot;
        this.efeito = efeito;
        this.quebrada = false;
        this.ativada = false;
    }

    /**
     * Ativa a peça. Só pode ser ativada uma vez por combate.
     *
     * @return true se ativou com sucesso, false se já estava ativada ou quebrada
     */
    public boolean ativar() {
        if (ativada || quebrada) return false;
        this.ativada = true;
        return true;
    }

    /**
     * Quebra a peça (via Breaker). Perde utilidade até ser consertada.
     */
    public void quebrar() {
        this.quebrada = true;
    }

    /**
     * Conserta a peça. Custa 1 Marco e leva 1 episódio.
     */
    public void consertar() {
        this.quebrada = false;
    }

    /**
     * Reseta o estado de ativação para um novo combate.
     */
    public void resetarParaNovoCombate() {
        this.ativada = false;
    }

    // Getters e Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getMemoria() { return memoria; }
    public void setMemoria(int memoria) { this.memoria = memoria; }

    public SlotPeca getSlot() { return slot; }
    public void setSlot(SlotPeca slot) { this.slot = slot; }

    public String getEfeito() { return efeito; }
    public void setEfeito(String efeito) { this.efeito = efeito; }

    public boolean isQuebrada() { return quebrada; }
    public void setQuebrada(boolean quebrada) { this.quebrada = quebrada; }

    public boolean isAtivada() { return ativada; }
    public void setAtivada(boolean ativada) { this.ativada = ativada; }

    @Override
    public String toString() {
        return String.format("Peca{nome='%s', mem=%d, slot=%s, quebrada=%s}",
                nome, memoria, slot, quebrada);
    }
}
