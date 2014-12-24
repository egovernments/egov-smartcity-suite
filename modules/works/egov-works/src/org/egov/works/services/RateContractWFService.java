package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.rateContract.RateContract;

public class RateContractWFService extends PersistenceService<RateContract,Long>{ 
	private RateContractService rateContractService;
	public RateContractWFService(){
		setType(RateContract.class);
	}
	public RateContractService getRateContractService() {
		return rateContractService;
	}
	public void setRateContractService(RateContractService rateContractService) {
		this.rateContractService = rateContractService;
	}
	
	
}
