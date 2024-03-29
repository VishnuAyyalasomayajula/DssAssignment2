package org.example.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryTwoDAO {
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private MongoCollection<Document> resultCollection;

    public QueryTwoDAO() {
        // Initialize MongoDB client and select the database and collection
        String connectionString = "mongodb+srv://vishnuayyalasomayajula:Uy0G1KIsGtWQv1v5@cluster07.dflyt7n.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase("EduData");
        this.collection = database.getCollection("EduCostStat");
        this.resultCollection = database.getCollection("EduCostStatQueryTwo");
    }

    public List<Document> getTopFiveExpensiveStates(int year, String type, String length) {
        // Aggregation pipeline
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(new Document("Year", year).append("Type", type).append("Length", length)),
                Aggregates.group("$State", Accumulators.sum("TotalExpense", "$Value")),
                Aggregates.sort(Sorts.descending("TotalExpense")),
                Aggregates.limit(5)
        );

        // Execute the aggregation query
        List<Document> results = new ArrayList<>();
        collection.aggregate(pipeline).into(results);
        
        resultCollection.insertMany(results);

        return results;
    }
}
