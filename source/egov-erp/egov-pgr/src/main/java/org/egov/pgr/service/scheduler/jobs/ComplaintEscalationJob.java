/**
 *
 */
package org.egov.pgr.service.scheduler.jobs;

import org.egov.infstr.scheduler.quartz.AbstractQuartzJob;
import org.egov.pgr.service.EscalationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vaibhav.K
 */
@Transactional
public class ComplaintEscalationJob extends AbstractQuartzJob {

    private static final long serialVersionUID = -5428952585539260293L;

    @Autowired
    private EscalationService escalationService;

    @Override
    public void executeJob() {
        escalationService.escalateComplaint();
    }

}
