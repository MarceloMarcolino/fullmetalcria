package br.com.etec.fullmetalcria.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Factory para conexão com o MongoDB.
 * Gerencia uma única instância de MongoClient (Singleton).
 *
 * Configuração padrão: mongodb://localhost:27017
 * Banco de dados: fullmetalcria
 */
public class MongoConnectionFactory {

    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "fullmetalcria";

    private static MongoClient mongoClient;

    private MongoConnectionFactory() {
        // Construtor privado — Singleton
    }

    /**
     * Obtém a instância do MongoClient.
     * Cria uma nova conexão caso ainda não exista.
     *
     * @return instância do MongoClient
     */
    public static synchronized MongoClient getClient() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient;
    }

    /**
     * Obtém o banco de dados principal da aplicação.
     *
     * @return instância do MongoDatabase "fullmetalcria"
     */
    public static MongoDatabase getDatabase() {
        return getClient().getDatabase(DATABASE_NAME);
    }

    /**
     * Obtém um banco de dados pelo nome.
     *
     * @param databaseName nome do banco de dados
     * @return instância do MongoDatabase
     */
    public static MongoDatabase getDatabase(String databaseName) {
        return getClient().getDatabase(databaseName);
    }

    /**
     * Fecha a conexão com o MongoDB.
     * Deve ser chamado ao encerrar a aplicação.
     */
    public static synchronized void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
