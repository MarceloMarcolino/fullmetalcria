package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Cria.
 * Segue o padrão TDD: cada teste verifica um comportamento específico do Cria.
 *
 * Nomenclatura: metodo_cenario_resultadoEsperado
 */
@DisplayName("Testes do Cria")
class CriaTest {

    private Cria cria;

    @BeforeEach
    void setUp() {
        // Cria um Shoto/Vento/Astuto para cada teste
        cria = new Cria("T3ST", Chassi.SHOTO, Elemento.VENTO, Personalidade.ASTUTO);
    }

    // ==================== CRIAÇÃO ====================

    @Test
    @DisplayName("Deve criar um Cria com nome, chassi, elemento e personalidade")
    void constructor_dadosCompletos_criaCriaCorreto() {
        assertEquals("T3ST", cria.getNome());
        assertEquals(Chassi.SHOTO, cria.getChassi());
        assertEquals(Elemento.VENTO, cria.getElemento());
        assertEquals(Personalidade.ASTUTO, cria.getPersonalidade());
    }

    @Test
    @DisplayName("Deve calcular atributos com base no Chassi + Personalidade")
    void constructor_shotoAstuto_atributosCorretos() {
        Atributos attr = cria.getAtributos();
        // Shoto base: DUR=14, MIR=3, VEL=3, CAR=3, DAN=3, BAT=5
        // Astuto: +1 Mira, -1 Carapaça
        assertEquals(14, attr.getDurabilidade());
        assertEquals(4, attr.getMira());      // 3 + 1 (Astuto)
        assertEquals(3, attr.getVelocidade());
        assertEquals(2, attr.getCarapaca());  // 3 - 1 (Astuto)
        assertEquals(3, attr.getDano());
        assertEquals(5, attr.getBateria());
    }

    @Test
    @DisplayName("Deve iniciar no ranking Latão com 0 marcos")
    void constructor_novoCria_rankingLataoZeroMarcos() {
        assertEquals(Ranking.LATAO, cria.getRanking());
        assertEquals(0, cria.getMarcos());
    }

    @Test
    @DisplayName("Deve iniciar com 3 pontos de memória e 2 pontos de comando")
    void constructor_novoCria_pontosIniciais() {
        assertEquals(3, cria.getPontosMemoria());
        assertEquals(2, cria.getPontosComando());
    }

    @Test
    @DisplayName("Deve iniciar sem peças, técnicas e status")
    void constructor_novoCria_listasVazias() {
        assertTrue(cria.getPecas().isEmpty());
        assertTrue(cria.getTecnicas().isEmpty());
        assertTrue(cria.getStatusAtivos().isEmpty());
    }

    // ==================== PEÇAS ====================

    @Test
    @DisplayName("Deve equipar peça quando há memória suficiente")
    void equiparPeca_memoriaDisponivel_equipaComSucesso() {
        Peca ioio = new Peca("Iô Iô", 1, SlotPeca.PARTE_SUPERIOR, "+1 Dano");
        assertTrue(cria.equiparPeca(ioio));
        assertEquals(1, cria.getPecas().size());
        assertEquals(2, cria.memoriaDisponivel());
    }

    @Test
    @DisplayName("Deve usar toda a memória com peça de 3 pontos")
    void equiparPeca_pecaDe3Memoria_usaTodaMemoria() {
        Peca coleira = new Peca("Coleira", 3, SlotPeca.PARTE_SUPERIOR, "Modo Garoto Mau");
        cria.equiparPeca(coleira);
        assertEquals(0, cria.memoriaDisponivel());
    }

    @Test
    @DisplayName("Deve lançar exceção quando memória insuficiente")
    void equiparPeca_memoriaInsuficiente_lancaExcecao() {
        cria.equiparPeca(new Peca("Peça Grande", 2, SlotPeca.PARTE_SUPERIOR, "..."));
        assertThrows(IllegalStateException.class, () ->
                cria.equiparPeca(new Peca("Peça Maior", 2, SlotPeca.PARTE_INFERIOR, "...")));
    }

    // ==================== TÉCNICAS ====================

    @Test
    @DisplayName("Deve aprender técnica do mesmo elemento")
    void aprenderTecnica_mesmoElemento_aprendeComSucesso() {
        Tecnica t = new Tecnica("Ventania", Elemento.VENTO, 2, "Dano em área");
        assertTrue(cria.aprenderTecnica(t));
        assertEquals(1, cria.getTecnicas().size());
    }

