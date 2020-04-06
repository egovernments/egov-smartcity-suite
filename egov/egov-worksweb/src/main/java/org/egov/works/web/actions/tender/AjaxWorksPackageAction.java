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
package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.infra.persistence.entity.component.Money;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.models.tender.TenderResponse;
import org.egov.works.models.tender.WorksPackage;
import org.egov.works.services.AbstractEstimateService;

@ParentPackage("egov")
@Results({
        @Result(name = AjaxWorksPackageAction.ESTIMATE_LIST, location = "ajaxWorksPackage-estList.jsp"),
        @Result(name = AjaxWorksPackageAction.TENDERFILENUMBERUNIQUECHECK, location = "ajaxWorksPackage-tenderFileNumberUniqueCheck.jsp"),
        @Result(name = AjaxWorksPackageAction.TENDER_RESPONSE_CHECK, location = "ajaxWorksPackage-tenderResponseCheck.jsp"),
        @Result(name = AjaxWorksPackageAction.WP_NUMBER_SEARCH_RESULTS, location = "ajaxWorksPackage-wpNoSearchResults.jsp"),
        @Result(name = AjaxWorksPackageAction.TENDER_FILE_NUMBER_SEARCH_RESULTS, location = "ajaxWorksPackage-tenderFileNoSearchResults.jsp"),
        @Result(name = AjaxWorksPackageAction.ESTIMATE_NUMBER_SEARCH_RESULTS, location = "ajaxWorksPackage-estimateNoSearchResults.jsp")
})
public class AjaxWorksPackageAction extends BaseFormAction {

    private static final long serialVersionUID = -5753205367102548473L;
    public static final String ESTIMATE_LIST = "estList";
    private List<AbstractEstimate> abstractEstimateList = new ArrayList<>();
    private AbstractEstimateService abstractEstimateService;
    private Money worktotalValue;
    private String estId;
    private String wpId;

    private WorksPackage worksPackage = new WorksPackage();

    public static final String TENDERFILENUMBERUNIQUECHECK = "tenderFileNumberUniqueCheck";
    private Long id;
    private String tenderFileNumber;
    public static final String TENDER_RESPONSE_CHECK = "tenderResponseCheck";
    private boolean tenderResponseCheck;
    private String tenderNegotiationNo;
    private String query = "";
    private List<WorksPackage> wpList = new LinkedList<>();
    public static final String WP_NUMBER_SEARCH_RESULTS = "wpNoSearchResults";
    public static final String TENDER_FILE_NUMBER_SEARCH_RESULTS = "tenderFileNoSearchResults";
    private String mode;
    private List<String> estimateNumberSearchList = new LinkedList<>();
    public static final String ESTIMATE_NUMBER_SEARCH_RESULTS = "estimateNoSearchResults";

    @PersistenceContext
    private EntityManager entityManager;

    @Action(value = "/tender/ajaxWorksPackage-estimateList")
    public String estimateList() {
        if (StringUtils.isNotBlank(estId)) {
            abstractEstimateList = abstractEstimateService.getAbEstimateListById(estId);
            setWorktotalValue(abstractEstimateService.getWorkValueIncludingTaxesForEstList(abstractEstimateList));
        }
        return ESTIMATE_LIST;
    }

    @Action(value = "/tender/ajaxWorksPackage-tenderFileNumberUniqueCheck")
    public String tenderFileNumberUniqueCheck() {
        return TENDERFILENUMBERUNIQUECHECK;
    }

