package br.com.etec.fullmetalcria.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Pivete — o jogador/criança que monta e controla um Cria.
 * No universo Full Metal Cria, os Pivetes são crianças e adolescentes
 * que constroem robôs de luta a partir de sucata e cristais elementais.
 *
 * @see <a href="Full Metal Cria/Capítulo 3/Jogando com os Pivetes.txt">Jogando com os Pivetes</a>
 */
public class Pivete {

    private String id;
    private String nome;
    private int idade;
    private String bairro;
    private String descricao;
    private List<Cria> crias;

    public Pivete() {
        this.crias = new ArrayList<>();
    }

    public Pivete(String nome, int idade, String bairro) {
        this();
        this.nome = nome;
        this.idade = idade;
        this.bairro = bairro;
    }

    /**
     * Adiciona um Cria à coleção do Pivete.
     *
     * @param cria o Cria a adicionar
     */
    public void adicionarCria(Cria cria) {
        this.crias.add(cria);
    }

    /**
     * Remove um Cria da coleção do Pivete pelo nome.
     *
     * @param nomeCria o nome do Cria a remover
     * @return true se removeu, false se não encontrou
     */
    public boolean removerCria(String nomeCria) {
        return crias.removeIf(c -> c.getNome().equalsIgnoreCase(nomeCria));
    }

    /**
     * Busca um Cria pelo nome.
     *
     * @param nomeCria o nome do Cria
     * @return o Cria encontrado ou null
     */
    public Cria buscarCria(String nomeCria) {
        return crias.stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nomeCria))
                .findFirst()
                .orElse(null);
    }

    // Getters e Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Cria> getCrias() { return crias; }
    public void setCrias(List<Cria> crias) { this.crias = crias; }

    @Override
    public String toString() {
        return String.format("Pivete{nome='%s', idade=%d, bairro='%s', crias=%d}",
                nome, idade, bairro, crias.size());
    }
}