    @Test
    @DisplayName("Deve aprender técnica de elemento Neutro")
    void aprenderTecnica_elementoNeutro_aprendeComSucesso() {
        Tecnica t = new Tecnica("Soco Básico", Elemento.NEUTRO, 1, "Dano básico");
        assertTrue(cria.aprenderTecnica(t));
    }

    @Test
    @DisplayName("Deve lançar exceção ao aprender técnica do elemento vantajoso")
    void aprenderTecnica_elementoVantajoso_lancaExcecao() {
        // Vento não pode aprender Terra (Terra tem vantagem sobre... não, espera)
        // Vento > Terra, então Vento não pode aprender do que tem vantagem sobre ele
        // Quem tem vantagem sobre Vento? Fogo (Fogo > Vento)
        // Então Vento não pode aprender Fogo
        Tecnica fogo = new Tecnica("Chama", Elemento.FOGO, 2, "Dano de fogo");
        assertThrows(IllegalStateException.class, () -> cria.aprenderTecnica(fogo));
    }

    // ==================== COMBATE ====================

    @Test
    @DisplayName("Não deve estar derrotado com Durabilidade > 0")
    void isDerrotado_durabilidadePositiva_retornaFalse() {
        assertFalse(cria.isDerrotado());
    }

    @Test
    @DisplayName("Deve estar derrotado com Durabilidade = 0")
    void isDerrotado_durabilidadeZero_retornaTrue() {
        cria.getAtributos().setDurabilidade(0);
        assertTrue(cria.isDerrotado());
    }

    @Test
    @DisplayName("Deve aplicar dano reduzido pela Carapaça")
    void receberDano_comCarapaca_reduzDano() {
        // Carapaça = 2 (Shoto 3 - 1 Astuto)
        int danoEfetivo = cria.receberDano(5);
        assertEquals(3, danoEfetivo); // 5 - 2 = 3
        assertEquals(11, cria.getAtributos().getDurabilidade()); // 14 - 3 = 11
    }

    @Test
    @DisplayName("Não deve causar dano negativo (mínimo 0)")
    void receberDano_danoMenorQueCarapaca_danoZero() {
        int danoEfetivo = cria.receberDano(1);
        assertEquals(0, danoEfetivo); // 1 - 2 = 0 (mínimo)
        assertEquals(14, cria.getAtributos().getDurabilidade()); // sem alteração
    }

    // ==================== STATUS ====================

    @Test
    @DisplayName("Deve aplicar status de batalha")
    void aplicarStatus_emChamas_adicionaStatus() {
        cria.aplicarStatus(StatusEfeito.EM_CHAMAS);
        assertTrue(cria.getStatusAtivos().contains(StatusEfeito.EM_CHAMAS));
    }

    @Test
    @DisplayName("Não deve duplicar status já aplicado")
    void aplicarStatus_statusJaAplicado_naoDuplica() {
        cria.aplicarStatus(StatusEfeito.EM_CHAMAS);
        cria.aplicarStatus(StatusEfeito.EM_CHAMAS);
        assertEquals(1, cria.getStatusAtivos().size());
    }

    @Test
    @DisplayName("Deve remover status de batalha")
    void removerStatus_statusExistente_removeComSucesso() {
        cria.aplicarStatus(StatusEfeito.PARALISADO);
        cria.removerStatus(StatusEfeito.PARALISADO);
        assertFalse(cria.getStatusAtivos().contains(StatusEfeito.PARALISADO));
    }

    @Test
    @DisplayName("Deve limpar tudo ao preparar para novo combate")
    void prepararParaCombate_comStatusEPecasAtivadas_limpaTudo() {
        cria.aplicarStatus(StatusEfeito.EM_CHAMAS);
        Peca peca = new Peca("Teste", 1, SlotPeca.PARTE_SUPERIOR, "Teste");
        cria.equiparPeca(peca);
        peca.ativar();

        cria.prepararParaCombate();

        assertTrue(cria.getStatusAtivos().isEmpty());
        assertFalse(peca.isAtivada());
    }

    // ==================== MARCOS E RANKING ====================

    @Test
    @DisplayName("Deve ganhar marcos")
    void ganharMarcos_ganhar3Marcos_total3() {
        cria.ganharMarcos(3);
        assertEquals(3, cria.getMarcos());
    }

    @Test
    @DisplayName("Deve acumular marcos")
    void ganharMarcos_ganhar2eMais3_total5() {
        cria.ganharMarcos(2);
        cria.ganharMarcos(3);
        assertEquals(5, cria.getMarcos());
    }

