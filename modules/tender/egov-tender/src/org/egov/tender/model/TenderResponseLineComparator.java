package org.egov.tender.model;

import java.util.Comparator;

public class TenderResponseLineComparator implements Comparator<TenderResponseLine>{

	public int compare(TenderResponseLine arg0, TenderResponseLine arg1) {
		TenderResponseLine entity0 = (TenderResponseLine)arg0;
		TenderResponseLine entity1 = (TenderResponseLine)arg1;
		if(entity0.getTenderableEntity()!=null && entity1.getTenderableEntity()!=null 
				&& entity0.getTenderableEntity().getIndexNo()!=null && entity1.getTenderableEntity().getIndexNo()!=null)
		return entity0.getTenderableEntity().getIndexNo().compareTo(entity1.getTenderableEntity().getIndexNo());
		else
			return 0;
	}
}