    @SuppressWarnings("deprecation")
    public boolean getTenderFileNumberCheck() {
        boolean tenderFileNoexistsOrNot = false;
        Long wpId = null;
        if (id == null || id == 0) {
            if (getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheck", tenderFileNumber) != null)
                wpId = (Long) getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheck", tenderFileNumber);
        } else if (getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheckForEdit", tenderFileNumber, id) != null)
            wpId = (Long) getPersistenceService().findByNamedQuery("TenderFileNumberUniqueCheckForEdit",
                    tenderFileNumber, id);

        if (wpId != null)
            worksPackage = entityManager.find(WorksPackage.class, wpId);

        if (worksPackage != null && worksPackage.getId() != null)
            tenderFileNoexistsOrNot = true;

        return tenderFileNoexistsOrNot;
    }

    @Action(value = "/tender/ajaxWorksPackage-isTRPresentForWPCheck")
    public String isTRPresentForWPCheck() {
        tenderResponseCheck = false;
        tenderNegotiationNo = "";
        if (!StringUtils.isEmpty(wpId)) {
            final StringBuffer query = new StringBuffer("from TenderResponse tr")
                    .append(" where tr.tenderEstimate.worksPackage.id = :wpId and tr.egwStatus.code != 'CANCELLED'");

            final List<TenderResponse> trList = entityManager.createQuery(query.toString(), TenderResponse.class)
                    .setParameter("wpId", Long.valueOf(wpId))
                    .getResultList();

            if (trList != null && trList.size() > 0) {
                tenderResponseCheck = true;
                tenderNegotiationNo = trList.get(0).getNegotiationNumber();
            }
        }
        return TENDER_RESPONSE_CHECK;
    }

    @Action(value = "/tender/ajaxWorksPackage-searchWorksPackageNumber")
    public String searchWorksPackageNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            TypedQuery<WorksPackage> qry = null;
            if (mode != null && mode.equalsIgnoreCase("cancelWP")) {
                strquery.append(
                        "from WorksPackage as wp where wp.wpNumber like '%'||:wpNumber||'%' and wp.egwStatus.code = :status ");

                qry = entityManager.createQuery(strquery.toString(), WorksPackage.class)
                        .setParameter("wpNumber", query.toUpperCase())
                        .setParameter("status", "APPROVED");
            } else {
                strquery.append(
                        "from WorksPackage as wp where wp.wpNumber like '%'||:wpNumber||'%' and wp.egwStatus.code <> :status ");

                qry = entityManager.createQuery(strquery.toString(), WorksPackage.class)
                        .setParameter("wpNumber", query.toUpperCase())
                        .setParameter("status", "NEW");
            }

            wpList = qry.getResultList();
        }
        return WP_NUMBER_SEARCH_RESULTS;
    }

    @Action(value = "/tender/ajaxWorksPackage-searchTenderFileNumber")
    public String searchTenderFileNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append(
                    "from WorksPackage as wp where wp.tenderFileNumber like '%'||:tenderFileNumber||'%' and wp.egwStatus.code <> :status");
            wpList = entityManager.createQuery(strquery.toString(), WorksPackage.class)
                    .setParameter("tenderFileNumber", query.toUpperCase())
                    .setParameter("status", "NEW")
                    .getResultList();
        }
        return TENDER_FILE_NUMBER_SEARCH_RESULTS;
    }

    @Action(value = "/tender/ajaxWorksPackage-searchEstimateNumber")
    public String searchEstimateNumber() {
        final StringBuffer strquery = new StringBuffer();
        if (!StringUtils.isEmpty(query)) {
            strquery.append("select distinct(wpd.estimate.estimateNumber)")
                    .append(" from WorksPackageDetails wpd")
                    .append(" where wpd.worksPackage.egwStatus.code <> :status and wpd.estimate.estimateNumber like '%'||:estimateNumber||'%' ");

            estimateNumberSearchList = entityManager.createQuery(strquery.toString(), String.class)
                    .setParameter("status", "NEW")
                    .setParameter("estimateNumber", query.toUpperCase())
                    .getResultList();

        }
        return ESTIMATE_NUMBER_SEARCH_RESULTS;
    }

    public Money getWorktotalValue() {
        return worktotalValue;
    }

    public void setWorktotalValue(final Money worktotalValue) {
        this.worktotalValue = worktotalValue;
    }

    public String getEstId() {
        return estId;
    }

    public void setEstId(final String estId) {
        this.estId = estId;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public void setAbstractEstimateService(final AbstractEstimateService abstractEstimateService) {
        this.abstractEstimateService = abstractEstimateService;
    }

    public List<AbstractEstimate> getAbstractEstimateList() {
        return abstractEstimateList;
    }

    public void setAbstractEstimateList(final List<AbstractEstimate> abstractEstimateList) {
        this.abstractEstimateList = abstractEstimateList;
    }

    public WorksPackage getWorksPackage() {
        return worksPackage;
    }

    public void setWorksPackage(final WorksPackage worksPackage) {
        this.worksPackage = worksPackage;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTenderFileNumber() {
        return tenderFileNumber;
    }

    public void setTenderFileNumber(final String tenderFileNumber) {
        this.tenderFileNumber = tenderFileNumber;
    }

    public void setWpId(final String wpId) {
        this.wpId = wpId;
    }

    public boolean getTenderResponseCheck() {
        return tenderResponseCheck;
    }

    public String getTenderNegotiationNo() {
        return tenderNegotiationNo;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<WorksPackage> getWpList() {
        return wpList;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public List<String> getEstimateNumberSearchList() {
        return estimateNumberSearchList;
    }

}
