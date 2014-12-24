package org.egov.ptis.nmc.util;

import java.util.Comparator;

import org.egov.infstr.utils.DateUtils;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;

public enum UnitTaxInfoComparator implements Comparator<UnitTaxCalculationInfo> {
        UNIT_SORT {
            public int compare(UnitTaxCalculationInfo o1, UnitTaxCalculationInfo o2) {
                return o1.getUnitNumber().compareTo(o2.getUnitNumber());
            }},
        FLOOR_SORT {
            public int compare(UnitTaxCalculationInfo o1, UnitTaxCalculationInfo o2) {
                return o1.getFloorNumberInteger().compareTo(o2.getFloorNumberInteger());
            }},
        INST_SORT {
                public int compare(UnitTaxCalculationInfo o1, UnitTaxCalculationInfo o2) {
                    return DateUtils.getDate(o1.getInstDate(),"dd/MM/yyyy").compareTo(DateUtils.getDate(o2.getInstDate(),"dd/MM/yyyy"));
                }};
        

    
    public static Comparator<UnitTaxCalculationInfo> getUnitComparator(final UnitTaxInfoComparator... multipleOptions) {
        return new Comparator<UnitTaxCalculationInfo>() {
            public int compare(UnitTaxCalculationInfo o1, UnitTaxCalculationInfo o2) {
                for (UnitTaxInfoComparator option : multipleOptions) {
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

