package grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.assignment.grpc.*;

public class GrpcClient {

    private final ManagedChannel channel;
    private final EduCostStatQueryOneServiceGrpc.EduCostStatQueryOneServiceBlockingStub queryOneStub;
    private final EduCostStatQueryTwoServiceGrpc.EduCostStatQueryTwoServiceBlockingStub queryTwoStub;
    private final EduCostStatQueryThreeServiceGrpc.EduCostStatQueryThreeServiceBlockingStub queryThreeStub;
    private final EduCostStatQueryFourServiceGrpc.EduCostStatQueryFourServiceBlockingStub queryFourStub;
    private final EduCostStatQueryFiveServiceGrpc.EduCostStatQueryFiveServiceBlockingStub queryFiveStub;

    public GrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        queryOneStub = EduCostStatQueryOneServiceGrpc.newBlockingStub(channel);
        queryTwoStub = EduCostStatQueryTwoServiceGrpc.newBlockingStub(channel);
        queryThreeStub = EduCostStatQueryThreeServiceGrpc.newBlockingStub(channel);
        queryFourStub = EduCostStatQueryFourServiceGrpc.newBlockingStub(channel);
        queryFiveStub = EduCostStatQueryFiveServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() {
        channel.shutdown();
    }

    public void callQueryOne() {
        QueryOneRequest request = QueryOneRequest.newBuilder()
                .setYear(2013)
                .setState("Alaska")
                .setType("Public In-State")
                .setLength("4-year")
                .setExpense("Room/Board")
                .build();
        QueryOneResponse response = queryOneStub.queryOne(request);
        System.out.println("Query 1 Response: " + response);
    }

    public void callQueryTwo() {
        QueryTwoRequest request = QueryTwoRequest.newBuilder()
                .setYear(2013)
                .setType("Public In-State")
                .setLength("4-year")
                .build();
        QueryTwoResponse response = queryTwoStub.queryTwo(request);
        System.out.println("Query 2 Response: " + response);
    }

    public void callQueryThree() {
        QueryThreeRequest request = QueryThreeRequest.newBuilder()
                .setYear(2013)
                .setType("Public In-State")
                .setLength("4-year")
                .build();
        QueryThreeResponse response = queryThreeStub.queryThree(request);
        System.out.println("Query 3 Response: " + response);
    }

    public void callQueryFour() {
        QueryFourRequest request = QueryFourRequest.newBuilder()
                .setType("Public In-State")
                .setLength("4-year")
                .setRange(5)
                .build();
        QueryFourResponse response = queryFourStub.queryFour(request);
        System.out.println("Query 4 Response: " + response);
    }

    public void callQueryFive() {
        QueryFiveRequest request = QueryFiveRequest.newBuilder()
                .setYear(2013)
                .setType("Private")
                .setLength("4-year")
                .build();
        QueryFiveResponse response = queryFiveStub.queryFive(request);
        System.out.println("Query 5 Response: " + response);
    }

    public static void main(String[] args) {
        GrpcClient client = new GrpcClient("localhost", 9090);
        try {
            client.callQueryOne();
            client.callQueryTwo();
            client.callQueryThree();
            client.callQueryFour();
            client.callQueryFive();
        } finally {
            client.shutdown();
        }
    }
}
