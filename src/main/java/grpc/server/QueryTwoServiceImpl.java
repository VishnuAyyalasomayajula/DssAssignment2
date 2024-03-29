package grpc.server;

import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.example.mongodb.QueryTwoDAO;
import com.assignment.grpc.QueryTwoRequest;
import com.assignment.grpc.QueryTwoResponse;
import com.assignment.grpc.StateExpense;
import com.assignment.grpc.EduCostStatQueryTwoServiceGrpc.EduCostStatQueryTwoServiceImplBase;

import java.util.List;

public class QueryTwoServiceImpl extends EduCostStatQueryTwoServiceImplBase {

    @Override
    public void queryTwo(QueryTwoRequest request, StreamObserver<QueryTwoResponse> responseObserver) {
        // Initialize DAO to interact with MongoDB
        QueryTwoDAO dao = new QueryTwoDAO();
        
        // Fetch the top 5 most expensive states from the database
        List<Document> topStates = dao.getTopFiveExpensiveStates(request.getYear(), request.getType(), request.getLength());
        
        // Build the response
        QueryTwoResponse.Builder responseBuilder = QueryTwoResponse.newBuilder();
        for (Document state : topStates) {
            StateExpense expense = StateExpense.newBuilder()
                    .setState(state.getString("_id")) // Assuming "_id" is used as the state name
                    .setOverallExpense(state.getInteger("TotalExpense"))
                    .build();
            responseBuilder.addExpenses(expense);
        }
        
        // Send the response back to the client
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
