package grpc.server;

import io.grpc.stub.StreamObserver;
import org.example.mongodb.QueryOneDAO;
import com.assignment.grpc.QueryOneRequest;
import com.assignment.grpc.QueryOneResponse;
import com.assignment.grpc.EduCostStatQueryOneServiceGrpc.EduCostStatQueryOneServiceImplBase;

public class QueryOneServiceImpl extends EduCostStatQueryOneServiceImplBase {

    @Override
    public void queryOne(QueryOneRequest request, StreamObserver<QueryOneResponse> responseObserver) {
        // Initialize DAO to interact with MongoDB
        QueryOneDAO dao = new QueryOneDAO();
        
        // Fetch the expense value from the database based on the request parameters
        Integer expenseValue = dao.getExpenseValue(
                request.getYear(), 
                request.getState(), 
                request.getType(), 
                request.getLength(), 
                request.getExpense()
        );
        
        // Build the response
        QueryOneResponse response = QueryOneResponse.newBuilder()
                .setValue(expenseValue != null ? expenseValue : 0) // Use 0 as default if null
                .build();
        
        // Send the response back to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
