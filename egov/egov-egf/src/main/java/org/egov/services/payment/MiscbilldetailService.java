package org.egov.services.payment;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.Miscbilldetail;
import org.springframework.stereotype.Service;

@Service
public class MiscbilldetailService extends PersistenceService<Miscbilldetail, Long> {

	
	public MiscbilldetailService(Class<Miscbilldetail> type) {
		super(type);
	}
	
	public MiscbilldetailService()
	{
		this(Miscbilldetail.class);
	}

	
}
