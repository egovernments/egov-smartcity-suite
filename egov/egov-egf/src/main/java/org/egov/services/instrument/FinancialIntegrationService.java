package org.egov.services.instrument;

public interface FinancialIntegrationService {

	public void updateCollectionsOnInstrumentDishonor(Long instrumentHeaderId);
	
	String EVENT_INSTRUMENT_DISHONOR_INITIATED = "INSTRUMENT_DISHONOR_INITIATED";
	String EVENT_INSTRUMENT_DISHONOR_CANCEL = "INSTRUMENT_DISHONOR_CANCEL" ;
			
	public void updateSourceInstrumentVoucher(String event,Long instrumentHeaderId);
}
