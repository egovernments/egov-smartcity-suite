package org.egov.ptis.nmc.util;

import java.util.Comparator;

import org.egov.ptis.nmc.model.AreaTaxCalculationInfo;

public enum AreaTaxInfoComparator implements Comparator<AreaTaxCalculationInfo> {
    BASERATE_SORT {
        public int compare(AreaTaxCalculationInfo o1, AreaTaxCalculationInfo o2) {
            return o1.getMonthlyBaseRent().compareTo(o2.getMonthlyBaseRent());
        }
    };

    public static Comparator<AreaTaxCalculationInfo> decending(final Comparator<AreaTaxCalculationInfo> other) {
        return new Comparator<AreaTaxCalculationInfo>() {
            public int compare(AreaTaxCalculationInfo o1, AreaTaxCalculationInfo o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

    public static Comparator<AreaTaxCalculationInfo> getAreaTaxComparator(
            final AreaTaxInfoComparator... multipleOptions) {
        return new Comparator<AreaTaxCalculationInfo>() {
            public int compare(AreaTaxCalculationInfo o1, AreaTaxCalculationInfo o2) {
                for (AreaTaxInfoComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
}