    @Test
    @DisplayName("Deve calcular atributos de Shooter/Bruto corretamente")
    void constructor_shooterBruto_atributosCorretos() {
        // Arrange: criar um Cria Shooter/Bruto
        Cria shooterBruto = new Cria("T35T", Chassi.SHOOTER, Elemento.FOGO, Personalidade.BRUTO);

        // Act: pegar os atributos calculados
        Atributos attr = shooterBruto.getAtributos();

        // Assert: verificar cada atributo
        // Shooter base: DUR=11, MIR=4, VEL=3, CAR=1, DAN=3, BAT=6
        // Bruto: +1 Dano, -1 Mira
        assertEquals(11, attr.getDurabilidade());  // 11 (sem alteração)
        assertEquals(3, attr.getMira());            // 4 - 1 (Bruto penalidade)
        assertEquals(3, attr.getVelocidade());      // 3 (sem alteração)
        assertEquals(1, attr.getCarapaca());         // 1 (sem alteração)
        assertEquals(4, attr.getDano());             // 3 + 1 (Bruto bônus)
        assertEquals(6, attr.getBateria());          // 6 (sem alteração)
    }

    @Test
    @DisplayName("Cria de Fogo não pode aprender técnica de Água")
    void aprenderTecnica_fogoAprendeAgua_lancaExcecao() {
        // Arrange: criar Cria de Fogo
        Cria criaFogo = new Cria("F0G0", Chassi.SHOTO, Elemento.FOGO, Personalidade.HUMILDE);

        // Arrange: criar técnica de Água
        Tecnica tecnicaAgua = new Tecnica("Tsunami", Elemento.AGUA, 3, "Ataque de água poderoso");

        // Act + Assert: deve lançar exceção ao tentar aprender
        assertThrows(IllegalStateException.class, () -> {
            criaFogo.aprenderTecnica(tecnicaAgua);
        });
    }

    // ==================== TESTES NOVOS (Aula 11/03) ====================

    @Test
    @DisplayName("Cria Neutro pode aprender técnica de qualquer elemento")
    void aprenderTecnica_criaNeutro_aprendeQualquerElemento() {
        // Arrange: Cria Neutro
        Cria criaNeutro = new Cria("N3UTR0", Chassi.SHOTO, Elemento.NEUTRO, Personalidade.HUMILDE);

        // Arrange: técnicas de elementos variados
        Tecnica fogo = new Tecnica("Chama", Elemento.FOGO, 2, "Dano de fogo");
        Tecnica agua = new Tecnica("Jato", Elemento.AGUA, 2, "Dano de água");

        // Act + Assert: ambas devem funcionar
        assertTrue(criaNeutro.aprenderTecnica(fogo));
        assertTrue(criaNeutro.aprenderTecnica(agua));
        assertEquals(2, criaNeutro.getTecnicas().size());
    }

    @Test
    @DisplayName("Custo efetivo deve ser +1 para elemento diferente")
    void custoEfetivo_elementoDiferente_custaMais1() {
        // Arrange: técnica de Fogo com custo 2
        Tecnica tecnicaFogo = new Tecnica("Chama", Elemento.FOGO, 2, "Ataque de fogo");

        // Assert: mesmo elemento = custo base
        assertEquals(2, tecnicaFogo.custoEfetivo(Elemento.FOGO));

        // Assert: elemento diferente = custo + 1
        assertEquals(3, tecnicaFogo.custoEfetivo(Elemento.VENTO));
        assertEquals(3, tecnicaFogo.custoEfetivo(Elemento.AGUA));

        // Arrange: técnica Neutra com custo 1
        Tecnica tecnicaNeutra = new Tecnica("Soco", Elemento.NEUTRO, 1, "Soco básico");

        // Assert: técnica Neutra = SEMPRE custo base
        assertEquals(1, tecnicaNeutra.custoEfetivo(Elemento.FOGO));
        assertEquals(1, tecnicaNeutra.custoEfetivo(Elemento.NEUTRO));
        assertEquals(1, tecnicaNeutra.custoEfetivo(Elemento.AGUA));
    }

    @Test
    @DisplayName("Peça quebrada não pode ser ativada")
    void ativar_pecaQuebrada_retornaFalse() {
        // Arrange: criar e quebrar a peça
        Peca peca = new Peca("Espada Frágil", 2, SlotPeca.PARTE_SUPERIOR, "Dano+1");
        peca.quebrar();

        // Act + Assert: ativar deve retornar false
        assertFalse(peca.ativar());
        assertTrue(peca.isQuebrada()); // Confirmar que continua quebrada
    }

