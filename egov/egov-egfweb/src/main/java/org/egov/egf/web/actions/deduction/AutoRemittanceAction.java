/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.egf.web.actions.deduction;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.dao.recoveries.TdsHibernateDAO;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.deduction.DepartmentDOMapping;
import org.egov.model.recoveries.Recovery;
import org.egov.model.recoveries.RemittanceSchedulerLog;
import org.egov.services.deduction.ScheduledRemittanceService;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



@Results({
    @Result(name = "manual", location = "autoRemittance-manual.jsp")
})
public class AutoRemittanceAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = 453551610004473622L;
    private final static Logger LOGGER = Logger.getLogger(AutoRemittanceAction.class);
    private ScheduledRemittanceService scheduledRemittanceService;
    private String glcode;
    private Integer dept;
    private String drawingOfficer;
    private Date lastRunDate;
    private Map<String, String> coaMap;
    private List<DepartmentDOMapping> deptDOList;
    private RemittanceSchedulerLog remittanceScheduler;
    private Map<String, String> lastRunDateMap;
    private TdsHibernateDAO tdsDAO;

   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    @Override
    public Object getModel() {
        return null;
    }

    @Action(value = "/deduction/autoRemittance-manualSchedule")
    public String manualSchedule()
    {
        try {
            coaMap = new LinkedHashMap<String, String>();
            final List<Recovery> allActiveAutoRemitTds = tdsDAO.getAllActiveAutoRemitTds();

            for (final Recovery r : allActiveAutoRemitTds)
                coaMap.put(r.getChartofaccounts().getGlcode(), r.getChartofaccounts().getGlcode() + "-"
                        + r.getChartofaccounts().getName());

            addDropdownData("departmentList", masterDataCache.get("egi-department"));
            deptDOList = persistenceService.findAllBy("from DepartmentDOMapping where department is not null  ");

            final List<Object[]> list = persistenceService.getSession()
                    .
                    createSQLQuery(
                            "select glcode, to_char(max(lastrundate),'dd/mm/yyyy') from egf_remittance_scheduler where glcode is not null and sch_type='A' "
                                    +
                            " GROUP by glcode order by glcode").list();
            lastRunDateMap = new HashMap<String, String>();
            for (final Object[] ob : list)
                lastRunDateMap.put((String) ob[0], (String) ob[1]);
        } catch (final ApplicationRuntimeException e) {
            addActionError("failed");
        } catch (final HibernateException e) {
            addActionError("failed");
        } catch (final Exception e) {
            addActionError("failed");
        }

        return "manual";

    }

    @ValidationErrorPage(value = "messages")
    public String schedule()
    {
        try {
            LOGGER.info("Inside RemittanceJob");
            remittanceScheduler = new RemittanceSchedulerLog();
            remittanceScheduler.setGlcode(glcode);
            remittanceScheduler.setSchType(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_MANUAL);
            remittanceScheduler.setSchJobName("Manual");
            remittanceScheduler.setLastRunDate(new Date());
            remittanceScheduler.setCreatedDate(new Date());
            remittanceScheduler.setCreatedBy(ApplicationThreadLocals.getUserId().intValue());
            remittanceScheduler.setStatus("Started");
            scheduledRemittanceService.getRemittanceSchedulerLogService().persist(remittanceScheduler);
            final Long schedularLogId = remittanceScheduler.getId();
            final boolean searchRecovery = scheduledRemittanceService.searchRecovery(glcode, "Manual", schedularLogId, dept,
                    lastRunDate);
            if (searchRecovery == false)
            {
                addActionMessage(getText("schedular.failed"));
                addActionMessage(scheduledRemittanceService.getErrorMessage().toString());
            } else
                addActionMessage(getText("schedular.succeful"));
        } catch (final ValidationException e)
        {
            addActionMessage(getText("schedular.failed"));
            throw new ValidationException(Arrays.asList(new ValidationError(scheduledRemittanceService.getErrorMessage()
                    .toString(), scheduledRemittanceService.getErrorMessage().toString())));
        }

        catch (final Exception e)
        {
            addActionMessage(getText("schedular.failed"));
            throw new ValidationException(Arrays.asList(new ValidationError(scheduledRemittanceService.getErrorMessage()
                    .toString(), scheduledRemittanceService.getErrorMessage().toString())));
        }
        final List<String> findAllBy = persistenceService.findAllBy("select voucherheaderId.voucherNumber from " +
                        "RemittanceSchedulePayment  where schId.id=?", remittanceScheduler.getId());
        if (findAllBy.isEmpty())
            addActionMessage(" No Payments Created ");
        else
        {
            addActionMessage(" Payment vouchernumbers listed below");
            addActionMessage(findAllBy.toString().replace('[', ' ').replace(']', ' '));
        }
        return "messages";
    }

    public void setScheduledRemittanceService(
            final ScheduledRemittanceService scheduledRemittanceService) {
        this.scheduledRemittanceService = scheduledRemittanceService;
    }

    public String getGlcode() {
        return glcode;
    }

    public void setGlcode(final String glcode) {
        this.glcode = glcode;
    }

    public Integer getDept() {
        return dept;
    }

    public void setDept(final Integer dept) {
        this.dept = dept;
    }

    public String getDrawingOfficer() {
        return drawingOfficer;
    }

    public void setDrawingOfficer(final String drawingOfficer) {
        this.drawingOfficer = drawingOfficer;
    }

    public Date getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(final Date lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public Map<String, String> getCoaMap() {
        return coaMap;
    }

    public void setCoaMap(final Map<String, String> coaMap) {
        this.coaMap = coaMap;
    }

    public RemittanceSchedulerLog getRemittanceScheduler() {
        return remittanceScheduler;
    }

    public void setRemittanceScheduler(final RemittanceSchedulerLog remittanceScheduler) {
        this.remittanceScheduler = remittanceScheduler;
    }

    public List<DepartmentDOMapping> getDeptDOList() {
        return deptDOList;
    }

    public void setDeptDOList(final List<DepartmentDOMapping> deptDOList) {
        this.deptDOList = deptDOList;
    }

    public Map<String, String> getLastRunDateMap() {
        return lastRunDateMap;
    }

    public void setLastRunDateMap(final Map<String, String> lastRunDateMap) {
        this.lastRunDateMap = lastRunDateMap;
    }

    public void setTdsDAO(final TdsHibernateDAO tdsDAO) {
        this.tdsDAO = tdsDAO;
    }

}