package sample.loan;

import java.io.Serializable;

public class Loan implements Serializable {
    private double annualInterestRate;
    private double numberOfYears;
    private double loanAmount;

    public Loan(double annualInterestRate, double numberOfYears, double loanAmount) {
        this.annualInterestRate = annualInterestRate;
        this.numberOfYears = numberOfYears;
        this.loanAmount = loanAmount;
    }


    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public double getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(double numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }
}
