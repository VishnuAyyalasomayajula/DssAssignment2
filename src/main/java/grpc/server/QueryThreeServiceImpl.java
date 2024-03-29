package grpc.server;

import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.example.mongodb.QueryThreeDAO;
import com.assignment.grpc.QueryThreeRequest;
import com.assignment.grpc.QueryThreeResponse;
import com.assignment.grpc.StateExpense;
import com.assignment.grpc.EduCostStatQueryThreeServiceGrpc.EduCostStatQueryThreeServiceImplBase;

import java.util.List;

public class QueryThreeServiceImpl extends EduCostStatQueryThreeServiceImplBase {

    @Override
    public void queryThree(QueryThreeRequest request, StreamObserver<QueryThreeResponse> responseObserver) {
        // Initialize the DAO
        QueryThreeDAO dao = new QueryThreeDAO();
        
        // Fetch the top 5 most economical states from the database
        List<Document> topEconomicStates = dao.getTopFiveEconomicStates(request.getYear(), request.getType(), request.getLength());
        
        // Build the response
        QueryThreeResponse.Builder responseBuilder = QueryThreeResponse.newBuilder();
        for (Document state : topEconomicStates) {
            StateExpense expense = StateExpense.newBuilder()
                    .setState(state.getString("_id")) // Assuming "_id" represents the state name
                    .setOverallExpense(state.getInteger("TotalExpense"))
                    .build();
            responseBuilder.addExpenses(expense);
        }
        
        // Send the response back to the client
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
