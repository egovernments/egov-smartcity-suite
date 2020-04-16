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
package org.egov.works.web.actions.reports;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractorbill.entity.ContractorBillRegister;

public class AjaxRetentionMoneyRecoveryRegisterAction extends BaseFormAction {

    private static final long serialVersionUID = 7421372624155467521L;

    private static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";
    private static final String PROJECT_CODE_SEARCH_RESULTS = "projectCodeSearchResults";
    private static final String CONTRACTOR_CODE_SEARCH_RESULTS = "contractorCodeSearchResults";

    private String query;
    private List<String> estimateNumberSearchList = new LinkedList<>();
    private List<String> projectCodeList = new LinkedList<>();
    private List<String> contractorCodeNameList = new LinkedList<>();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String execute() {
        return SUCCESS;
    }

    @Override
    public Object getModel() {
        return null;
    }

    /*
     * Autocomplete for estimates where bills are created
     */
    @ReadOnly
    public String searchEstimateNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(ae.estimateNumber)")
                    .append(" from AbstractEstimate ae")
                    .append(" where ae.parent is null and ae.projectCode.id in ")
                    .append(" (select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = (")
                    .append(" select id from Accountdetailtype where name='PROJECTCODE')")
                    .append(" and bpd.egBilldetailsId.egBillregister.status.code = :billRegisterStatus ")
                    .append(" and expendituretype='Works' ) and UPPER(ae.estimateNumber) like '%'||:estimateNumber||'%'")
                    .append(" and ae.egwStatus.code = :aeStatus )");

            estimateNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("billRegisterStatus", ContractorBillRegister.BillStatus.APPROVED.toString())
                    .setParameter("estimateNumber", query.toUpperCase())
                    .setParameter("aeStatus", AbstractEstimate.EstimateStatus.ADMIN_SANCTIONED.toString())
                    .getResultList();
        }
        return ESTIMATE_NUMBER_SEARCH_RESULTS;
    }

    /*
     * Autocomplete of Project codes where bills are created
     */
    @ReadOnly
    public String searchProjectCode() {
        if (!StringUtils.isEmpty(query)) {
            final StringBuffer strquery = new StringBuffer("select pc.code")
                    .append(" from ProjectCode pc")
                    .append(" where upper(pc.code) like '%'||:pcCode||'%'")
                    .append(" and pc.id in (select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = (")
                    .append(" select id from Accountdetailtype where name='PROJECTCODE')")
                    .append(" and bpd.egBilldetailsId.egBillregister.status.code = :billRegisterStatus ")
                    .append(" and expendituretype='Works')");

            projectCodeList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("pcCode", query.toUpperCase())
                    .setParameter("billRegisterStatus", ContractorBillRegister.BillStatus.APPROVED.toString())
                    .getResultList();
        }
        return PROJECT_CODE_SEARCH_RESULTS;
    }

    /*
     * Autocomplete of Contractor Code/Names where bills are created
     */
    @ReadOnly
    public String searchContractors() {
        if (!StringUtils.isEmpty(query)) {
            final StringBuffer strquery = new StringBuffer("select cont.code||'~'||cont.name")
                    .append(" from Contractor cont")
                    .append(" where upper(cont.code) like '%'||:code||'%' or upper(cont.name) like '%'||:name||'%'")
                    .append(" and cont.id in (select bpd.accountDetailKeyId from EgBillPayeedetails bpd where bpd.accountDetailTypeId = (")
                    .append(" select id from Accountdetailtype where name='contractor')")
                    .append(" and bpd.egBilldetailsId.egBillregister.status.code = :billRegisterStatus ")
                    .append(" and expendituretype = 'Works')");

            contractorCodeNameList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("code", query.toUpperCase())
                    .setParameter("name", query.toUpperCase())
                    .setParameter("billRegisterStatus", ContractorBillRegister.BillStatus.APPROVED.toString())
                    .getResultList();
        }
        return CONTRACTOR_CODE_SEARCH_RESULTS;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<String> getEstimateNumberSearchList() {
        return estimateNumberSearchList;
    }

    public List<String> getProjectCodeList() {
        return projectCodeList;
    }

    public List<String> getContractorCodeNameList() {
        return contractorCodeNameList;
    }
}