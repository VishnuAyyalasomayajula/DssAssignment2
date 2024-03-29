package grpc.server;

import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.example.mongodb.QueryFourDAO;
import com.assignment.grpc.QueryFourRequest;
import com.assignment.grpc.QueryFourResponse;
import com.assignment.grpc.QueryFourResult;
import com.assignment.grpc.EduCostStatQueryFourServiceGrpc.EduCostStatQueryFourServiceImplBase;

import java.util.List;

public class QueryFourServiceImpl extends EduCostStatQueryFourServiceImplBase {

    @Override
    public void queryFour(QueryFourRequest request, StreamObserver<QueryFourResponse> responseObserver) {
        // Initialize DAO to interact with MongoDB
        QueryFourDAO dao = new QueryFourDAO();
        
        // Fetch the top 5 states with the highest growth rate
        List<Document> topGrowthStates = dao.getTopFiveStatesByGrowthRate(request.getType(), request.getLength(), request.getRange());
        
        // Build the response
        QueryFourResponse.Builder responseBuilder = QueryFourResponse.newBuilder();
        for (Document state : topGrowthStates) {
            QueryFourResult result = QueryFourResult.newBuilder()
                    .setState(state.getString("_id")) // Assuming "_id" is used as the state name
                    .setGrowthRate(((Number) state.get("GrowthRate")).doubleValue()) // Ensure this matches the field name and type in your DAO's returned Document
                    .build();
            responseBuilder.addResults(result);
        }
        
        // Send the response back to the client
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
