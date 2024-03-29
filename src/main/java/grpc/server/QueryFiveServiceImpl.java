package grpc.server;

import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.example.mongodb.QueryFiveDAO;
import com.assignment.grpc.QueryFiveRequest;
import com.assignment.grpc.QueryFiveResponse;
import com.assignment.grpc.QueryFiveResult;
import com.assignment.grpc.EduCostStatQueryFiveServiceGrpc.EduCostStatQueryFiveServiceImplBase;

import java.util.List;

public class QueryFiveServiceImpl extends EduCostStatQueryFiveServiceImplBase {

    @Override
    public void queryFive(QueryFiveRequest request, StreamObserver<QueryFiveResponse> responseObserver) {
        // Initialize the DAO for MongoDB interactions
        QueryFiveDAO dao = new QueryFiveDAO();
        
        // Fetch the aggregated expenses by region for the specified criteria
        List<Document> aggregatedResults = dao.getAverageExpenseByRegion(request.getYear(), request.getType(), request.getLength());
        
        // Construct the response
        QueryFiveResponse.Builder responseBuilder = QueryFiveResponse.newBuilder();
        for (Document regionResult : aggregatedResults) {
            // Assuming the document structure has "Region" and "AverageExpense" fields
            String region = regionResult.getString("_id"); // The region identifier
            int averageExpense = regionResult.getInteger("total_value", 0); // Default to 0 if not found
            
            QueryFiveResult result = QueryFiveResult.newBuilder()
                    .setRegion(region)
                    .setAverageExpense(averageExpense)
                    .build();
            responseBuilder.addResults(result);
        }
        
        // Send the response back to the client
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
