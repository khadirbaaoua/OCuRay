package com.atlastic.ocuray.text;

/**
 * Created by khadirbaaoua on 22/03/2016.
 */
public class PercentTotal {
    private double percentage;
    private double amount;

    public PercentTotal() {
    }

    public PercentTotal(double percentage, double amount) {
        this.percentage = percentage;
        this.amount = amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
