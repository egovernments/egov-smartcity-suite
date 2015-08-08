package org.egov.ptis.domain.service.revisionPetition;

import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.springframework.transaction.annotation.Transactional;

public class RevisionPetitionService extends PersistenceService<RevisionPetition, Long> {
    private static final Logger LOGGER = Logger.getLogger(RevisionPetitionService.class);
    @Transactional
    public RevisionPetition createRevisionPetition(RevisionPetition objection) {
        if (objection.getId() == null)
            objection = persist(objection);
        else {
            objection = merge(objection);
        }

        return objection;

    }
    @Transactional
    public RevisionPetition updateRevisionPetition(RevisionPetition objection) {
        if (objection.getId() == null)
            objection = persist(objection);
        else {
            objection = update(objection);
        }

        return objection;

    }
}
