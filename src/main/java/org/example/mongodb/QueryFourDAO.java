package org.example.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryFourDAO {
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private MongoCollection<Document> resultCollection;

    public QueryFourDAO() {
        // Initialize MongoDB client and select the database and collection
        String connectionString = "mongodb+srv://vishnuayyalasomayajula:Uy0G1KIsGtWQv1v5@cluster07.dflyt7n.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase("EduData");
        this.collection = database.getCollection("EduCostStat");
        this.resultCollection = database.getCollection("EduCostStatQueryFour");
    }

    public List<Document> getTopFiveStatesByGrowthRate(String type, String length, int range) {
        int currentYear = java.time.Year.now().getValue();
        int startYear = currentYear - range;

        // Building the aggregation pipeline
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.and(
                        Filters.eq("Type", type),
                        Filters.eq("Length", length),
                        Filters.gte("Year", startYear),
                        Filters.lte("Year", currentYear)
                )),
                Aggregates.group("$State",
                        Accumulators.first("FirstYearValue", "$Value"),
                        Accumulators.last("LastYearValue", "$Value")
                ),
                Aggregates.project(new Document("_id", 1)
                        .append("GrowthRate", new Document("$subtract", Arrays.asList("$LastYearValue", "$FirstYearValue")))
                ),
                Aggregates.sort(Sorts.descending("GrowthRate")),
                Aggregates.limit(5)
        );

        // Execute the aggregation query
        List<Document> results = new ArrayList<>();
        collection.aggregate(pipeline).into(results);

        // Insert results into the result collection
        resultCollection.insertMany(results);

        return results;
    }
}
