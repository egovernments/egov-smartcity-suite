/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) <2015>  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *  	1) All versions of this program, verbatim or modified must carry this
 *  	   Legal Notice.
 *
 *  	2) Any misrepresentation of the origin of the material is prohibited. It
 *  	   is required that all modified versions of this material be marked in
 *  	   reasonable ways as different from the original version.
 *
 *  	3) This license does not grant any rights to any user of the program
 *  	   with regards to rights under trademark law for use of the trade names
 *  	   or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tl.web.actions.citizen.search;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.services.Page;
import org.egov.tl.domain.entity.TradeLicense;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class SearchCitizenLicenseAction.
 * @author laxman
 */
@ParentPackage("egov")
public class SearchCitizenLicenseAction extends BaseFormAction implements ServletRequestAware {

    private static final long serialVersionUID = 1L;
    private final String CITIZENUSER = "citizenUser";
    private int page;
    private int reportSize;
    private String applNumber;
    private String licenseNumber;
    private PaginatedList pagedResults;
    private final String RESULT = "result";
    private HttpSession session = null;
    private HttpServletRequest request;
    private Long userId;
    @Autowired
    private UserService userService;

    public String newForm() {
        return BaseFormAction.NEW;
    }

    @Override
    public void prepare() {
        super.prepare();
        session = request.getSession();
        final User user = userService.getUserByUsername(CITIZENUSER);
        userId = user.getId();
        EgovThreadLocals.setUserId(userId);
        session.setAttribute("com.egov.user.LoginUserName", user.getName());
    }

    @SkipValidation
    public String search() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final Criteria criteria = createSearchQuery();
        if (page == 0) {
            criteria.setProjection(Projections.rowCount());
            reportSize = (Integer) criteria.uniqueResult();
        }
        final ParamEncoder paramEncoder = new ParamEncoder("license");
        final boolean isReport = parameters.get(paramEncoder.encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE)) != null;
        final Page page = new Page(createSearchQuery(), isReport ? 1 : this.page, isReport ? null : 20);
        pagedResults = new EgovPaginatedList(page, reportSize, null, null);
        request.setAttribute("hasResult", !page.getList().isEmpty());
        return RESULT;
    }

    private Criteria createSearchQuery() {
        final Criteria criteria = getPersistenceService().getSession().createCriteria(TradeLicense.class);
        if (applNumber != null || licenseNumber != null) {
            if (StringUtils.isNotBlank(getApplNumber()) && StringUtils.isNotEmpty(getApplNumber()))
                criteria.add(Restrictions.ilike("applicationNumber", getApplNumber()));
            if (StringUtils.isNotBlank(getLicenseNumber()) && StringUtils.isNotEmpty(getLicenseNumber()))
                criteria.add(Restrictions.ilike("licenseNumber", getLicenseNumber()));
        }
        return criteria;
    }

    @Override
    public boolean acceptableParameterName(final String paramName) {
        final List<String> nonAcceptable = Arrays.asList(new String[] { "struts.token.name", "struts.token", "token.name" });
        final boolean retValue = super.acceptableParameterName(paramName);
        return retValue ? !nonAcceptable.contains(paramName) : retValue;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public String getApplNumber() {
        return applNumber;
    }

    public void setApplNumber(final String applNumber) {
        this.applNumber = applNumber;
    }

    public int getPage() {
        return page;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public PaginatedList getPagedResults() {
        return pagedResults;
    }

    public void setPagedResults(final PaginatedList pagedResults) {
        this.pagedResults = pagedResults;
    }

    public int getReportSize() {
        return reportSize;
    }

    public void setReportSize(final int reportSize) {
        this.reportSize = reportSize;
    }

    @Override
    public void setServletRequest(final HttpServletRequest arg0) {
        request = arg0;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(final String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

}
