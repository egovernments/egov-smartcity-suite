package org.egov.works.models.estimate;

import java.util.Comparator;

public class ActivityComparator  implements Comparator{

    public int compare(Object arg0, Object arg1) {
        Activity obj1=(Activity)arg0;
        Activity obj2=(Activity)arg1;
        return obj1.getId().compareTo(obj2.getId());                
}

}
