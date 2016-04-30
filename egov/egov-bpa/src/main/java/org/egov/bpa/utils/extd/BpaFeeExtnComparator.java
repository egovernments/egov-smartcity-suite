package org.egov.bpa.utils.extd;

import org.egov.bpa.models.extd.masters.BpaFeeExtn;

import java.util.Comparator;

public enum BpaFeeExtnComparator implements Comparator<BpaFeeExtn> {
	 FEECODE_SORT {
         public int compare(BpaFeeExtn o1, BpaFeeExtn o2) {
             return Integer.valueOf(o1.getFeeCode()).compareTo(Integer.valueOf(o2.getFeeCode()));
         }};


public static Comparator<BpaFeeExtn> getFeeComparator(final BpaFeeExtnComparator... multipleOptions) {
    return new Comparator<BpaFeeExtn>() {
        public int compare(BpaFeeExtn o1, BpaFeeExtn o2) {
            for (BpaFeeExtnComparator option : multipleOptions) {
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