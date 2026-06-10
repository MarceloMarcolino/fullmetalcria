package br.com.etec.fullmetalcria.dao;

import br.com.etec.fullmetalcria.model.Cria;
import br.com.etec.fullmetalcria.model.Pivete;
import br.com.etec.fullmetalcria.model.enums.Chassi;
import br.com.etec.fullmetalcria.model.enums.Elemento;
import br.com.etec.fullmetalcria.model.enums.Personalidade;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de INTEGRAÇÃO do fluxo completo Pivete &rarr; Cria (modelo Normalized).
 *
 * <p>No banco, o Pivete é NORMALIZED: guarda apenas os {@code criaIds}. Ao
 * buscar, o {@link PiveteDAO} percorre os ids e carrega cada Cria do
 * {@link CriaDAO}. Este teste prova que {@code adicionarCria} insere o Cria,
 * referencia o id no Pivete e que a busca reconstrói o objeto completo.</p>
 *
 * <p><b>Pré-requisitos:</b> MongoDB rodando em {@code localhost:27017} e Java 17.
 * Usa o banco "fullmetalcria_test" (separado do de produção).</p>
 */
@DisplayName("Testes de Integração do PiveteDAO")
class PiveteDAOIntegrationTest {

    private static MongoDatabase testDb;
    private static PiveteDAO piveteDAO;
    private static CriaDAO criaDAO;

    @BeforeAll
    static void conectar() {
        testDb = MongoConnectionFactory.getDatabase("fullmetalcria_test");
        piveteDAO = new PiveteDAO(testDb);
        criaDAO = new CriaDAO(testDb);
    }

    @BeforeEach
    void limpar() {
        // Limpa as duas coleções para garantir isolamento entre os testes.
        piveteDAO.removerTodos();
        criaDAO.removerTodos();
    }

    @AfterAll
    static void desconectar() {
        if (piveteDAO != null) {
            piveteDAO.removerTodos();
        }
        if (criaDAO != null) {
            criaDAO.removerTodos();
        }
        MongoConnectionFactory.close();
    }

    @Test
    @DisplayName("Fluxo: criar Pivete e associar um Cria")
    void fluxoCompleto_piveteComCria() {
        // 1. Criar e inserir o Pivete (ainda sem Crias).
        Pivete pivete = new Pivete("Zezinho-T3ST", 12, "Vila Teste");
        piveteDAO.inserir(pivete); // seta pivete.getId()

        // 2. Criar um Cria e associá-lo (adicionarCria insere o Cria e referencia o id).
        Cria cria = new Cria("FL0W-T3ST", Chassi.BEAST, Elemento.FOGO, Personalidade.BRUTO);
        piveteDAO.adicionarCria(pivete.getId(), cria);

        // 3. Buscar o Pivete e verificar que o Cria veio junto (carregado pelo id).
        Pivete recuperado = piveteDAO.buscarPorNome("Zezinho-T3ST");

        assertNotNull(recuperado);
        assertFalse(recuperado.getCrias().isEmpty(), "o Pivete deveria ter 1 Cria associado");
        assertEquals("FL0W-T3ST", recuperado.getCrias().get(0).getNome());
    }
}
