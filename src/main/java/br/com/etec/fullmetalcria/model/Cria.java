package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Cria — o robô de luta do universo Full Metal Cria.
 * Cada Cria é montado por um Pivete (jogador) e possui Chassi, Elemento,
 * Personalidade, Atributos, Peças e Técnicas.
 *
 * Regras de criação (Montando seu Cria):
 * 1. Escolher Chassi (Shoto, Shooter, Beast, Lancer)
 * 2. Escolher Personalidade (CD) — aplica modificadores de atributo
 * 3. Escolher Elemento (Água, Fogo, Terra, Vento, Elétrico, Neutro)
 * 4. Distribuir pontos-bônus do chassi conforme regra
 * 5. Escolher Peças (até 3 Pontos de Memória)
 * 6. Escolher 2 Técnicas
 * 7. Dar um nome legal misturando letras e números
 *
 * @see <a href="Full Metal Cria/Capítulo 4/Montando seu Cria.txt">Montando seu Cria</a>
 */
public class Cria {

    private String id;
    private String nome;
    private Chassi chassi;
    private Elemento elemento;
    private Personalidade personalidade;
    private Atributos atributos;
    private Ranking ranking;
    private int marcos;
    private List<Peca> pecas;
    private List<Tecnica> tecnicas;
    private List<StatusEfeito> statusAtivos;

    // Pontos de memória e comando (podem mudar com ranking)
    private int pontosMemoria;
    private int pontosComando;

    public Cria() {
        this.pecas = new ArrayList<>();
        this.tecnicas = new ArrayList<>();
        this.statusAtivos = new ArrayList<>();
        this.ranking = Ranking.LATAO;
        this.marcos = 0;
        this.pontosMemoria = 3;
        this.pontosComando = 2;
    }

    public Cria(String nome, Chassi chassi, Elemento elemento, Personalidade personalidade) {
        this();
        this.nome = nome;
        this.chassi = chassi;
        this.elemento = elemento;
        this.personalidade = personalidade;
        this.atributos = Atributos.fromChassiEPersonalidade(chassi, personalidade);
    }

    /**
     * Verifica se ainda há pontos de memória disponíveis para equipar uma peça.
     *
     * @return pontos de memória livres
     */
    public int memoriaDisponivel() {
        int memoriaUsada = pecas.stream().mapToInt(Peca::getMemoria).sum();
        return pontosMemoria - memoriaUsada;
    }

    /**
     * Tenta equipar uma peça no Cria.
     *
     * @param peca a peça a equipar
     * @return true se equipou com sucesso
     * @throws IllegalStateException se memória insuficiente
     */
    public boolean equiparPeca(Peca peca) {
        if (peca.getMemoria() > memoriaDisponivel()) {
            throw new IllegalStateException(
                    String.format("Memória insuficiente! Disponível: %d, Necessário: %d",
                            memoriaDisponivel(), peca.getMemoria()));
        }
        return pecas.add(peca);
    }

    /**
     * Tenta aprender uma nova técnica.
     *
     * @param tecnica a técnica a aprender
     * @return true se aprendeu com sucesso
     * @throws IllegalStateException se a técnica não pode ser aprendida pelo elemento do Cria
     */
    public boolean aprenderTecnica(Tecnica tecnica) {
        if (!tecnica.podeSerAprendidaPor(this.elemento)) {
            throw new IllegalStateException(
                    String.format("Cria de elemento %s não pode aprender a técnica '%s' de elemento %s!",
                            elemento.getNome(), tecnica.getNome(), tecnica.getElemento().getNome()));
        }
        return tecnicas.add(tecnica);
    }

    /**
     * Verifica se o Cria está derrotado (Durabilidade <= 0).
     *
     * @return true se derrotado
     */
    public boolean isDerrotado() {
        return atributos.getDurabilidade() <= 0;
    }

    /**
     * Recebe dano, reduzindo a Durabilidade. Considera Carapaça como defesa.
     *
     * @param danoRecebido quantidade de dano bruto
     * @return dano efetivo aplicado
     */
    public int receberDano(int danoRecebido) {
        int danoEfetivo = Math.max(0, danoRecebido - atributos.getCarapaca());
        atributos.setDurabilidade(atributos.getDurabilidade() - danoEfetivo);
        return danoEfetivo;
    }

    /**
     * Aplica um status de batalha ao Cria.
     *
     * @param status o status a aplicar
     */
    public void aplicarStatus(StatusEfeito status) {
        if (!statusAtivos.contains(status)) {
            statusAtivos.add(status);
        }
    }

    /**
     * Remove um status de batalha do Cria.
     *
     * @param status o status a remover
     */
    public void removerStatus(StatusEfeito status) {
        statusAtivos.remove(status);
    }

    /**
     * Prepara o Cria para um novo combate: reseta peças ativadas e status.
     */
    public void prepararParaCombate() {
        statusAtivos.clear();
        pecas.forEach(Peca::resetarParaNovoCombate);
    }

    /**
     * Adiciona marcos ao Cria (máximo 3 por episódio).
     *
     * @param quantidade marcos a adicionar
     */
    public void ganharMarcos(int quantidade) {
        this.marcos += quantidade;
    }

    // Getters e Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Chassi getChassi() { return chassi; }
    public void setChassi(Chassi chassi) { this.chassi = chassi; }

    public Elemento getElemento() { return elemento; }
    public void setElemento(Elemento elemento) { this.elemento = elemento; }

    public Personalidade getPersonalidade() { return personalidade; }
    public void setPersonalidade(Personalidade personalidade) { this.personalidade = personalidade; }

    public Atributos getAtributos() { return atributos; }
    public void setAtributos(Atributos atributos) { this.atributos = atributos; }

    public Ranking getRanking() { return ranking; }
    public void setRanking(Ranking ranking) { this.ranking = ranking; }

    public int getMarcos() { return marcos; }
    public void setMarcos(int marcos) { this.marcos = marcos; }

    public List<Peca> getPecas() { return pecas; }
    public void setPecas(List<Peca> pecas) { this.pecas = pecas; }

    public List<Tecnica> getTecnicas() { return tecnicas; }
    public void setTecnicas(List<Tecnica> tecnicas) { this.tecnicas = tecnicas; }

    public List<StatusEfeito> getStatusAtivos() { return statusAtivos; }
    public void setStatusAtivos(List<StatusEfeito> statusAtivos) { this.statusAtivos = statusAtivos; }

    public int getPontosMemoria() { return pontosMemoria; }
    public void setPontosMemoria(int pontosMemoria) { this.pontosMemoria = pontosMemoria; }

    public int getPontosComando() { return pontosComando; }
    public void setPontosComando(int pontosComando) { this.pontosComando = pontosComando; }

    @Override
    public String toString() {
        return String.format(
                "Cria{nome='%s', chassi=%s, elem=%s, pers=%s, ranking=%s, %s}",
                nome, chassi, elemento.getNome(), personalidade.getNome(),
                ranking.getNome(), atributos
        );
    }
}
