package br.com.etec.fullmetalcria.model;

import br.com.etec.fullmetalcria.model.enums.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Atributos.
 * Verifica cálculos de atributos-base + personalidade para todos os chassis.
 */
@DisplayName("Testes dos Atributos")
class AtributosTest {

    // ==================== CRIAÇÃO A PARTIR DE CHASSI ====================

    @Test
    @DisplayName("Deve gerar atributos corretos para Shoto/Humilde (sem modificador)")
    void fromChassi_shotoHumilde_atributosBase() {
        Atributos attr = Atributos.fromChassiEPersonalidade(Chassi.SHOTO, Personalidade.HUMILDE);
        assertEquals(14, attr.getDurabilidade());
        assertEquals(3, attr.getMira());
        assertEquals(3, attr.getVelocidade());
        assertEquals(3, attr.getCarapaca());
        assertEquals(3, attr.getDano());
        assertEquals(5, attr.getBateria());
    }

    @Test
    @DisplayName("Deve gerar atributos corretos para Shooter/Bruto (+1 Dano, -1 Mira)")
    void fromChassi_shooterBruto_comModificador() {
        Atributos attr = Atributos.fromChassiEPersonalidade(Chassi.SHOOTER, Personalidade.BRUTO);
        assertEquals(11, attr.getDurabilidade());
        assertEquals(3, attr.getMira());      // 4 - 1
        assertEquals(3, attr.getVelocidade());
        assertEquals(1, attr.getCarapaca());
        assertEquals(4, attr.getDano());      // 3 + 1
        assertEquals(6, attr.getBateria());
    }

    @Test
    @DisplayName("Deve gerar atributos corretos para Beast/Calmo (+1 Bateria, -1 Velocidade)")
    void fromChassi_beastCalmo_comModificador() {
        Atributos attr = Atributos.fromChassiEPersonalidade(Chassi.BEAST, Personalidade.CALMO);
        assertEquals(15, attr.getDurabilidade());
        assertEquals(4, attr.getMira());
        assertEquals(2, attr.getVelocidade()); // 3 - 1
        assertEquals(3, attr.getCarapaca());
        assertEquals(2, attr.getDano());
        assertEquals(6, attr.getBateria());    // 5 + 1
    }

    @Test
    @DisplayName("Deve gerar atributos corretos para Lancer/Tímido (+1 Velocidade, -1 Dano)")
    void fromChassi_lancerTimido_comModificador() {
        Atributos attr = Atributos.fromChassiEPersonalidade(Chassi.LANCER, Personalidade.TIMIDO);
        assertEquals(13, attr.getDurabilidade());
        assertEquals(3, attr.getMira());
        assertEquals(4, attr.getVelocidade()); // 3 + 1
        assertEquals(3, attr.getCarapaca());
        assertEquals(0, attr.getDano());       // 1 - 1
        assertEquals(6, attr.getBateria());
    }

    @Test
    @DisplayName("Deve gerar atributos para Shoto/Cuidadoso (+1 Carapaça, -1 Durabilidade)")
    void fromChassi_shotoCuidadoso_comModificador() {
        Atributos attr = Atributos.fromChassiEPersonalidade(Chassi.SHOTO, Personalidade.CUIDADOSO);
        assertEquals(13, attr.getDurabilidade()); // 14 - 1
        assertEquals(3, attr.getMira());
        assertEquals(3, attr.getVelocidade());
        assertEquals(4, attr.getCarapaca());      // 3 + 1
        assertEquals(3, attr.getDano());
        assertEquals(5, attr.getBateria());
    }

    // ==================== APLICAR MODIFICADOR ====================

    @Test
    @DisplayName("Deve aplicar modificador positivo corretamente")
    void aplicarModificador_positivo_incrementaAtributo() {
        Atributos attr = new Atributos(10, 3, 3, 3, 3, 5);
        attr.aplicarModificador("dano", 2);
        assertEquals(5, attr.getDano());
    }

    @Test
    @DisplayName("Deve aplicar modificador negativo corretamente")
    void aplicarModificador_negativo_decrementaAtributo() {
        Atributos attr = new Atributos(10, 3, 3, 3, 3, 5);
        attr.aplicarModificador("velocidade", -1);
        assertEquals(2, attr.getVelocidade());
    }

    @Test
    @DisplayName("Deve lançar exceção para atributo desconhecido")
    void aplicarModificador_atributoInvalido_lancaExcecao() {
        Atributos attr = new Atributos(10, 3, 3, 3, 3, 5);
        assertThrows(IllegalArgumentException.class, () ->
                attr.aplicarModificador("poder", 1));
    }

    @Test
    @DisplayName("Não deve aceitar bateria negativa")
    void setBateria_valorNegativo_lancaExcecao() {
        Atributos attr = new Atributos(10, 3, 3, 3, 3, 5);

        assertThrows(IllegalArgumentException.class, () -> attr.setBateria(-1));
        assertEquals(5, attr.getBateria());
    }

    // ==================== toString ====================

    @Test
    @DisplayName("Deve formatar toString corretamente")
    void toString_atributosNormais_formatoCorreto() {
        Atributos attr = new Atributos(14, 4, 5, 0, 4, 5);
        String resultado = attr.toString();
        assertTrue(resultado.contains("DUR=14"));
        assertTrue(resultado.contains("MIR=4"));
        assertTrue(resultado.contains("VEL=5"));
        assertTrue(resultado.contains("CAR=0"));
        assertTrue(resultado.contains("DAN=4"));
        assertTrue(resultado.contains("BAT=5"));
    }
}
