package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.Elemento;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o enum Elemento.
 * Verifica o sistema de vantagens/desvantagens elementais.
 *
 * Ciclo: Água > Fogo > Vento > Terra > Elétrico > Água
 * Neutro: não participa de vantagem/desvantagem
 */
@DisplayName("Testes dos Elementos")
class ElementoTest {

    // ==================== VANTAGENS ====================

    @Test
    @DisplayName("Água deve ter vantagem sobre Fogo")
    void temVantagem_aguaSobreFogo_true() {
        assertTrue(Elemento.AGUA.temVantagemSobre(Elemento.FOGO));
    }

    @Test
    @DisplayName("Fogo deve ter vantagem sobre Vento")
    void temVantagem_fogoSobreVento_true() {
        assertTrue(Elemento.FOGO.temVantagemSobre(Elemento.VENTO));
    }

    @Test
    @DisplayName("Vento deve ter vantagem sobre Terra")
    void temVantagem_ventoSobreTerra_true() {
        assertTrue(Elemento.VENTO.temVantagemSobre(Elemento.TERRA));
    }

    @Test
    @DisplayName("Terra deve ter vantagem sobre Elétrico")
    void temVantagem_terraSobreEletrico_true() {
        assertTrue(Elemento.TERRA.temVantagemSobre(Elemento.ELETRICO));
    }

    @Test
    @DisplayName("Elétrico deve ter vantagem sobre Água")
    void temVantagem_eletricoSobreAgua_true() {
        assertTrue(Elemento.ELETRICO.temVantagemSobre(Elemento.AGUA));
    }

    // ==================== SEM VANTAGEM ====================

    @Test
    @DisplayName("Fogo NÃO deve ter vantagem sobre Água")
    void temVantagem_fogoSobreAgua_false() {
        assertFalse(Elemento.FOGO.temVantagemSobre(Elemento.AGUA));
    }

    @Test
    @DisplayName("Neutro NÃO deve ter vantagem sobre ninguém")
    void temVantagem_neutroSobreQualquer_false() {
        for (Elemento e : Elemento.values()) {
            assertFalse(Elemento.NEUTRO.temVantagemSobre(e),
                    "Neutro não deve ter vantagem sobre " + e.getNome());
        }
    }

    @Test
    @DisplayName("Ninguém deve ter vantagem sobre Neutro")
    void temVantagem_qualquerSobreNeutro_false() {
        for (Elemento e : Elemento.values()) {
            assertFalse(e.temVantagemSobre(Elemento.NEUTRO),
                    e.getNome() + " não deve ter vantagem sobre Neutro");
        }
    }

    // ==================== DESVANTAGENS ====================

    @Test
    @DisplayName("Fogo deve ter desvantagem contra Água")
    void temDesvantagem_fogoContraAgua_true() {
        assertTrue(Elemento.FOGO.temDesvantagemContra(Elemento.AGUA));
    }

    // ==================== MODIFICADOR DE DANO ====================

    @Test
    @DisplayName("Modificador de Água contra Fogo deve ser +1")
    void modificadorDano_vantagemElemental_mais1() {
        assertEquals(1, Elemento.AGUA.modificadorDano(Elemento.FOGO));
    }

    @Test
    @DisplayName("Modificador de Fogo contra Água deve ser -1")
    void modificadorDano_desvantagemElemental_menos1() {
        assertEquals(-1, Elemento.FOGO.modificadorDano(Elemento.AGUA));
    }

    @Test
    @DisplayName("Modificador de Neutro contra qualquer deve ser 0")
    void modificadorDano_neutro_zero() {
        assertEquals(0, Elemento.NEUTRO.modificadorDano(Elemento.FOGO));
    }

    @Test
    @DisplayName("Modificador entre mesmos elementos deve ser 0")
    void modificadorDano_mesmoElemento_zero() {
        assertEquals(0, Elemento.FOGO.modificadorDano(Elemento.FOGO));
    }
}
