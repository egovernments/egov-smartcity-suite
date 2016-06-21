package org.egov.pgr.service.dashboard;

import java.text.DecimalFormat;

/**
 * Created by srikanth on 15/6/16.
 */
public class Test {

    public static void main(String [] arg) {
        final DecimalFormat df = new DecimalFormat("####0.00");
        double val = 0;

        System.out.print(df.format(100 * (0 + 1 - 1)
                / val));
    }
}
