package sample.loan;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoanServer extends Application {
    private ObjectOutputStream outputToClient;
    private ObjectInputStream inputFromClient;
    private double annualInterestRate;
    private double numberOfYears;
    private double loanAmount;
    private TextArea ta;

    @Override
    public void start(Stage stage) throws Exception {
        ta = new TextArea();
        Scene scene = new Scene(new javafx.scene.control.ScrollPane(ta), 450, 200);
        stage.setScene(scene);
        stage.show();

        new LoanServer();
    }

    public LoanServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                System.out.println("Server started");

                while (true) {
                    Socket socket = serverSocket.accept();
                    inputFromClient = new ObjectInputStream(socket.getInputStream());
                    outputToClient = new ObjectOutputStream(socket.getOutputStream());

                    Loan loan = (Loan) inputFromClient.readObject();

                    annualInterestRate = loan.getAnnualInterestRate();
                    numberOfYears = loan.getNumberOfYears();
                    loanAmount = loan.getLoanAmount();

                    Platform.runLater(() -> {
                        ta.appendText("Annual Interest Rate recieved from the client: " + annualInterestRate + "\n");
                        ta.appendText("Number Of Years recieved from the client: " + numberOfYears + "\n");
                        ta.appendText("Loan Amount recieved from the client: " + loanAmount + "\n");
                        ta.appendText("\n");
                    });

                    double totalPayment = loanAmount * Math.pow(1 + (annualInterestRate / 100), numberOfYears);
                    double monthlyPayment = totalPayment / 12;

                    Payment payment = new Payment(totalPayment, monthlyPayment);
                    outputToClient.writeObject(payment);


                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
