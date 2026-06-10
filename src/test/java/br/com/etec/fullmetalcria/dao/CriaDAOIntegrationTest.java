package br.com.etec.fullmetalcria.dao;

import br.com.etec.fullmetalcria.model.Cria;
import br.com.etec.fullmetalcria.model.enums.Chassi;
import br.com.etec.fullmetalcria.model.enums.Elemento;
import br.com.etec.fullmetalcria.model.enums.Personalidade;
import br.com.etec.fullmetalcria.model.enums.Ranking;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de INTEGRAÇÃO do {@link CriaDAO} contra um MongoDB real.
 *
 * <p>Diferente dos testes unitários (que rodam em memória, sem banco), estes
 * conectam-se ao MongoDB e validam o caminho completo:
 * objeto Java &rarr; Document &rarr; MongoDB &rarr; Document &rarr; objeto Java.</p>
 *
 * <p>Usa um banco SEPARADO ("fullmetalcria_test") para nunca tocar nos 9 Crias
 * reais do banco de produção ("fullmetalcria").</p>
 *
 * <p><b>Pré-requisitos:</b> MongoDB rodando em {@code localhost:27017} e Java 17
 * (o agente do JaCoCo pode falhar em versões muito novas). Se o MongoDB não
 * estiver no ar, o {@code @BeforeAll} falha e todos os testes quebram.</p>
 *
 * Nomenclatura: metodo_cenario_resultadoEsperado
 */
@DisplayName("Testes de Integração do CriaDAO")
class CriaDAOIntegrationTest {

    private static MongoDatabase testDb;
    private static CriaDAO dao;
    private Cria criaTest;

    @BeforeAll
    static void conectar() {
        // Banco de TESTE, separado do de produção (fullmetalcria).
        testDb = MongoConnectionFactory.getDatabase("fullmetalcria_test");
        dao = new CriaDAO(testDb);
    }

    @BeforeEach
    void setUp() {
        dao.removerTodos(); // começa limpo, mesmo se um teste anterior abortou
        criaTest = new Cria("T3ST-INT", Chassi.SHOTO, Elemento.VENTO, Personalidade.ASTUTO);
        dao.inserir(criaTest); // devolve o id e já faz criaTest.setId(...)
    }

    @AfterEach
    void tearDown() {
        // Remove por ID (o DAO remove por id; o inserir() já preencheu o getId()).
        if (criaTest.getId() != null) {
            dao.remover(criaTest.getId());
        }
    }

    @AfterAll
    static void desconectar() {
        if (dao != null) {
            dao.removerTodos(); // limpa a coleção "crias" do banco de TESTE
        }
        MongoConnectionFactory.close();
    }

    @Test
    @DisplayName("Inserir e buscar: retorna o Cria correto")
    void inserirEBuscar_criaExistente_retornaCria() {
        Cria encontrado = dao.buscarPorNome("T3ST-INT");

        assertNotNull(encontrado, "o Cria inserido no @BeforeEach deveria ser encontrado");
        assertEquals("T3ST-INT", encontrado.getNome());
        assertEquals(Chassi.SHOTO, encontrado.getChassi());
        assertEquals(Elemento.VENTO, encontrado.getElemento());
    }

    @Test
    @DisplayName("Buscar inexistente: retorna null")
    void buscarPorNome_inexistente_retornaNull() {
        assertNull(dao.buscarPorNome("NAO-EXISTE"));
    }

    @Test
    @DisplayName("Atualizar ranking: persiste no banco")
    void atualizar_ranking_persisteNoBanco() {
        criaTest.setRanking(Ranking.BRONZE);
        dao.atualizar(criaTest); // usa criaTest.getId(), setado no inserir

        Cria recarregado = dao.buscarPorNome("T3ST-INT");
        assertEquals(Ranking.BRONZE, recarregado.getRanking());
    }

    @Test
    @DisplayName("Remover: apaga do banco")
    void remover_criaExistente_removeDoBanco() {
        dao.remover(criaTest.getId());

        assertNull(dao.buscarPorNome("T3ST-INT"));
        // o @AfterEach tenta remover de novo, mas é inofensivo (id já não existe)
    }

    @Test
    @DisplayName("listarTodos: inclui o Cria de teste")
    void listarTodos_comDadoDeTeste_incluiCria() {
        List<Cria> todos = dao.listarTodos();

        assertFalse(todos.isEmpty());
        assertTrue(todos.stream().anyMatch(c -> c.getNome().equals("T3ST-INT")));
    }
}
