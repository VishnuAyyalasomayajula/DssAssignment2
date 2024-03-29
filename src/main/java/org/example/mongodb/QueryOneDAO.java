package org.example.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class QueryOneDAO {
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private MongoCollection<Document> resultCollection; // New collection for query results

    public QueryOneDAO() {
        // Initialize MongoDB client and select the database
        String connectionString = "mongodb+srv://vishnuayyalasomayajula:Uy0G1KIsGtWQv1v5@cluster07.dflyt7n.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase("EduData");

        // Set up the collection for original data
        this.collection = database.getCollection("EduCostStat");

        // Create a new collection for query results
        this.resultCollection = database.getCollection("EduCostStatQueryOne");
    }

    public Integer getExpenseValue(int year, String state, String type, String length, String expense) {
        // Construct the query document
        Document query = new Document("Year", year)
                .append("State", state)
                .append("Type", type)
                .append("Length", length)
                .append("Expense", expense);

        // Execute the query
        Document result = collection.find(query).first();

        // Store the query result in the new collection
        if (result != null) {
            resultCollection.insertOne(result);
        }

        // Return the expense value if the document is found, otherwise return null
        return result != null ? result.getInteger("Value") : null;
    }
}
