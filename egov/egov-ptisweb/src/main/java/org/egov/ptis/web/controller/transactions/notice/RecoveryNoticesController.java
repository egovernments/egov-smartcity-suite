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
package org.egov.ptis.web.controller.transactions.notice;

import static org.egov.ptis.constants.PropertyTaxConstants.NON_VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.RECOVERY_NOTICETYPES;
import static org.egov.ptis.constants.PropertyTaxConstants.VAC_LAND_PROPERTY_TYPE_CATEGORY;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.bean.NoticeRequest;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.service.notice.RecoveryNoticeService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author subhash
 *
 */
@Controller
@RequestMapping(value = "/recoveryNotices")
public class RecoveryNoticesController {

    private static final String NOTICE_REQUEST = "noticeRequest";
    private static final String NOTICE_TYPE = "noticeType";
    private static final String ASSESSMENT_NUMBERS = "assessmentNumbers";
    private static final String PTIS_RECOVERY_NOTICES_JOB = "PTISRecoveryNoticesJob";
    private static final String RECOVERY_NOTICES_TRIGGER = "Recovery Notices Trigger";
    private static final String CATEGORY_TYPES = "categoryTypes";
    private static final String PROPERTY_TYPES = "propertyTypes";
    private static final String BILL_COLLECTORS = "billCollectors";
    private static final String BLOCKS = "blocks";
    private static final String WARDS = "wards";
    private static final String NOTICE_TYPES = "noticeTypes";
    private static final String RECOVERY_FORM = "recovery-form";
    private static final String RECOVERY_ACK = "recovery-ack";
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    private RecoveryNoticeService recoveryNoticeService;
    @Autowired
    private ApplicationContext beanProvider;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String form(final Model model) {
        final NoticeRequest noticeRequest = new NoticeRequest();
        model.addAttribute(NOTICE_REQUEST, noticeRequest);
        populateDropdowns(model, noticeRequest);
        return RECOVERY_FORM;
    }

    private void populateDropdowns(final Model model, final NoticeRequest noticeRequest) {
        final List<PropertyTypeMaster> propertyTypes = propertyTypeMasterDAO.findAllExcludeEWSHS();
        final List<Boundary> wards = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
        model.addAttribute(NOTICE_TYPES, RECOVERY_NOTICETYPES);
        model.addAttribute(WARDS, wards);
        if (!(noticeRequest.getWard() == null || noticeRequest.getWard().equals(-1l))) {
            final List<Boundary> blocks = boundaryService.getActiveChildBoundariesByBoundaryId(noticeRequest.getWard());
            model.addAttribute(BLOCKS, blocks);
        } else
            model.addAttribute(BLOCKS, Collections.emptyList());
        model.addAttribute(BILL_COLLECTORS, Collections.emptyList());
        model.addAttribute(PROPERTY_TYPES, propertyTypes);
        final Long propertyType = noticeRequest.getPropertyType();
        if (!(propertyType == null || propertyType.equals(-1l))) {
            final Map<String, String> propTypeCategoryMap = new TreeMap<>();
            final PropertyTypeMaster propTypeMstr = propertyTypeMasterDAO.findById(propertyType.intValue(),
                    false);
            if (propTypeMstr.getCode().equalsIgnoreCase(OWNERSHIP_TYPE_VAC_LAND))
                propTypeCategoryMap.putAll(VAC_LAND_PROPERTY_TYPE_CATEGORY);
            else
                propTypeCategoryMap.putAll(NON_VAC_LAND_PROPERTY_TYPE_CATEGORY);
            model.addAttribute(CATEGORY_TYPES, propTypeCategoryMap);
        } else
            model.addAttribute(CATEGORY_TYPES, Collections.emptyList());
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String generateNotices(@Valid @ModelAttribute final NoticeRequest noticeRequest, final Model model,
            final BindingResult errors) {
        validate(noticeRequest, errors);
        if (errors.hasErrors()) {
            model.addAttribute(NOTICE_REQUEST, noticeRequest);
            populateDropdowns(model, noticeRequest);
            return RECOVERY_FORM;
        } else {
            Long jobNumber = recoveryNoticeService.getLatestJobNumber();
            jobNumber = jobNumber == null ? 0l : jobNumber + 1;
            final List<String> assessmentNumbers = recoveryNoticeService.generateRecoveryNotices(noticeRequest);
            if (assessmentNumbers.isEmpty()) {
                model.addAttribute(NOTICE_REQUEST, noticeRequest);
                populateDropdowns(model, noticeRequest);
                errors.reject("record.not.found", "record.not.found");
                return RECOVERY_FORM;
            }

            final JobDetailImpl jobDetail = (JobDetailImpl) beanProvider.getBean("recoveryNoticesJobScheduler");
            final Scheduler scheduler = (Scheduler) beanProvider.getBean("ptisSchedular");
            try {
                jobDetail.setName(PTIS_RECOVERY_NOTICES_JOB.concat(jobNumber.toString()));
                final StringBuilder assessmentNoStr = new StringBuilder();
                for (final String assessmentNo : assessmentNumbers)
                    assessmentNoStr.append(assessmentNo).append(", ");
                assessmentNoStr.deleteCharAt(assessmentNoStr.lastIndexOf(", "));
                jobDetail.getJobDataMap().remove(ASSESSMENT_NUMBERS);
                jobDetail.getJobDataMap().remove(NOTICE_TYPE);
                jobDetail.getJobDataMap().put(ASSESSMENT_NUMBERS, assessmentNoStr.toString());
                jobDetail.getJobDataMap().put(NOTICE_TYPE, noticeRequest.getNoticeType());
                final SimpleTriggerImpl trigger = new SimpleTriggerImpl();
                trigger.setName(RECOVERY_NOTICES_TRIGGER.concat(jobNumber.toString()));
                trigger.setStartTime(new Date(System.currentTimeMillis() + 100000));
                trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                scheduler.start();
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (final SchedulerException e) {
                throw new ApplicationRuntimeException(e.getMessage(), e);
            }
        }
        return RECOVERY_ACK;
    }

    private void validate(final NoticeRequest noticeRequest, final BindingResult errors) {
        if (org.apache.commons.lang.StringUtils.isEmpty(noticeRequest.getNoticeType())
                || "-1".equals(noticeRequest.getNoticeType()))
            errors.reject("mandatory.noticetype", "mandatory.noticetype");
        Boolean isSelected = Boolean.FALSE;
        if (isWardSelected(noticeRequest) || isBillCollectorSelected(noticeRequest) || isPropertyTypeSelected(noticeRequest)
                || isPropertyIdEntered(noticeRequest))
            isSelected = Boolean.TRUE;
        if (!isSelected)
            errors.reject("mandatory.anyone", "mandatory.anyone");
    }

    private Boolean isPropertyIdEntered(final NoticeRequest noticeRequest) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(noticeRequest.getPropertyId()))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean isPropertyTypeSelected(final NoticeRequest noticeRequest) {
        if (noticeRequest.getPropertyType() != null && !noticeRequest.getPropertyType().equals(-1l))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean isBillCollectorSelected(final NoticeRequest noticeRequest) {
        if (noticeRequest.getBillCollector() != null && !noticeRequest.getBillCollector().equals(-1l))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Boolean isWardSelected(final NoticeRequest noticeRequest) {
        if (noticeRequest.getWard() != null && !noticeRequest.getWard().equals(-1l))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
