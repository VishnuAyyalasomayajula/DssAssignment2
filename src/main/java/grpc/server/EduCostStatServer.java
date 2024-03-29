package grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class EduCostStatServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 9090; // Standard gRPC port; change as needed.
        Server server = ServerBuilder.forPort(port)
                .addService((BindableService) new QueryOneServiceImpl())
                .addService((BindableService) new QueryTwoServiceImpl())
                .addService((BindableService) new QueryThreeServiceImpl())
                .addService((BindableService) new QueryFourServiceImpl())
                .addService((BindableService) new QueryFiveServiceImpl())
                .build();

        server.start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            server.shutdown();
            System.err.println("*** server shut down");
        }));

        server.awaitTermination();
    }
}
