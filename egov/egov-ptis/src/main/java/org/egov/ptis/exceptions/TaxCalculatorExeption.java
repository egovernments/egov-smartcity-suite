package org.egov.ptis.exceptions;

public class TaxCalculatorExeption extends Exception {
    public TaxCalculatorExeption() {
        super();
    }

    public TaxCalculatorExeption(String msg) {
        super(msg);
    }

    public TaxCalculatorExeption(String msg, Throwable th) {
        super(msg, th);
    }

}
