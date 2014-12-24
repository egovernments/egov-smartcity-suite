package org.egov.tender.model;

import java.util.Comparator;
import org.egov.tender.interfaces.Tenderable;


public class TenderableEntityComparator implements Comparator<Tenderable>{
	
	public int compare(Tenderable arg0, Tenderable arg1) {
		TenderableEntity entity0 = (TenderableEntity)arg0;
		TenderableEntity entity1 = (TenderableEntity)arg1;
		if(entity0.getIndexNo()!=null && entity1.getIndexNo()!=null)
			return entity0.getIndexNo().compareTo(entity1.getIndexNo());
		else
			return 0;
	}

}