    @Test
    @DisplayName("Peça consertada pode ser ativada novamente")
    void ativar_pecaConsertada_retornaTrue() {
        Peca peca = new Peca("Espada Frágil", 2, SlotPeca.PARTE_SUPERIOR, "Dano+1");
        peca.quebrar();
        peca.consertar();  // Conserta!

        assertTrue(peca.ativar()); // Agora pode ativar
        assertFalse(peca.isQuebrada()); // Não está mais quebrada
    }

    @Test
    @DisplayName("Deve calcular atributos de Beast/Cuidadoso corretamente")
    void constructor_beastCuidadoso_atributosCorretos() {
        // Arrange: criar Cria Beast/Cuidadoso
        Cria beastCuidadoso = new Cria("B34ST", Chassi.BEAST, Elemento.TERRA, Personalidade.CUIDADOSO);

        // Act: pegar atributos
        Atributos attr = beastCuidadoso.getAtributos();

        // Assert: Beast base + Cuidadoso
        // Beast:     DUR=15, MIR=4, VEL=3, CAR=3, DAN=2, BAT=5
        // Cuidadoso: +1 CAR, -1 DUR
        assertEquals(14, attr.getDurabilidade());  // 15 - 1
        assertEquals(4, attr.getMira());            // 4 (sem alteração)
        assertEquals(3, attr.getVelocidade());      // 3 (sem alteração)
        assertEquals(4, attr.getCarapaca());         // 3 + 1
        assertEquals(2, attr.getDano());             // 2 (sem alteração)
        assertEquals(5, attr.getBateria());           // 5 (sem alteração)
    }

    // ==================== COBERTURA (Desafio JaCoCo) ====================

    @Test
    @DisplayName("Memória disponível deve diminuir conforme equipa peças")
    void memoriaDisponivel_comPecas_calculaCorreto() {
        // Sem peças: 3 disponíveis
        assertEquals(3, cria.memoriaDisponivel());

        // Equipa peça de memória 1
        cria.equiparPeca(new Peca("Peça 1", 1, SlotPeca.PARTE_SUPERIOR, "..."));
        assertEquals(2, cria.memoriaDisponivel());

        // Equipa peça de memória 2
        cria.equiparPeca(new Peca("Peça 2", 2, SlotPeca.PARTE_INFERIOR, "..."));
        assertEquals(0, cria.memoriaDisponivel());
    }

    @Test
    @DisplayName("Preparar para combate deve funcionar mesmo sem peças nem status")
    void prepararParaCombate_semPecasNemStatus_naoQuebraNada() {
        // Caso vazio: nenhuma peça, nenhum status
        cria.prepararParaCombate();

        assertTrue(cria.getStatusAtivos().isEmpty());
    }

    @Test
    @DisplayName("Preparar para combate deve limpar múltiplos status e resetar múltiplas peças")
    void prepararParaCombate_comMultiplosStatusEPecas_limpaTudo() {
        // Aplica vários status
        cria.aplicarStatus(StatusEfeito.EM_CHAMAS);
        cria.aplicarStatus(StatusEfeito.CONGELADO);
        cria.aplicarStatus(StatusEfeito.ENVENENADO);

        // Equipa e ativa várias peças
        Peca peca1 = new Peca("Peça 1", 1, SlotPeca.PARTE_SUPERIOR, "...");
        Peca peca2 = new Peca("Peça 2", 1, SlotPeca.PARTE_INFERIOR, "...");
        cria.equiparPeca(peca1);
        cria.equiparPeca(peca2);
        peca1.ativar();
        peca2.ativar();

        cria.prepararParaCombate();

        assertTrue(cria.getStatusAtivos().isEmpty());
        assertFalse(peca1.isAtivada());
        assertFalse(peca2.isAtivada());
    }

    @Test
    @DisplayName("Ganhar 0 marcos deve manter marcos iguais")
    void ganharMarcos_zero_mantemIgual() {
        int marcosAntes = cria.getMarcos();

        cria.ganharMarcos(0);

        assertEquals(marcosAntes, cria.getMarcos());
    }

    @Test
    @DisplayName("Ganhar 100 marcos deve funcionar sem limite")
    void ganharMarcos_valorAlto_funcionaSemLimite() {
        cria.ganharMarcos(100);

        assertEquals(100, cria.getMarcos());
    }
}
