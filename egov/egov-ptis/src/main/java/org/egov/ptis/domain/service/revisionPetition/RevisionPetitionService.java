package org.egov.ptis.domain.service.revisionPetition;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.objection.Objection;
import org.springframework.transaction.annotation.Transactional;

public class RevisionPetitionService extends PersistenceService<Objection, Long> {
    private static final Logger LOGGER = Logger.getLogger(RevisionPetitionService.class);
    @Transactional
    public Objection createRevisionPetition(Objection objection) {
        if (objection.getId() == null)
            objection = persist(objection);
        else {
            objection = merge(objection);
        }

        return objection;

    }
    @Transactional
    public Objection updateRevisionPetition(Objection objection) {
        if (objection.getId() == null)
            objection = persist(objection);
        else {
            objection = update(objection);
        }

        return objection;

    }
}
