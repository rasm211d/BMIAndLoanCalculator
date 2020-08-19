package sample.loan;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoanClient2 extends Application {
    private TextField tfAnnualInterestRate = new TextField();
    private TextField tfNumberOfYears = new TextField();
    private TextField tfLoanAmount = new TextField();
    private TextArea ta = new TextArea();
    private Button btRegister = new Button("calculate");
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    String host = "localhost";

    @Override
    public void start(Stage stage) throws Exception {

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Annual Interest Rate: "),0,0);
        gridPane.add(tfAnnualInterestRate, 1 ,0);

        gridPane.add(new Label("Number of Years: "),0,1);
        gridPane.add(tfNumberOfYears,1,1);

        gridPane.add(new Label("Loan Amount: "),0,2);
        gridPane.add(tfLoanAmount, 1, 2);

        gridPane.add(btRegister, 2, 1);
        GridPane.setHalignment(btRegister, HPos.RIGHT);

        ScrollPane scrollPane = new ScrollPane(ta);
        VBox vBox = new VBox(gridPane, scrollPane);

        Scene scene = new Scene(vBox, 400, 200);
        stage.setScene(scene);
        stage.show();

        try {
            Socket socket = new Socket(host, 8000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        btRegister.setOnAction(new LoanClient2.ButtonListener2());



    }

    private class ButtonListener2 implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            new Thread(() -> {
                try {
                    double annualInterestRate = Double.parseDouble(tfAnnualInterestRate.getText().trim());
                    double numberOfYears = Double.parseDouble(tfNumberOfYears.getText().trim());
                    double loanAmount = Double.parseDouble(tfLoanAmount.getText().trim());

                    Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);
                    objectOutputStream.writeObject(loan);
                    //objectOutputStream.flush();

                    ta.appendText("Data is sent to the server" + "\n");
                    ta.appendText("\n");

                    Payment payment = (Payment) objectInputStream.readObject();
                    ta.appendText("Data recieved from the server: \n");
                    ta.appendText("Monthly Payment: " + payment.getMonthlyPayment() + "\n");
                    ta.appendText("Total Payment: " + payment.getTotalPayment() + "\n");

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();


        }
    }
}
