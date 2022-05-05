package org.betaiotazeta.fractalmusicgenerator;

public class ComplexNumber {

    public ComplexNumber(double realValue, double imaginaryValue) {
        this.realValue = realValue;
        this.imaginaryValue = imaginaryValue;
    }

    public double getRealValue() {
        return realValue;
    }

    public void setRealValue(double realValue) {
        this.realValue = realValue;
    }

    public double getImaginaryValue() {
        return imaginaryValue;
    }

    public void setImaginaryValue(double imaginaryValue) {
        this.imaginaryValue = imaginaryValue;
    }
    
    // Variables
    private double realValue;
    private double imaginaryValue;
}
