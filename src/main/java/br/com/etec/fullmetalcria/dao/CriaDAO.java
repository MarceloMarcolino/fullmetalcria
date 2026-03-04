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
 * Data Access Object para a entidade Cria.
 * Realiza operações CRUD no MongoDB na coleção "crias".
 *
 * Estrutura do documento MongoDB:
 * {
 *   _id: ObjectId,
 *   nome: "R-9",
 *   chassi: "SHOOTER",
 *   elemento: "VENTO",
 *   personalidade: "ASTUTO",
 *   ranking: "LATAO",
 *   marcos: 0,
 *   pontosMemoria: 3,
 *   pontosComando: 2,
 *   atributos: { durabilidade: 12, mira: 4, velocidade: 5, carapaca: 0, dano: 4, bateria: 5 },
 *   pecas: [ { nome: "...", memoria: 2, slot: "PARTE_SUPERIOR", efeito: "..." } ],
 *   tecnicas: [ { nome: "...", elemento: "VENTO", custoBateria: 2, efeito: "..." } ]
 * }
 */
public class CriaDAO {

    private static final String COLLECTION_NAME = "crias";
    private final MongoCollection<Document> collection;

    public CriaDAO() {
        MongoDatabase database = MongoConnectionFactory.getDatabase();
        this.collection = database.getCollection(COLLECTION_NAME);
    }

    public CriaDAO(MongoDatabase database) {
        this.collection = database.getCollection(COLLECTION_NAME);
    }

    // ==================== CREATE ====================

    /**
     * Insere um novo Cria no banco de dados.
     *
     * @param cria o Cria a inserir
     * @return o ID gerado pelo MongoDB
     */
    public String inserir(Cria cria) {
        Document doc = criaToDocument(cria);
        collection.insertOne(doc);
        String id = doc.getObjectId("_id").toHexString();
        cria.setId(id);
        return id;
    }

    /**
     * Insere vários Crias de uma vez.
     *
     * @param crias lista de Crias
     */
    public void inserirVarios(List<Cria> crias) {
        List<Document> docs = crias.stream()
                .map(this::criaToDocument)
                .toList();
        collection.insertMany(docs);
        for (int i = 0; i < crias.size(); i++) {
            crias.get(i).setId(docs.get(i).getObjectId("_id").toHexString());
        }
    }

    // ==================== READ ====================

    /**
     * Busca um Cria pelo ID.
     *
     * @param id o ObjectId em hexadecimal
     * @return o Cria encontrado, ou null
     */
    public Cria buscarPorId(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToCria(doc) : null;
    }

    /**
     * Busca um Cria pelo nome (case-insensitive).
     *
     * @param nome o nome do Cria
     * @return o Cria encontrado, ou null
     */
    public Cria buscarPorNome(String nome) {
        Document doc = collection.find(Filters.regex("nome", "^" + nome + "$", "i")).first();
        return doc != null ? documentToCria(doc) : null;
    }

    /**
     * Lista todos os Crias no banco de dados.
     *
     * @return lista de Crias
     */
    public List<Cria> listarTodos() {
        List<Cria> crias = new ArrayList<>();
        for (Document doc : collection.find()) {
            crias.add(documentToCria(doc));
        }
        return crias;
    }

