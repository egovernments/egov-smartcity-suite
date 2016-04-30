package org.egov.services.instrument;

import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentOtherDetails;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Transactional(readOnly = true)
public class InstrumentOtherDetailsService extends PersistenceService<InstrumentOtherDetails, Long> {

    public InstrumentOtherDetailsService(final Class<InstrumentOtherDetails> instrumentOtherDetails) {
        this.type = instrumentOtherDetails;
    }
    
    public void reconcile(Date recociledOn,Long ihId,BigDecimal instrumentAmount)
    {
    	InstrumentOtherDetails io = find("from InstrumentOtherDetails where instrumentHeaderId.id=?",ihId);
    	io.setReconciledAmount(instrumentAmount);
    	io.setReconciledOn(recociledOn);
    	io.setInstrumentStatusDate(new Date());
    	applyAuditing(io);
    	update(io);
    	
    }
	
}
