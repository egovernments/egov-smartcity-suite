/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.ptis.scheduler;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.notice.RecoveryNoticesInfo;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.notice.RecoveryNoticeService;
import org.egov.ptis.notice.PtNotice;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *
 * @author subhash
 *
 */
@DisallowConcurrentExecution
public class RecoveryNoticesJob extends QuartzJobBean {

    private static final Logger LOGGER = Logger.getLogger(RecoveryNoticesJob.class);

    @Autowired
    private RecoveryNoticeService recoveryNoticesService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
        ApplicationThreadLocals.setUserId(securityUtils.getCurrentUser().getId());
        final String assessmentNumbers = (String) context.getJobDetail().getJobDataMap().get("assessmentNumbers");
        final String noticeType = (String) context.getJobDetail().getJobDataMap().get("noticeType");
        final List<String> assessments = Arrays.asList(assessmentNumbers.split(", "));
        final TransactionTemplate txTemplate = new TransactionTemplate(transactionTemplate.getTransactionManager());
        for (final String assessmentNo : assessments) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Generating " + noticeType + " for assessment : " + assessmentNo);
            try {
                txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                txTemplate.execute(result -> {
                    final List<String> errors = recoveryNoticesService.validateRecoveryNotices(assessmentNo, noticeType);
                    final RecoveryNoticesInfo noticeInfo = recoveryNoticesService
                            .getRecoveryNoticeInfoByAssessmentAndNoticeType(assessmentNo, noticeType);
                    if (errors.isEmpty())
                        return generateNotice(noticeType, assessmentNo, noticeInfo);
                    else {
                        updateNoticeInfo(errors, noticeInfo);
                        return Boolean.FALSE;
                    }
                });
            } catch (final Exception e) {
                txTemplate.execute(result -> {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.error("Exception in Generating " + noticeType + " for assessment : " + assessmentNo);
                        LOGGER.error(e);
                    }
                    return Boolean.FALSE;
                });
            }
        }
    }

    private void updateNoticeInfo(final List<String> errors, final RecoveryNoticesInfo noticeInfo) {
        noticeInfo.setError(errors.get(0));
        recoveryNoticesService.saveRecoveryNoticeInfo(noticeInfo);
    }

    private Boolean generateNotice(final String noticeType, final String assessmentNo, final RecoveryNoticesInfo noticeInfo) {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        final PtNotice notice = noticeService.getNoticeByNoticeTypeAndAssessmentNumner(noticeType,
                basicProperty.getUpicNo());
        if (notice == null) {
            recoveryNoticesService.generateNotice(noticeType, basicProperty);
            noticeInfo.setGenerated(Boolean.TRUE);
            recoveryNoticesService.saveRecoveryNoticeInfo(noticeInfo);
        }
        return Boolean.TRUE;
    }

}
