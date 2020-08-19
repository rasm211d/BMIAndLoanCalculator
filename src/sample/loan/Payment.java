package sample.loan;

import java.io.Serializable;

public class Payment implements Serializable {
    private double totalPayment;
    private double monthlyPayment;

    public Payment(double totalPayment, double monthlyPayment)  {
        this.totalPayment = totalPayment;
        this.monthlyPayment = monthlyPayment;
    }


    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }
}
