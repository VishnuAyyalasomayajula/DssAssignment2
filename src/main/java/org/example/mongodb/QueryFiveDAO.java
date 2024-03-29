package org.example.mongodb;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class QueryFiveDAO {
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private MongoCollection<Document> resultCollection;

    public QueryFiveDAO() {
        String connectionString = "mongodb+srv://vishnuayyalasomayajula:Uy0G1KIsGtWQv1v5@cluster07.dflyt7n.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase("EduData");
        this.collection = database.getCollection("EduCostStat");
        this.resultCollection = database.getCollection("EduCostStatQueryFive");
    }

    public List<Document> getAverageExpenseByRegion(int year, String type, String length) {
        List<Document> regionsAggregation = new ArrayList<>();

        // Define arrays for states in each region
        List<String> westStates = Arrays.asList("Washington", "Oregon", "California", "Nevada", "Idaho", "Utah", "Montana", "Wyoming", "Colorado", "Alaska", "Hawaii");
        List<String> southWestStates = Arrays.asList("Arizona", "New Mexico", "Texas", "Oklahoma");
        List<String> midWestStates = Arrays.asList("North Dakota", "South Dakota", "Nebraska", "Kansas", "Minnesota", "Iowa", "Missouri", "Wisconsin", "Illinois", "Michigan", "Indiana", "Ohio");
        List<String> southEastStates = Arrays.asList("Arkansas", "Louisiana", "Mississippi", "Alabama", "Tennessee", "Kentucky", "West Virginia", "Georgia", "South Carolina", "North Carolina", "Florida", "Virginia", "District of Columbia", "Delaware");
        List<String> northEastStates = Arrays.asList("Maine", "Pennsylvania", "Maryland", "New York", "Vermont", "New Hampshire", "Massachusetts", "Rhode Island", "Connecticut", "New Jersey");

        // Aggregate expenses by region
        regionsAggregation.addAll(aggregateExpensesByRegion(year, type, length, "West", westStates));
        regionsAggregation.addAll(aggregateExpensesByRegion(year, type, length, "South West", southWestStates));
        regionsAggregation.addAll(aggregateExpensesByRegion(year, type, length, "Mid West", midWestStates));
        regionsAggregation.addAll(aggregateExpensesByRegion(year, type, length, "South East", southEastStates));
        regionsAggregation.addAll(aggregateExpensesByRegion(year, type, length, "North East", northEastStates));

        return regionsAggregation;
    }

    private List<Document> aggregateExpensesByRegion(int year, String type, String length, String regionId, List<String> states) {
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("Year", year)
                        .append("Type", type)
                        .append("Length", length)
                        .append("State", new Document("$in", states))),
                new Document("$group", new Document("_id", regionId)
                        .append("total_value", new Document("$sum", "$Value"))),
                new Document("$project", new Document("Region", "$_id").append("AverageExpense", "$total_value"))
        ));

        List<Document> docs = new ArrayList<>();
        result.into(docs);
        
        resultCollection.insertMany(docs);
        return docs;
    }
}
