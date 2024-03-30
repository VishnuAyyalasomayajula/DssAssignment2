/*package org.example.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ExceltoMongoDB {
	
	public static void main(String[] args)
	{
		
		//**** CONNECTION STRING FOR MONGODB *****
		ConnectionString connectionString = new ConnectionString("mongodb+srv://vishnuayyalasomayajula:Uy0G1KIsGtWQv1v5@cluster07.dflyt7n.mongodb.net/?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder()
		        .applyConnectionString(connectionString)
		        .build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("EduData");
        
        // Creating a collection
        MongoCollection<Document> collection = database.getCollection("EduCostStat");
        
        //Reading the CSV File
        String csvFile = "C:\\Users\\ayyal\\Downloads\\Dataset_Ass2\\nces330_20.csv";
        String line;
        String csvSplitBy = ",";
        
        try (BufferedReader br = new BufferedReader (new FileReader (csvFile)))
        {
        	//Skipping the first line for column headers
        	br.readLine();
        	
            // Iterating over the remaining lines and inserting data into MongoDB
        	 while ((line = br.readLine()) != null) {
                 String[] values = line.split(csvSplitBy);
                 
                 String field2 = values[1];
                 int field1 = Integer.parseInt(values[0]);
                 //boolean field3 = Boolean.parseBoolean(values[2]);
                 String field3 = values[3];
                 
                 int year = Integer.parseInt(values[0]);
                 String state = values[1];
                 String type = values[2];
                 String length = values [3];
                 String expense = values [4];
                 int value = Integer.parseInt(values[5]);
                 
                 
                 Document document = new Document()
                         .append("Year", year)
                         .append("State", state)
                         .append("Type", type)
                         .append("Length", length)
                         .append("Expense", expense)
                         .append("Value", value);

                 collection.insertOne(document);
        	 }
        	
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
        
        // Closing the connections
        mongoClient.close();
       
	}
}*/
