package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.Chassi;
import br.com.etec.fullmetalcria.model.enums.Personalidade;

/**
 * Representa os 6 atributos de um Cria.
 * O valor final é calculado a partir do Chassi-base + modificador da Personalidade + distribuição.
 *
 * Atributos:
 * - Durabilidade (HP): o Cria é derrotado quando chega a 0
 * - Bateria (MP): energia para ativar técnicas
 * - Mira: precisão dos ataques
 * - Dano: poder de ataque
 * - Velocidade: agilidade e ordem de turno
 * - Carapaça: defesa/resistência
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Atributos.txt">Atributos</a>
 */
public class Atributos {

    private int durabilidade;
    private int mira;
    private int velocidade;
    private int carapaca;
    private int dano;
    private int bateria;

    public Atributos() {}

    public Atributos(int durabilidade, int mira, int velocidade, int carapaca, int dano, int bateria) {
        this.durabilidade = durabilidade;
        this.mira = mira;
        this.velocidade = velocidade;
        this.carapaca = carapaca;
        this.dano = dano;
        this.bateria = bateria;
    }

    /**
     * Cria os atributos-base a partir do Chassi, já aplicando o modificador da Personalidade.
     *
     * @param chassi        o chassi escolhido
     * @param personalidade a personalidade escolhida
     * @return Atributos com valores base + personalidade
     */
    public static Atributos fromChassiEPersonalidade(Chassi chassi, Personalidade personalidade) {
        Atributos atributos = new Atributos(
                chassi.getDurabilidade(),
                chassi.getMira(),
                chassi.getVelocidade(),
                chassi.getCarapaca(),
                chassi.getDano(),
                chassi.getBateria()
        );

        // Aplicar bônus da personalidade
        if (personalidade.getAtributoBonus() != null) {
            atributos.aplicarModificador(personalidade.getAtributoBonus(), personalidade.getValorBonus());
        }

        // Aplicar penalidade da personalidade
        if (personalidade.getAtributoPenalidade() != null) {
            atributos.aplicarModificador(personalidade.getAtributoPenalidade(), personalidade.getValorPenalidade());
        }

        return atributos;
    }

    /**
     * Aplica um modificador numérico a um atributo pelo nome.
     *
     * @param nomeAtributo nome do atributo (durabilidade, mira, velocidade, carapaca, dano, bateria)
     * @param valor        valor a somar (pode ser negativo)
     */
    public void aplicarModificador(String nomeAtributo, int valor) {
        switch (nomeAtributo.toLowerCase()) {
            case "durabilidade" -> this.durabilidade += valor;
            case "mira" -> this.mira += valor;
            case "velocidade" -> this.velocidade += valor;
            case "carapaca" -> this.carapaca += valor;
            case "dano" -> this.dano += valor;
            case "bateria" -> this.bateria += valor;
            default -> throw new IllegalArgumentException("Atributo desconhecido: " + nomeAtributo);
        }
    }

    // Getters e Setters

    public int getDurabilidade() { return durabilidade; }
    public void setDurabilidade(int durabilidade) { this.durabilidade = durabilidade; }

    public int getMira() { return mira; }
    public void setMira(int mira) { this.mira = mira; }

    public int getVelocidade() { return velocidade; }
    public void setVelocidade(int velocidade) { this.velocidade = velocidade; }

    public int getCarapaca() { return carapaca; }
    public void setCarapaca(int carapaca) { this.carapaca = carapaca; }

    public int getDano() { return dano; }
    public void setDano(int dano) { this.dano = dano; }

    public int getBateria() { return bateria; }
    public void setBateria(int bateria) { this.bateria = bateria; }

    @Override
    public String toString() {
        return String.format(
                "Atributos{DUR=%d, MIR=%d, VEL=%d, CAR=%d, DAN=%d, BAT=%d}",
                durabilidade, mira, velocidade, carapaca, dano, bateria
        );
    }
}
