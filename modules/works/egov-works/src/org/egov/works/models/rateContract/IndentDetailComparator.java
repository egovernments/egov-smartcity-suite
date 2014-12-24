package org.egov.works.models.rateContract;

import java.util.Comparator;

public class IndentDetailComparator  implements Comparator{

    public int compare(Object arg0, Object arg1) {
        IndentDetail obj1=(IndentDetail)arg0;
        IndentDetail obj2=(IndentDetail)arg1;
        return obj1.getId().compareTo(obj2.getId());                
}

}
