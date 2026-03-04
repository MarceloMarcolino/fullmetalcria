package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.Elemento;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Arena.
 * Verifica geração aleatória por d6 e bônus de técnica elemental.
 */
@DisplayName("Testes da Arena")
class ArenaTest {

    @Test
    @DisplayName("Deve gerar Riacho Chuvoso (Água) para d6 = 1")
    void gerarPorD6_resultado1_riachoChuvosoAgua() {
        Arena arena = Arena.gerarPorD6(1);
        assertEquals("Riacho Chuvoso", arena.getNome());
        assertEquals(Elemento.AGUA, arena.getElemento());
    }

    @Test
    @DisplayName("Deve gerar Rocha Magmática (Fogo) para d6 = 2")
    void gerarPorD6_resultado2_rochaMagmaticaFogo() {
        Arena arena = Arena.gerarPorD6(2);
        assertEquals("Rocha Magmática", arena.getNome());
        assertEquals(Elemento.FOGO, arena.getElemento());
    }

    @Test
    @DisplayName("Deve gerar Deserto Pedregoso (Terra) para d6 = 3")
    void gerarPorD6_resultado3_desertoPedregosoTerra() {
        Arena arena = Arena.gerarPorD6(3);
        assertEquals("Deserto Pedregoso", arena.getNome());
        assertEquals(Elemento.TERRA, arena.getElemento());
    }

    @Test
    @DisplayName("Deve gerar Pico do Mundo (Vento) para d6 = 4")
    void gerarPorD6_resultado4_picoDoMundoVento() {
        Arena arena = Arena.gerarPorD6(4);
        assertEquals("Pico do Mundo", arena.getNome());
        assertEquals(Elemento.VENTO, arena.getElemento());
    }

    @Test
    @DisplayName("Deve gerar Usina Tempestuosa (Elétrico) para d6 = 5")
    void gerarPorD6_resultado5_usinaTempestuosaEletrico() {
        Arena arena = Arena.gerarPorD6(5);
        assertEquals("Usina Tempestuosa", arena.getNome());
        assertEquals(Elemento.ELETRICO, arena.getElemento());
    }

    @Test
    @DisplayName("Deve gerar Arena Neutra para d6 = 6")
    void gerarPorD6_resultado6_arenaNeutra() {
        Arena arena = Arena.gerarPorD6(6);
        assertEquals("Arena Neutra", arena.getNome());
        assertEquals(Elemento.NEUTRO, arena.getElemento());
    }

    @Test
    @DisplayName("Deve lançar exceção para valor fora de 1-6")
    void gerarPorD6_resultado7_lancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> Arena.gerarPorD6(7));
    }

    @Test
    @DisplayName("Deve lançar exceção para valor 0")
    void gerarPorD6_resultado0_lancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> Arena.gerarPorD6(0));
    }

    // ==================== BÔNUS DE TÉCNICA ====================

    @Test
    @DisplayName("Deve dar bônus +1 quando técnica combina com arena")
    void bonusTecnica_elementoCombina_bonus1() {
        Arena arena = new Arena("Riacho Chuvoso", Elemento.AGUA);
        assertEquals(1, arena.bonusTecnica(Elemento.AGUA));
    }

    @Test
    @DisplayName("Não deve dar bônus quando técnica não combina com arena")
    void bonusTecnica_elementoNaoCombina_bonus0() {
        Arena arena = new Arena("Riacho Chuvoso", Elemento.AGUA);
        assertEquals(0, arena.bonusTecnica(Elemento.FOGO));
    }

    @Test
    @DisplayName("Arena Neutra não deve dar bônus para nenhum elemento")
    void bonusTecnica_arenaNeutra_semBonus() {
        Arena arena = new Arena("Arena Neutra", Elemento.NEUTRO);
        for (Elemento e : Elemento.values()) {
            assertEquals(0, arena.bonusTecnica(e),
                    "Arena Neutra não deve dar bônus para " + e.getNome());
        }
    }
}
