package sample.loan;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class LoanClient extends Application {
    private TextField tfAnnualInterestRate = new TextField();
    private TextField tfNumberOfYears = new TextField();
    private TextField tfLoanAmount = new TextField();
    private TextArea ta = new TextArea();
    private Button btRegister = new Button("calculate");

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

        btRegister.setOnAction(new ButtonListener());

    }

    private class ButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                Socket socket = new Socket(host, 8000);

                ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
                double annualInterestRate = Double.parseDouble(tfAnnualInterestRate.getText());
                double numberOfYears = Double.parseDouble(tfNumberOfYears.getText());
                double loanAmount = Double.parseDouble(tfLoanAmount.getText());

                Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);
                toServer.writeObject(loan);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
