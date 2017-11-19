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
/**
 *
 */
package org.egov.egf.web.actions.bill;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.EgBillregistermis;
import org.egov.utils.FinancialConstants;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
@Results({
    @Result(name = BillRegisterSearchAction.NEW, location = "billRegisterSearch-" + BillRegisterSearchAction.NEW + ".jsp")
})
public class BillRegisterSearchAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger
            .getLogger(BillRegisterSearchAction.class);
    private List<String> headerFields = new ArrayList<String>();
    private List<String> mandatoryFields = new ArrayList<String>();
    private EgBillregister billregister;
    private String billDateFrom;
    private String billDateTo;
    private String expType;
    private List<Map<String, Object>> billList;
    @Autowired
    private AppConfigValueService appConfigValueService;
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public BillRegisterSearchAction() {
        billregister = new EgBillregister();
        billregister.setEgBillregistermis(new EgBillregistermis());
        addRelatedEntity("egBillregistermis.egDepartment", Department.class);
        addRelatedEntity("egBillregistermis.fund", Fund.class);
        addRelatedEntity("egBillregistermis.scheme", Scheme.class);
        addRelatedEntity("egBillregistermis.subScheme", SubScheme.class);
        addRelatedEntity("egBillregistermis.functionaryid", Functionary.class);
        addRelatedEntity("egBillregistermis.fundsource", Fundsource.class);
        addRelatedEntity("egBillregistermis.fieldid", Boundary.class);

    }

    @Override
    public Object getModel() {

        return billregister;

    }

    @Override
    public void prepare() {
        super.prepare();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterSearchAction | prepare | Start");
        final List<String> expTypeList = new ArrayList<String>();
        expTypeList.add(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        expTypeList.add(FinancialConstants.STANDARD_EXPENDITURETYPE_WORKS);
        expTypeList.add(FinancialConstants.STANDARD_EXPENDITURETYPE_PURCHASE);
        /*expTypeList.add(FinancialConstants.STANDARD_EXPENDITURETYPE_PENSION);
        expTypeList.add(FinancialConstants.STANDARD_EXPENDITURETYPE_SALARY);*/
        addDropdownData("expType", expTypeList);
        getHeaderFields();
        if (headerFields.contains("department"))
            addDropdownData("departmentList", masterDataCache.get("egi-department"));
        if (headerFields.contains("functionary"))
            addDropdownData("functionaryList", masterDataCache
                    .get("egi-functionary"));
        if (headerFields.contains("fund"))
            addDropdownData("fundList", masterDataCache.get("egi-fund"));
        if (headerFields.contains("fundsource"))
            addDropdownData("fundsourceList", masterDataCache.get("egi-fundSource"));
        if (headerFields.contains("field"))
            addDropdownData("fieldList", masterDataCache.get("egi-ward"));
        if (headerFields.contains("scheme"))
            addDropdownData("schemeList", Collections.EMPTY_LIST);
        if (headerFields.contains("subscheme"))
            addDropdownData("subschemeList", Collections.EMPTY_LIST);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterSearchAction | prepare | End");
    }

    @Action(value = "/bill/billRegisterSearch-newform")
    public String newform() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterSearchAction | newform | Start");
        return NEW;
    }

    @Action(value = "/bill/billRegisterSearch-search")
    public String search() {

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("BillRegisterSearchAction | search | Start");
        final StringBuffer query = new StringBuffer(500);
        query
        .append(
                "select br.expendituretype , br.billtype ,br.billnumber , br.billdate , br.billamount , br.passedamount ,egwstatus.description,billmis.sourcePath,")
                .append(" br.id ,br.status.id,egwstatus.description ,br.state.id,br.lastModifiedBy.id ")
                .append(
                        " from EgBillregister br, EgBillregistermis billmis , EgwStatus egwstatus where   billmis.egBillregister.id = br.id and egwstatus.id = br.status.id  ")
                        .append(" and br.expendituretype=?").append(
                                VoucherHelper
                                .getBillDateQuery(billDateFrom, billDateTo))
                                .append(VoucherHelper.getBillMisQuery(billregister));

        final List<Object[]> list = persistenceService.findAllBy(query.toString(),
                expType);
        final List<Long> stateIds = new ArrayList<Long>();
        final Map<Long, String> stateIdAndOwnerNameMap = new HashMap<Long, String>();
        for (final Object[] object : list)
            stateIds.add(getLongValue(object[11]));
        List<Object[]> oWnerNamesList = new ArrayList<Object[]>();
        if (stateIds != null && stateIds.size() > 0)
            oWnerNamesList = getOwnersForWorkFlowState(stateIds);

        for (final Object[] owner : oWnerNamesList)
            if (!stateIdAndOwnerNameMap.containsKey(getLongValue(owner[1])))
                stateIdAndOwnerNameMap.put(getLongValue(owner[1]), getStringValue(owner[0]));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Total number of bills found =: " + list.size());

        if (list.size() != 0) {
            billList = new ArrayList<Map<String, Object>>();
            Map<String, Object> billMap;

            for (final Object[] object : list) {
                billMap = new HashMap<String, Object>();
                billMap.put("expendituretype", object[0].toString());
                // bill type is coming as null for purchase bill
                String billtype = "";
                if (object[1] != null)
                    billtype = object[1].toString();
                billMap.put("billtype", billtype);
                billMap.put("billnumber", object[2].toString());
                billMap.put("billdate", object[3]);
                billMap.put("billamount", object[4]);
                billMap.put("passedamount", object[5]);
                billMap.put("billstatus", object[6].toString());
                if (null != object[7])
                    billMap.put("sourcepath", object[7].toString());
                else
                    billMap.put("sourcepath",
                            "/EGF/bill/billView-view.action?billId=" + object[8].toString());
                // If bill is created from create bill screen
                if (object[11] != null)
                {
                    if (!(getStringValue(object[10]).equalsIgnoreCase(FinancialConstants.CONTINGENCYBILL_APPROVED_STATUS) || getStringValue(
                            object[10]).equalsIgnoreCase(FinancialConstants.CONTINGENCYBILL_CANCELLED_STATUS)))
                        billMap.put(
                                "ownerName",
                                stateIdAndOwnerNameMap.get(getLongValue(object[11])) != null ? stateIdAndOwnerNameMap
                                        .get(getLongValue(object[11])) : "-");
                    else
                        billMap.put("ownerName", "-");
                } else
                    billMap.put("ownerName", "-");
                billList.add(billMap);
            }
        } else
            billList = new ArrayList<Map<String, Object>>();
        return NEW;
    }

    private List<Object[]> getOwnersForWorkFlowState(final List<Long> stateIds)
    {
        List<Object[]> ownerNamesList = new ArrayList<Object[]>();
        final String ownerNamesQueryStr = "select a.employee.username,bill.state.id from Assignment a,State state, EgBillregister bill"
                + " where  bill.state.id=state.id and a.position.id = state.ownerPosition.id and bill.state.id in (:IDS)";
        int size = stateIds.size();
        if (size > 999)
        {
            int fromIndex = 0;
            int toIndex = 0;
            final int step = 1000;
            List<Object[]> newGLDList;
            while (size - step >= 0)
            {
                newGLDList = new ArrayList<Object[]>();
                toIndex += step;
                final Query ownerNamesQuery = persistenceService.getSession().createQuery(ownerNamesQueryStr);
                ownerNamesQuery.setParameterList("IDS", stateIds.subList(fromIndex, toIndex));
                newGLDList = ownerNamesQuery.list();
                fromIndex = toIndex;
                size -= step;
                if (newGLDList != null)
                    ownerNamesList.addAll(newGLDList);

            }

            if (size > 0)
            {
                newGLDList = new ArrayList<Object[]>();
                fromIndex = toIndex;
                toIndex = fromIndex + size;
                final Query ownerNamesQuery = persistenceService.getSession().createQuery(ownerNamesQueryStr);
                ownerNamesQuery.setParameterList("IDS", stateIds.subList(fromIndex, toIndex));
                newGLDList = ownerNamesQuery.list();
                if (newGLDList != null)
                    ownerNamesList.addAll(newGLDList);
            }

        } else
            ownerNamesList = persistenceService.getSession().createQuery(ownerNamesQueryStr)
            .setParameterList("IDS", stateIds)
            .list();
        return ownerNamesList;
    }

    public EgwStatus getStatusId(final String moduleType, final Integer statusid) {
        final String statusQury = "from EgwStatus where upper(moduletype)=upper('" + moduleType + "') and  id=" + statusid;
        // "upper(description)=upper('"+ statusString + "')";
        final EgwStatus egwStatus = (EgwStatus) persistenceService.find(statusQury);
        return egwStatus;

    }

    protected void getHeaderFields() {
        final List<AppConfigValues> appConfigList = appConfigValueService.getConfigValuesByModuleAndKey("EGF", "DEFAULT_SEARCH_MISATTRRIBUTES");
            for (final AppConfigValues appConfigVal : appConfigList) {
                final String value = appConfigVal.getValue();
                final String header = value.substring(0, value.indexOf('|'));
                headerFields.add(header);
                final String mandate = value.substring(value.indexOf('|') + 1);
                if (mandate.equalsIgnoreCase("M"))
                    mandatoryFields.add(header);
            }

    }

    public void setMandatoryFields(final List<String> mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
    }

    public boolean isFieldMandatory(final String field) {
        return mandatoryFields.contains(field);
    }

    public boolean shouldShowHeaderField(final String field) {
        return headerFields.contains(field);
    }

    public void setBillregister(final EgBillregister billregister) {
        this.billregister = billregister;
    }

    public void setExpType(final String expType) {
        this.expType = expType;
    }

    public void setBillDateFrom(final String billDateFrom) {
        this.billDateFrom = billDateFrom;
    }

    public void setBillDateTo(final String billDateTo) {
        this.billDateTo = billDateTo;
    }

    public void setBillList(final List<Map<String, Object>> billList) {
        this.billList = billList;
    }

    public String getBillDateFrom() {
        return billDateFrom;
    }

    public String getBillDateTo() {
        return billDateTo;
    }

    public String getExpType() {
        return expType;
    }

    public List<Map<String, Object>> getBillList() {
        return billList;
    }

    public void setHeaderFields(final List<String> headerFields) {
        this.headerFields = headerFields;
    }

    private Long getLongValue(final Object object) {
        return object != null ? new Long(object.toString()) : 0;
    }

    private String getStringValue(final Object object) {
        return object != null ? object.toString() : "";
    }
}
