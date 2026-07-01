package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RegrasDeNegocioAceitacaoTest {
    @Test
    @DisplayName("Fluxo valido: montar um Cria completo")
    void fluxo_montarCriaCompleto_sucesso() {
        Cria cria = new Cria("BUILD-OK", Chassi.BEAST,
                            Elemento.TERRA, Personalidade.HUMILDE);

        assertTrue(cria.equiparPeca(
            new Peca("Carcaca", 2, SlotPeca.PARTE_SUPERIOR, "+Carapaca")));
        assertTrue(cria.equiparPeca(
            new Peca("Patas", 1, SlotPeca.PARTE_INFERIOR, "+Velocidade")));
        assertEquals(0, cria.memoriaDisponivel());

        assertTrue(cria.aprenderTecnica(
            new Tecnica("Terremoto", Elemento.TERRA, 2, "dano de terra")));

        assertEquals(2, cria.getPecas().size());
        assertEquals(1, cria.getTecnicas().size());
    }

    @Test
    @DisplayName("Equipar peça além da memória deve lançar exceção")
    void memoria_acimaDoLimite_lancaExcecao() {
        Cria cria = new Cria("MEM-T3ST", Chassi.SHOTO,
                             Elemento.VENTO, Personalidade.ASTUTO);

        cria.equiparPeca(new Peca("Blindagem", 3,
            SlotPeca.PARTE_SUPERIOR, "+Carapaca"));
        assertEquals(0, cria.memoriaDisponivel());

        Peca turbo = new Peca("Turbo", 1,
            SlotPeca.PARTE_INFERIOR, "+Velocidade");
        assertThrows(IllegalStateException.class,
            () -> cria.equiparPeca(turbo));
    }

    @Test
    @DisplayName("Cria de Fogo nao aprende tecnica de Agua")
    void elemento_semPreRequisito_lancaExcecao() {
        Cria fogo = new Cria("F0G0", Chassi.LANCER,
                             Elemento.FOGO, Personalidade.BRUTO);

        Tecnica tecnicaAgua = new Tecnica("Jato d'Agua",
            Elemento.AGUA, 3, "dano de agua");

        assertThrows(IllegalStateException.class,
            () -> fogo.aprenderTecnica(tecnicaAgua));
    }

    @Test
    @DisplayName("Tecnica de elemento diferente custa +1 bateria")
    void custo_elementoDiferente_somaUm() {
        Tecnica terremoto = new Tecnica("Terremoto",
            Elemento.TERRA, 2, "dano de terra");

        assertEquals(2, terremoto.custoEfetivo(Elemento.TERRA));
        assertEquals(3, terremoto.custoEfetivo(Elemento.FOGO));
    }
}
