/*
 * @(#)SearchCitizenLicenseAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.citizen.search;

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
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.Page;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
	private Integer userId;

	public String newForm() {
		return BaseFormAction.NEW;
	}

	@Override
	public void prepare() {
		super.prepare();
		this.session = this.request.getSession();
		final UserDAO userDao = new UserDAO();
		final User user = userDao.getUserByUserName(this.CITIZENUSER);
		this.userId = user.getId();
		EGOVThreadLocals.setUserId(this.userId.toString());
		this.session.setAttribute("com.egov.user.LoginUserName", user.getUserName());
	}

	@SkipValidation
	public String search() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		final Criteria criteria = this.createSearchQuery();
		if (this.page == 0) {
			criteria.setProjection(Projections.rowCount());
			this.reportSize = (Integer) criteria.uniqueResult();
		}
		final ParamEncoder paramEncoder = new ParamEncoder("license");
		final boolean isReport = this.parameters.get(paramEncoder.encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE)) != null;
		final Page page = new Page(this.createSearchQuery(), isReport ? 1 : this.page, isReport ? null : 20);
		this.pagedResults = new EgovPaginatedList(page, this.reportSize, null, null);
		request.setAttribute("hasResult", !page.getList().isEmpty());
		return this.RESULT;
	}

	private Criteria createSearchQuery() {
		final Criteria criteria = this.getPersistenceService().getSession().createCriteria(TradeLicense.class);
		if (this.applNumber != null || this.licenseNumber != null) {
			if (StringUtils.isNotBlank(this.getApplNumber()) && StringUtils.isNotEmpty(this.getApplNumber())) {
				criteria.add(Restrictions.ilike("applicationNumber", this.getApplNumber()));
			}
			if (StringUtils.isNotBlank(this.getLicenseNumber()) && StringUtils.isNotEmpty(this.getLicenseNumber())) {
				criteria.add(Restrictions.ilike("licenseNumber", this.getLicenseNumber()));
			}
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
		return this.applNumber;
	}

	public void setApplNumber(final String applNumber) {
		this.applNumber = applNumber;
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(final int page) {
		this.page = page;
	}

	public PaginatedList getPagedResults() {
		return this.pagedResults;
	}

	public void setPagedResults(final PaginatedList pagedResults) {
		this.pagedResults = pagedResults;
	}

	public int getReportSize() {
		return this.reportSize;
	}

	public void setReportSize(final int reportSize) {
		this.reportSize = reportSize;
	}

	@Override
	public void setServletRequest(final HttpServletRequest arg0) {
		this.request = arg0;
	}

	public String getLicenseNumber() {
		return this.licenseNumber;
	}

	public void setLicenseNumber(final String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

}
