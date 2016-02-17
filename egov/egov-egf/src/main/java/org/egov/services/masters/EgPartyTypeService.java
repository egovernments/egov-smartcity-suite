package org.egov.services.masters;

import org.egov.commons.EgPartytype;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class EgPartyTypeService extends PersistenceService<EgPartytype, Integer> {
    
    public EgPartyTypeService(final Class<EgPartytype> partyType) {
        this.type = partyType;
    }
}
