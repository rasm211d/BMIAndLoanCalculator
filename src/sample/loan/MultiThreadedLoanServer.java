package sample.loan;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MultiThreadedLoanServer extends Application {
    private double annualInterestRate;
    private double numberOfYears;
    private double loanAmount;
    private TextArea ta = new TextArea();
    private Socket socket;
    private int port = 8000;
    private int clientNo = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new javafx.scene.control.ScrollPane(ta), 450, 200);
        stage.setScene(scene);
        stage.show();

        new MultiThreadedLoanServer();
    }

    public MultiThreadedLoanServer() {
        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(port);
                ta.appendText("MultiThreadedServer started at " + new Date() + "\n");

                while (true) {
                    socket = server.accept();
                    clientNo++;

                    Platform.runLater(() -> {
                        ta.appendText("Starting thread for client " + clientNo + " at " + new Date() + "\n");
                        InetAddress inetAddress = socket.getInetAddress();

                        ta.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                        ta.appendText("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
                    });
                    new Thread(new HandleAClient(socket)).start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    class HandleAClient implements Runnable {

        private Socket socket;

        public HandleAClient(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    Loan loan = (Loan) inputStream.readObject();
                    annualInterestRate = loan.getAnnualInterestRate();
                    numberOfYears = loan.getNumberOfYears();
                    loanAmount = loan.getLoanAmount();

                    Platform.runLater(() -> {
                        ta.appendText("Annual Interest Rate recieved from client number: " + clientNo + ": " + annualInterestRate + "\n");
                        ta.appendText("Number Of Years recieved from the client number: " + clientNo + ": " + numberOfYears + "\n");
                        ta.appendText("Loan Amount recieved from the client number: " + clientNo + ": " + loanAmount + "\n");
                        ta.appendText("\n");
                    });

                    double totalPayment = loanAmount * Math.pow(1 + (annualInterestRate / 100), numberOfYears);
                    double monthlyPayment = totalPayment / 12;

                    Payment payment = new Payment(totalPayment, monthlyPayment);
                    outputStream.writeObject(payment);

                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
