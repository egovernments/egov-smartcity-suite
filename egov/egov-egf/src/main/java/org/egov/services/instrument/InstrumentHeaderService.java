package org.egov.services.instrument;

import org.egov.commons.EgwStatus;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional(readOnly = true)
public class InstrumentHeaderService extends PersistenceService<InstrumentHeader, Long> {

    public InstrumentHeaderService(final Class<InstrumentHeader> instrumentHeader) {
        this.type = instrumentHeader;
    }
    public InstrumentHeader reconcile(Date recociledOn,Long ihId,EgwStatus reconciledStatus)
    {
    	//InstrumentOtherDetails io = find("from InstrumentOtherDetails where instrumentHeaderId.id=?",ihId);
    	InstrumentHeader ih = findById(ihId);
    	ih.setStatusId(reconciledStatus);
    	applyAuditing(ih);
    	update(ih);
    	return ih;
    	
    }
}
