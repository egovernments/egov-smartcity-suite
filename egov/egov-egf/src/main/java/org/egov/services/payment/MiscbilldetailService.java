package org.egov.services.payment;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.bills.Miscbilldetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
