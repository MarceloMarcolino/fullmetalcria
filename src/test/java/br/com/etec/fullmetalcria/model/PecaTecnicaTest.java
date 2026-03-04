package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.Elemento;
import br.com.etec.fullmetalcria.model.enums.SlotPeca;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para as classes Peca e Tecnica.
 */
@DisplayName("Testes de Peça e Técnica")
class PecaTecnicaTest {

    // ==================== PEÇA ====================

    @Nested
    @DisplayName("Testes da Peça")
    class PecaTests {

        private Peca peca;

        @BeforeEach
        void setUp() {
            peca = new Peca("Bolinhas de Capotão", 2, SlotPeca.PARTE_SUPERIOR,
                    "+1 Dano no próximo Ataque!");
        }

        @Test
        @DisplayName("Deve ativar peça pela primeira vez")
        void ativar_primeiraVez_retornaTrue() {
            assertTrue(peca.ativar());
            assertTrue(peca.isAtivada());
        }

        @Test
        @DisplayName("Não deve ativar peça já ativada")
        void ativar_jaAtivada_retornaFalse() {
            peca.ativar();
            assertFalse(peca.ativar());
        }

        @Test
        @DisplayName("Não deve ativar peça quebrada")
        void ativar_pecaQuebrada_retornaFalse() {
            peca.quebrar();
            assertFalse(peca.ativar());
        }

        @Test
        @DisplayName("Deve quebrar a peça")
        void quebrar_pecaNormal_ficaQuebrada() {
            peca.quebrar();
            assertTrue(peca.isQuebrada());
        }

        @Test
        @DisplayName("Deve consertar a peça")
        void consertar_pecaQuebrada_ficaFuncionando() {
            peca.quebrar();
            peca.consertar();
            assertFalse(peca.isQuebrada());
        }

        @Test
        @DisplayName("Deve resetar ativação para novo combate")
        void resetar_pecaAtivada_ficaDesativada() {
            peca.ativar();
            peca.resetarParaNovoCombate();
            assertFalse(peca.isAtivada());
        }
    }

    // ==================== TÉCNICA ====================

    @Nested
    @DisplayName("Testes da Técnica")
    class TecnicaTests {

        @Test
        @DisplayName("Custo normal quando elemento do Cria combina")
        void custoEfetivo_mesmoElemento_custoNormal() {
            Tecnica t = new Tecnica("Ventania", Elemento.VENTO, 2, "Dano de vento");
            assertEquals(2, t.custoEfetivo(Elemento.VENTO));
        }

        @Test
        @DisplayName("Custo +1 quando elemento do Cria não combina")
        void custoEfetivo_elementoDiferente_custoMais1() {
            Tecnica t = new Tecnica("Ventania", Elemento.VENTO, 2, "Dano de vento");
            assertEquals(3, t.custoEfetivo(Elemento.FOGO));
        }

        @Test
        @DisplayName("Técnica Neutro sempre tem custo normal")
        void custoEfetivo_tecnicaNeutra_custoNormal() {
            Tecnica t = new Tecnica("Soco", Elemento.NEUTRO, 1, "Soco básico");
            assertEquals(1, t.custoEfetivo(Elemento.FOGO));
            assertEquals(1, t.custoEfetivo(Elemento.AGUA));
        }

        @Test
        @DisplayName("Cria de qualquer elemento pode aprender Neutro")
        void podeAprender_tecnicaNeutra_sempre() {
            Tecnica t = new Tecnica("Soco", Elemento.NEUTRO, 1, "Soco");
            for (Elemento e : Elemento.values()) {
                assertTrue(t.podeSerAprendidaPor(e),
                        e.getNome() + " deve poder aprender técnica Neutra");
            }
        }

        @Test
        @DisplayName("Cria Neutro pode aprender qualquer técnica")
        void podeAprender_criaNeutra_aprenderQualquer() {
            for (Elemento elem : Elemento.values()) {
                Tecnica t = new Tecnica("T", elem, 1, "...");
                assertTrue(t.podeSerAprendidaPor(Elemento.NEUTRO),
                        "Cria Neutro deve poder aprender técnica de " + elem.getNome());
            }
        }

        @Test
        @DisplayName("Cria de Vento NÃO pode aprender Fogo (Fogo > Vento)")
        void podeAprender_fogoParaVento_false() {
            Tecnica fogo = new Tecnica("Chama", Elemento.FOGO, 2, "Dano de fogo");
            assertFalse(fogo.podeSerAprendidaPor(Elemento.VENTO));
        }

        @Test
        @DisplayName("Cria de Água NÃO pode aprender Elétrico (Elétrico > Água)")
        void podeAprender_eletricoParaAgua_false() {
            Tecnica eletrico = new Tecnica("Raio", Elemento.ELETRICO, 2, "Dano elétrico");
            assertFalse(eletrico.podeSerAprendidaPor(Elemento.AGUA));
        }
    }
}
