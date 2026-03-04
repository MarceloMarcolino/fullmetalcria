package br.com.etec.fullmetalcria.dao;

import br.com.etec.fullmetalcria.model.*;
import br.com.etec.fullmetalcria.model.enums.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para a entidade Pivete.
 * Realiza operações CRUD no MongoDB na coleção "pivetes".
 *
 * Pivetes podem ter seus Crias embutidos (embedded) ou referenciados.
 * Nesta implementação, os Crias do Pivete são referenciados pelo ID
 * para evitar duplicação e facilitar atualização independente.
 *
 * Estrutura do documento MongoDB:
 * {
 *   _id: ObjectId,
 *   nome: "Zezinho",
 *   idade: 12,
 *   bairro: "Vila Mariana",
 *   descricao: "Um menino curioso...",
 *   criaIds: ["id1", "id2"]
 * }
 */
public class PiveteDAO {

    private static final String COLLECTION_NAME = "pivetes";
    private final MongoCollection<Document> collection;
    private final CriaDAO criaDAO;

    public PiveteDAO() {
        MongoDatabase database = MongoConnectionFactory.getDatabase();
        this.collection = database.getCollection(COLLECTION_NAME);
        this.criaDAO = new CriaDAO(database);
    }

    public PiveteDAO(MongoDatabase database) {
        this.collection = database.getCollection(COLLECTION_NAME);
        this.criaDAO = new CriaDAO(database);
    }

    // ==================== CREATE ====================

    /**
     * Insere um novo Pivete no banco de dados.
     * Os Crias do Pivete são salvos separadamente e referenciados por ID.
     *
     * @param pivete o Pivete a inserir
     * @return o ID gerado pelo MongoDB
     */
    public String inserir(Pivete pivete) {
        // Primeiro, inserir os Crias que ainda não têm ID
        for (Cria cria : pivete.getCrias()) {
            if (cria.getId() == null) {
                criaDAO.inserir(cria);
            }
        }

        Document doc = piveteToDocument(pivete);
        collection.insertOne(doc);
        String id = doc.getObjectId("_id").toHexString();
        pivete.setId(id);
        return id;
    }

    // ==================== READ ====================

    /**
     * Busca um Pivete pelo ID, carregando também seus Crias.
     *
     * @param id o ObjectId em hexadecimal
     * @return o Pivete com seus Crias, ou null
     */
    public Pivete buscarPorId(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToPivete(doc) : null;
    }

    /**
     * Busca um Pivete pelo nome (case-insensitive).
     *
     * @param nome o nome do Pivete
     * @return o Pivete encontrado, ou null
     */
    public Pivete buscarPorNome(String nome) {
        Document doc = collection.find(Filters.regex("nome", "^" + nome + "$", "i")).first();
        return doc != null ? documentToPivete(doc) : null;
    }

    /**
     * Lista todos os Pivetes.
     *
     * @return lista de Pivetes com seus Crias
     */
    public List<Pivete> listarTodos() {
        List<Pivete> pivetes = new ArrayList<>();
        for (Document doc : collection.find()) {
            pivetes.add(documentToPivete(doc));
        }
        return pivetes;
    }

    /**
     * Lista Pivetes de um bairro.
     *
     * @param bairro o nome do bairro
     * @return lista de Pivetes do bairro
     */
    public List<Pivete> listarPorBairro(String bairro) {
        List<Pivete> pivetes = new ArrayList<>();
        for (Document doc : collection.find(Filters.regex("bairro", bairro, "i"))) {
            pivetes.add(documentToPivete(doc));
        }
        return pivetes;
    }

    /**
     * Conta o total de Pivetes.
     *
     * @return quantidade total
     */
    public long contar() {
        return collection.countDocuments();
    }

    // ==================== UPDATE ====================

    /**
     * Atualiza um Pivete existente.
     *
     * @param pivete o Pivete com dados atualizados
     * @return true se atualizou
     */
    public boolean atualizar(Pivete pivete) {
        if (pivete.getId() == null) {
            throw new IllegalArgumentException("Pivete deve ter um ID para ser atualizado");
        }

        Document doc = piveteToDocument(pivete);
        doc.remove("_id");

        return collection.replaceOne(
                Filters.eq("_id", new ObjectId(pivete.getId())),
                doc
        ).getModifiedCount() > 0;
    }

    /**
     * Adiciona um Cria ao Pivete (insere o Cria e atualiza referência).
     *
     * @param piveteId o ID do Pivete
     * @param cria     o Cria a adicionar
     * @return o ID do Cria inserido
     */
    public String adicionarCria(String piveteId, Cria cria) {
        if (cria.getId() == null) {
            criaDAO.inserir(cria);
        }

        collection.updateOne(
                Filters.eq("_id", new ObjectId(piveteId)),
                new Document("$push", new Document("criaIds", cria.getId()))
        );

        return cria.getId();
    }

    // ==================== DELETE ====================

    /**
     * Remove um Pivete pelo ID (não remove seus Crias).
     *
     * @param id o ID do Pivete
     * @return true se removeu
     */
    public boolean remover(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }

    /**
     * Remove todos os Pivetes.
     *
     * @return quantidade removida
     */
    public long removerTodos() {
        return collection.deleteMany(new Document()).getDeletedCount();
    }

    // ==================== CONVERSÃO ====================

    /**
     * Converte um Pivete para Document do MongoDB.
     */
    private Document piveteToDocument(Pivete pivete) {
        Document doc = new Document();
        doc.append("nome", pivete.getNome())
           .append("idade", pivete.getIdade())
           .append("bairro", pivete.getBairro())
           .append("descricao", pivete.getDescricao());

        // Referências aos Crias (IDs)
        List<String> criaIds = pivete.getCrias().stream()
                .map(Cria::getId)
                .filter(id -> id != null)
                .toList();
        doc.append("criaIds", criaIds);

        return doc;
    }

    /**
     * Converte um Document do MongoDB para Pivete, carregando os Crias referenciados.
     */
    @SuppressWarnings("unchecked")
    private Pivete documentToPivete(Document doc) {
        Pivete pivete = new Pivete();

        pivete.setId(doc.getObjectId("_id").toHexString());
        pivete.setNome(doc.getString("nome"));
        pivete.setIdade(doc.getInteger("idade", 0));
        pivete.setBairro(doc.getString("bairro"));
        pivete.setDescricao(doc.getString("descricao"));

        // Carregar Crias referenciados
        List<String> criaIds = doc.getList("criaIds", String.class);
        if (criaIds != null) {
            List<Cria> crias = new ArrayList<>();
            for (String criaId : criaIds) {
                Cria cria = criaDAO.buscarPorId(criaId);
                if (cria != null) {
                    crias.add(cria);
                }
            }
            pivete.setCrias(crias);
        }

        return pivete;
    }
}