    /**
     * Lista Crias filtrados por chassi.
     *
     * @param chassi o tipo de chassi
     * @return lista de Crias com esse chassi
     */
    public List<Cria> listarPorChassi(Chassi chassi) {
        List<Cria> crias = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("chassi", chassi.name()))) {
            crias.add(documentToCria(doc));
        }
        return crias;
    }

    /**
     * Lista Crias filtrados por elemento.
     *
     * @param elemento o tipo de elemento
     * @return lista de Crias com esse elemento
     */
    public List<Cria> listarPorElemento(Elemento elemento) {
        List<Cria> crias = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("elemento", elemento.name()))) {
            crias.add(documentToCria(doc));
        }
        return crias;
    }

    /**
     * Lista Crias filtrados por ranking.
     *
     * @param ranking o ranking desejado
     * @return lista de Crias com esse ranking
     */
    public List<Cria> listarPorRanking(Ranking ranking) {
        List<Cria> crias = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("ranking", ranking.name()))) {
            crias.add(documentToCria(doc));
        }
        return crias;
    }

    /**
     * Conta o total de Crias no banco de dados.
     *
     * @return quantidade total
     */
    public long contar() {
        return collection.countDocuments();
    }

    // ==================== UPDATE ====================

    /**
     * Atualiza um Cria existente no banco de dados.
     *
     * @param cria o Cria com dados atualizados (deve ter ID)
     * @return true se atualizou pelo menos um documento
     */
    public boolean atualizar(Cria cria) {
        if (cria.getId() == null) {
            throw new IllegalArgumentException("Cria deve ter um ID para ser atualizado");
        }

        Document doc = criaToDocument(cria);
        doc.remove("_id"); // Não atualizar o _id

        return collection.replaceOne(
                Filters.eq("_id", new ObjectId(cria.getId())),
                doc
        ).getModifiedCount() > 0;
    }

    /**
     * Atualiza apenas os atributos de um Cria.
     *
     * @param id        o ID do Cria
     * @param atributos os novos atributos
     * @return true se atualizou
     */
    public boolean atualizarAtributos(String id, Atributos atributos) {
        Document attrDoc = new Document()
                .append("durabilidade", atributos.getDurabilidade())
                .append("mira", atributos.getMira())
                .append("velocidade", atributos.getVelocidade())
                .append("carapaca", atributos.getCarapaca())
                .append("dano", atributos.getDano())
                .append("bateria", atributos.getBateria());

        return collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                new Document("$set", new Document("atributos", attrDoc))
        ).getModifiedCount() > 0;
    }

    /**
     * Atualiza o ranking e marcos de um Cria.
     *
     * @param id      o ID do Cria
     * @param ranking novo ranking
     * @param marcos  novos marcos
     * @return true se atualizou
     */
    public boolean atualizarRanking(String id, Ranking ranking, int marcos) {
        return collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                new Document("$set", new Document("ranking", ranking.name()).append("marcos", marcos))
        ).getModifiedCount() > 0;
    }

    // ==================== DELETE ====================

    /**
     * Remove um Cria pelo ID.
     *
     * @param id o ID do Cria
     * @return true se removeu
     */
    public boolean remover(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }

    /**
     * Remove todos os Crias da coleção.
     *
     * @return quantidade de documentos removidos
     */
    public long removerTodos() {
        return collection.deleteMany(new Document()).getDeletedCount();
    }

    // ==================== CONVERSÃO ====================

    /**
     * Converte um objeto Cria para Document do MongoDB.
     */
    private Document criaToDocument(Cria cria) {
        Document doc = new Document();

        doc.append("nome", cria.getNome())
           .append("chassi", cria.getChassi().name())
           .append("elemento", cria.getElemento().name())
           .append("personalidade", cria.getPersonalidade().name())
           .append("ranking", cria.getRanking().name())
           .append("marcos", cria.getMarcos())
           .append("pontosMemoria", cria.getPontosMemoria())
           .append("pontosComando", cria.getPontosComando());

        // Atributos como subdocumento
        if (cria.getAtributos() != null) {
            Atributos attr = cria.getAtributos();
            doc.append("atributos", new Document()
                    .append("durabilidade", attr.getDurabilidade())
                    .append("mira", attr.getMira())
                    .append("velocidade", attr.getVelocidade())
                    .append("carapaca", attr.getCarapaca())
                    .append("dano", attr.getDano())
                    .append("bateria", attr.getBateria()));
        }

        // Peças como array de subdocumentos
        List<Document> pecaDocs = new ArrayList<>();
        for (Peca peca : cria.getPecas()) {
            pecaDocs.add(new Document()
                    .append("nome", peca.getNome())
                    .append("memoria", peca.getMemoria())
                    .append("slot", peca.getSlot() != null ? peca.getSlot().name() : null)
                    .append("efeito", peca.getEfeito())
                    .append("quebrada", peca.isQuebrada()));
        }
        doc.append("pecas", pecaDocs);

        // Técnicas como array de subdocumentos
        List<Document> tecnicaDocs = new ArrayList<>();
        for (Tecnica tecnica : cria.getTecnicas()) {
            tecnicaDocs.add(new Document()
                    .append("nome", tecnica.getNome())
                    .append("elemento", tecnica.getElemento().name())
                    .append("custoBateria", tecnica.getCustoBateria())
                    .append("efeito", tecnica.getEfeito()));
        }
        doc.append("tecnicas", tecnicaDocs);

        return doc;
    }

    /**
     * Converte um Document do MongoDB para objeto Cria.
     */
    @SuppressWarnings("unchecked")
    private Cria documentToCria(Document doc) {
        Cria cria = new Cria();

        cria.setId(doc.getObjectId("_id").toHexString());
        cria.setNome(doc.getString("nome"));
        cria.setChassi(Chassi.valueOf(doc.getString("chassi")));
        cria.setElemento(Elemento.valueOf(doc.getString("elemento")));
        cria.setPersonalidade(Personalidade.valueOf(doc.getString("personalidade")));
        cria.setRanking(Ranking.valueOf(doc.getString("ranking")));
        cria.setMarcos(doc.getInteger("marcos", 0));
        cria.setPontosMemoria(doc.getInteger("pontosMemoria", 3));
        cria.setPontosComando(doc.getInteger("pontosComando", 2));

        // Atributos
        Document attrDoc = doc.get("atributos", Document.class);
        if (attrDoc != null) {
            Atributos atributos = new Atributos(
                    attrDoc.getInteger("durabilidade", 0),
                    attrDoc.getInteger("mira", 0),
                    attrDoc.getInteger("velocidade", 0),
                    attrDoc.getInteger("carapaca", 0),
                    attrDoc.getInteger("dano", 0),
                    attrDoc.getInteger("bateria", 0)
            );
            cria.setAtributos(atributos);
        }

        // Peças
        List<Document> pecaDocs = doc.getList("pecas", Document.class);
        if (pecaDocs != null) {
            List<Peca> pecas = new ArrayList<>();
            for (Document pecaDoc : pecaDocs) {
                Peca peca = new Peca();
                peca.setNome(pecaDoc.getString("nome"));
                peca.setMemoria(pecaDoc.getInteger("memoria", 0));
                String slotStr = pecaDoc.getString("slot");
                if (slotStr != null) {
                    peca.setSlot(SlotPeca.valueOf(slotStr));
                }
                peca.setEfeito(pecaDoc.getString("efeito"));
                peca.setQuebrada(pecaDoc.getBoolean("quebrada", false));
                pecas.add(peca);
            }
            cria.setPecas(pecas);
        }

        // Técnicas
        List<Document> tecnicaDocs = doc.getList("tecnicas", Document.class);
        if (tecnicaDocs != null) {
            List<Tecnica> tecnicas = new ArrayList<>();
            for (Document tecnicaDoc : tecnicaDocs) {
                Tecnica tecnica = new Tecnica();
                tecnica.setNome(tecnicaDoc.getString("nome"));
                tecnica.setElemento(Elemento.valueOf(tecnicaDoc.getString("elemento")));
                tecnica.setCustoBateria(tecnicaDoc.getInteger("custoBateria", 0));
                tecnica.setEfeito(tecnicaDoc.getString("efeito"));
                tecnicas.add(tecnica);
            }
            cria.setTecnicas(tecnicas);
        }

        return cria;
    }
}
