package br.com.etec.fullmetalcria;

import br.com.etec.fullmetalcria.dao.CriaDAO;
import br.com.etec.fullmetalcria.dao.MongoConnectionFactory;
import br.com.etec.fullmetalcria.dao.PiveteDAO;
import br.com.etec.fullmetalcria.model.*;
import br.com.etec.fullmetalcria.model.enums.*;

import java.util.List;

/**
 * Aplicação principal do sistema Full Metal Cria.
 * Demonstra CRUD básico com MongoDB usando os 9 Crias prontos do livro.
 *
 * Pré-requisito: MongoDB rodando em localhost:27017
 */
public class App {

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   ⚙️  FULL METAL CRIA — Sistema Digital  ");
        System.out.println("===========================================");
        System.out.println();

        try {
            CriaDAO criaDAO = new CriaDAO();
            PiveteDAO piveteDAO = new PiveteDAO();

            // Limpar dados anteriores para demonstração
            criaDAO.removerTodos();
            piveteDAO.removerTodos();

            // === CRIAR OS 9 CRIAS PRONTOS DO LIVRO ===
            System.out.println("📦 Inserindo os 9 Crias prontos do livro...");
            List<Cria> criasProntos = criarCriasProntos();
            criaDAO.inserirVarios(criasProntos);
            System.out.printf("   ✅ %d Crias inseridos com sucesso!%n%n", criasProntos.size());

            // === LISTAR TODOS ===
            System.out.println("📋 Lista de todos os Crias:");
            System.out.println("-------------------------------------------");
            List<Cria> todos = criaDAO.listarTodos();
            for (Cria c : todos) {
                System.out.printf("   🤖 %-12s | %-8s | %-9s | %s%n",
                        c.getNome(), c.getChassi(), c.getElemento().getNome(), c.getAtributos());
            }
            System.out.println();

            // === FILTRAR POR CHASSI ===
            System.out.println("🔍 Filtrando Crias do tipo BEAST:");
            List<Cria> beasts = criaDAO.listarPorChassi(Chassi.BEAST);
            for (Cria c : beasts) {
                System.out.printf("   🐾 %s (DUR:%d, DAN:%d)%n",
                        c.getNome(), c.getAtributos().getDurabilidade(), c.getAtributos().getDano());
            }
            System.out.println();

            // === FILTRAR POR ELEMENTO ===
            System.out.println("🌪️ Filtrando Crias do elemento VENTO:");
            List<Cria> ventos = criaDAO.listarPorElemento(Elemento.VENTO);
            for (Cria c : ventos) {
                System.out.printf("   💨 %s (%s)%n", c.getNome(), c.getChassi());
            }
            System.out.println();

            // === CRIAR UM PIVETE COM CRIA ===
            System.out.println("👦 Criando Pivete 'Zezinho' com o Cria R-9...");
            Pivete zezinho = new Pivete("Zezinho", 12, "Vila Mariana");
            zezinho.setDescricao("Um menino esperto que adora montar robôs de sucata");
            Cria r9 = criaDAO.buscarPorNome("R-9");
            if (r9 != null) {
                zezinho.adicionarCria(r9);
            }
            piveteDAO.inserir(zezinho);
            System.out.printf("   ✅ Pivete '%s' criado com %d Cria(s)!%n%n",
                    zezinho.getNome(), zezinho.getCrias().size());

            // === BUSCAR PIVETE E MOSTRAR CRIAS ===
            Pivete encontrado = piveteDAO.buscarPorNome("Zezinho");
            if (encontrado != null) {
                System.out.printf("📖 Pivete encontrado: %s, %d anos, %s%n",
                        encontrado.getNome(), encontrado.getIdade(), encontrado.getBairro());
                for (Cria c : encontrado.getCrias()) {
                    System.out.printf("   🤖 Cria: %s — %s/%s%n",
                            c.getNome(), c.getChassi(), c.getElemento().getNome());
                    System.out.printf("      Atributos: %s%n", c.getAtributos());
                    for (Tecnica t : c.getTecnicas()) {
                        System.out.printf("      ⚡ Técnica: %s (%s, Bat: %d)%n",
                                t.getNome(), t.getElemento().getNome(), t.getCustoBateria());
                    }
                    for (Peca p : c.getPecas()) {
                        System.out.printf("      🔧 Peça: %s (Mem: %d, %s)%n",
                                p.getNome(), p.getMemoria(), p.getSlot().getDescricao());
                    }
                }
            }

            // === DEMONSTRAR VANTAGEM ELEMENTAL ===
            System.out.println();
            System.out.println("⚡ Tabela de Vantagens Elementais:");
            System.out.println("-------------------------------------------");
            for (Elemento atacante : Elemento.values()) {
                for (Elemento defensor : Elemento.values()) {
                    if (atacante.temVantagemSobre(defensor)) {
                        System.out.printf("   %s > %s (+1 Dano, -1 Dano recebido)%n",
                                atacante.getNome(), defensor.getNome());
                    }
                }
            }

            // === ESTATÍSTICAS ===
            System.out.println();
            System.out.printf("📊 Total de Crias no banco: %d%n", criaDAO.contar());
            System.out.printf("📊 Total de Pivetes no banco: %d%n", piveteDAO.contar());

        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            System.err.println("   Verifique se o MongoDB está rodando em localhost:27017");
            e.printStackTrace();
        } finally {
            MongoConnectionFactory.close();
        }

        System.out.println();
        System.out.println("===========================================");
        System.out.println("   Fim da demonstração Full Metal Cria!    ");
        System.out.println("===========================================");
    }

    /**
     * Cria os 9 Crias prontos do livro Full Metal Cria com todos os atributos,
     * peças e técnicas exatamente como descritos no material original.
     */
    private static List<Cria> criarCriasProntos() {

        // === R-9 (Shooter/Vento) ===
        Cria r9 = new Cria("R-9", Chassi.SHOOTER, Elemento.VENTO, Personalidade.ASTUTO);
        r9.setAtributos(new Atributos(12, 4, 5, 0, 4, 5));
        r9.equiparPeca(new Peca("Bolinhas de Capotão", 2, SlotPeca.PARTE_SUPERIOR,
                "Uma vez por combate: +1 Dano no próximo Ataque!"));
        r9.aprenderTecnica(new Tecnica("Drible Místico", Elemento.VENTO, 2,
                "Na ação Cuidado!, rola com Sintonia"));
        r9.aprenderTecnica(new Tecnica("Chute do Dragão de Vento", Elemento.VENTO, 3,
                "Ataque! causando 1d6+1 Dano, atinge até 2 alvos, +1 Velocidade até fim da Rodada"));

        // === S4C1 (Shoto/Vento) ===
        Cria s4c1 = new Cria("S4C1", Chassi.SHOTO, Elemento.VENTO, Personalidade.ASTUTO);
        s4c1.setAtributos(new Atributos(14, 4, 4, 2, 4, 6));
        s4c1.equiparPeca(new Peca("Iô Iô", 1, SlotPeca.PARTE_SUPERIOR,
                "Uma vez por combate: +1 Dano no próximo Ataque!"));
        s4c1.equiparPeca(new Peca("Pião", 2, SlotPeca.PARTE_INFERIOR,
                "Pode gastar 1 Bateria para esquivar sem Pontos de Comando. A cada turno, rola 1d6: em 1-2 gasta automaticamente 1 Ponto de Comando para esquivar"));
        s4c1.aprenderTecnica(new Tecnica("Proteção dos Ventos Cortantes", Elemento.VENTO, 2,
                "+1 Velocidade, Mira, Dano por 1d3 rodadas. Quando atacado, pode gastar +1 Bateria para causar 1 Dano de volta"));
        s4c1.aprenderTecnica(new Tecnica("Redemoinho Espoleta", Elemento.VENTO, 4,
                "+2 Dano. Oponente fica Em Chamas por 1d3 rodadas"));

        // === Cêr0 (Lancer/Fogo) ===
        Cria cer0 = new Cria("Cêr0", Chassi.LANCER, Elemento.FOGO, Personalidade.ASTUTO);
        cer0.setAtributos(new Atributos(14, 4, 4, 2, 1, 6));
        cer0.equiparPeca(new Peca("Espada de Madeira", 3, SlotPeca.PARTE_SUPERIOR,
                "Ao errar: perde 1 Durabilidade → ganha +1 Mira e Dano permanente. Com +3 bônus, vira Lâmina de Carvão"));
        cer0.aprenderTecnica(new Tecnica("Espada Chamuscada", Elemento.FOGO, 2,
                "Ataque! com +2 Mira, causa Dano+. Pode gastar 1 Bateria/rodada para manter: +1 Mira e Dano"));
        cer0.aprenderTecnica(new Tecnica("Derreter", Elemento.FOGO, 3,
                "Requer Lâmina de Carvão. Todos Ataque! em Sincronia. Pode abrir mão da Sincronia: próximo acerto = Breaker; 20 natural = vitória automática"));

        // === Car4m3l0 (Beast/Neutro) ===
        Cria car4m3l0 = new Cria("Car4m3l0", Chassi.BEAST, Elemento.NEUTRO, Personalidade.CUIDADOSO);
        car4m3l0.setAtributos(new Atributos(14, 2, 3, 4, 2, 5));
        car4m3l0.equiparPeca(new Peca("Coleira", 3, SlotPeca.PARTE_SUPERIOR,
                "Quebra os códigos → modo 'Garoto Mau' por 1d6 turnos: +2 Mira/Velocidade/Dano, mas -1 Durabilidade/Rodada"));
        car4m3l0.aprenderTecnica(new Tecnica("Péx", Elemento.NEUTRO, 2,
                "Ataque de mordida, Dano+1. Alvo rola 1d6; em 1-2 perde 1 Ponto de Comando"));
        car4m3l0.aprenderTecnica(new Tecnica("Cavar", Elemento.TERRA, 4,
                "Cava escondendo-se no chão, imune a ataques não-Terra por 1 rodada. Ao sair: Ataque com +2 Mira e Dano"));

        // === Scor-P1 (Beast/Água) ===
        Cria scorP1 = new Cria("Scor-P1", Chassi.BEAST, Elemento.AGUA, Personalidade.BRUTO);
        scorP1.setAtributos(new Atributos(12, 3, 1, 2, 3, 5));
        scorP1.equiparPeca(new Peca("Garra Estraçalhadora", 2, SlotPeca.PARTE_SUPERIOR,
                "Uma vez por combate: +1 Dano no Ataque, reduz Carapaça do oponente em 1 até fim da próxima rodada"));
        scorP1.equiparPeca(new Peca("Cauda Venenosa", 1, SlotPeca.PARTE_INFERIOR,
                "Após acertar 4 ataques em um oponente, uma vez por combate: alvo fica Paralisado"));
        scorP1.aprenderTecnica(new Tecnica("Garra Gelada", Elemento.AGUA, 2,
                "Ataque causando Dano+2. Pelos próximos 1d3 turnos: +1 Dano"));
        scorP1.aprenderTecnica(new Tecnica("Enroscar", Elemento.NEUTRO, 3,
                "Ataque com +1 Mira. Ao acertar: sem dano, mas por 1d3 rodadas alvo fica -1 Velocidade, atacante ganha Sincronização"));

        // === KARR4NK4 (Beast/Terra) ===
        Cria karr4nk4 = new Cria("KARR4NK4", Chassi.BEAST, Elemento.TERRA, Personalidade.BRUTO);
        karr4nk4.setAtributos(new Atributos(14, 2, 3, 3, 3, 5));
        karr4nk4.equiparPeca(new Peca("Garras da Noite", 1, SlotPeca.PARTE_SUPERIOR,
                "Uma vez por combate, se oponente ≤ 50% Durabilidade: +1 Dano por 1 Rodada"));
        karr4nk4.equiparPeca(new Peca("Carranca", 2, SlotPeca.PARTE_SUPERIOR,
                "Uma vez por combate, assusta oponentes: todos os alvos escolhidos recebem -1 Dano, Mira, Carapaça até fim da próxima rodada"));
        karr4nk4.aprenderTecnica(new Tecnica("Chão Assustado", Elemento.TERRA, 2,
                "Ataque! Dano+1, alvo fica No Ar até fim do próximo turno"));
        karr4nk4.aprenderTecnica(new Tecnica("Postura do Carcaju", Elemento.NEUTRO, 1,
                "Por 1 Rodada: +1 Dano e +1 Carapaça"));

        // === Mar3-T4D4 (Lancer/Terra) ===
        Cria mar3T4d4 = new Cria("Mar3-T4D4", Chassi.LANCER, Elemento.TERRA, Personalidade.BRUTO);
        mar3T4d4.setAtributos(new Atributos(14, 0, 0, 3, 4, 4));
        mar3T4d4.equiparPeca(new Peca("Marreta do Pedreiro", 3, SlotPeca.PARTE_SUPERIOR,
                "Uma vez por combate, muda a arena para elemento Terra por 1d6 rodadas. Se já for Terra, dobra o efeito por 2 rodadas"));
        mar3T4d4.aprenderTecnica(new Tecnica("Levantar Muro", Elemento.TERRA, 1,
                "Golpeia o chão e levanta muro de pedra. Aguente! com Sincronização"));
        mar3T4d4.aprenderTecnica(new Tecnica("Desabamento", Elemento.TERRA, 4,
                "AoE: Dano+3 em todos os alvos"));

        // === P0P0 (Shoto/Neutro) ===
        Cria p0p0 = new Cria("P0P0", Chassi.SHOTO, Elemento.NEUTRO, Personalidade.HUMILDE);
        p0p0.setAtributos(new Atributos(13, 3, 3, 3, 3, 4));
        p0p0.equiparPeca(new Peca("Luvas de Boxe", 2, SlotPeca.PARTE_SUPERIOR,
                "Uma vez por combate: reduz custo em 1 para técnica com 'soco' no nome"));
        p0p0.equiparPeca(new Peca("Capacete de Boxeador", 1, SlotPeca.PARTE_SUPERIOR,
                "Modo pendular: +2 Velocidade em rolagens de Cuidado!"));
        p0p0.aprenderTecnica(new Tecnica("Soco Flamejante", Elemento.FOGO, 4,
                "Causa 1d6+1 Dano. Rola 1d6: em 4/5/6 alvo fica Em Chamas"));
        p0p0.aprenderTecnica(new Tecnica("Soco Eletrizante", Elemento.ELETRICO, 3,
                "Ataque com +2 Mira, causa 1d6 Dano. Rola 1d6: em 1-2 alvo perde 1 Bateria"));

        // === G00d (Shooter/Neutro) ===
        Cria g00d = new Cria("G00d", Chassi.SHOOTER, Elemento.NEUTRO, Personalidade.ASTUTO);
        g00d.setAtributos(new Atributos(11, 4, 4, 1, 4, 5));
        g00d.equiparPeca(new Peca("Capacete Mixer", 3, SlotPeca.PARTE_SUPERIOR,
                "A cada Ataque, rola 1d6 para determinar o tipo elemental: 1=Água, 2=Raio, 3=Fogo, 4=Vento, 5=Terra, 6=Neutro"));
        g00d.aprenderTecnica(new Tecnica("Bolinhas Grudentas Explosivas", Elemento.NEUTRO, 2,
                "Ataque! gruda bola no alvo. Ativa a qualquer momento (sem custo de Comando): 1 Dano imbloqueável. Sem limite de bolas. +1 Mira e Dano"));
        g00d.aprenderTecnica(new Tecnica("Chuva de Bolinhas", Elemento.NEUTRO, 3,
                "AoE Ataque! causando 1d6+1 Dano, dividido entre oponentes escolhidos"));

        return List.of(r9, s4c1, cer0, car4m3l0, scorP1, karr4nk4, mar3T4d4, p0p0, g00d);
    }
}
