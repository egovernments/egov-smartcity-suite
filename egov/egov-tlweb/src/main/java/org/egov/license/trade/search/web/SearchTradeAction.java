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
package org.egov.license.trade.search.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.egov.infstr.services.Page;
import org.egov.lib.admbndry.Boundary;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.entity.objection.Notice;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.search.form.SearchForm;
import org.egov.license.utils.Constants;
import org.egov.license.utils.LicenseUtils;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * The Class TradeSearchAction.
 */
@ParentPackage("egov")
public class SearchTradeAction extends BaseFormAction {
	private static final Logger LOGGER = Logger.getLogger(SearchTradeAction.class);
	private static final long serialVersionUID = 1L;
	private PaginatedList pagedResults;
	private int page;
	private int reportSize;
	private final SearchForm searchForm = new SearchForm();
	private LicenseUtils licenseUtils;
	private List<String> noticelist = new ArrayList<String>();
	private String roleName;
	

	public String newForm() {
		return BaseFormAction.NEW;
	}

	public Object getModel() {
		return this.searchForm;
	}

	public int getPage() {
		return this.page;
	}

	public PaginatedList getPagedResults() {
		return this.pagedResults;
	}

	public int getReportSize() {
		return this.reportSize;
	}

	@Override
	public void prepare() {
		noticelist.add(Constants.DROPDOWN_PRENOTICE);
		noticelist.add(Constants.DROPDOWN_SCNOTICE);
		noticelist.add(Constants.DROPDOWN_SUSNOTICE);
		noticelist.add(Constants.DROPDOWN_CANNOTICE);
		super.prepare();
		this.addDropdownData(Constants.DROPDOWN_AREA_LIST_LICENSE, new ArrayList<Boundary>());
		this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE, new ArrayList<Boundary>());
		this.addDropdownData(Constants.DROPDOWN_ZONE_LIST, this.licenseUtils.getAllZone());
		this.addDropdownData(Constants.DROPDOWN_TRADENAME_LIST, this.licenseUtils.getAllTradeNames("TradeLicense"));
		this.addDropdownData(Constants.DROPDOWN_NOTICE_LIST, noticelist);
		Integer userId = (Integer) session().get(Constants.SESSIONLOGINID);
		if (userId != null) {
			setRoleName(licenseUtils.getRolesForUserId(userId));
		}
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
		return BaseFormAction.NEW;
	}

	private Criteria createSearchQuery() {
		Criteria criteria;
		if (this.searchForm != null && this.searchForm.getNoticeNumber().equals("") && 
				this.searchForm.getDocNumber().equals("") && this.searchForm.getNoticeType().equals("-1")
				&& this.searchForm.getNoticeFromDate()==null && this.searchForm.getNoticeToDate()==null) {
			criteria = this.getPersistenceService().getSession().createCriteria(TradeLicense.class);
			if (StringUtils.isNotBlank(this.searchForm.getLicenseNumber())) {
				criteria.add(Restrictions.ilike("licenseNumber", "%" + this.searchForm.getLicenseNumber() + "%"));
			} else {
				if (StringUtils.isNotBlank(this.searchForm.getApplNumber())) {
					criteria.add(Restrictions.ilike("applicationNumber", "%" + this.searchForm.getApplNumber() + "%"));
				}
				if (StringUtils.isNotBlank(this.searchForm.getApplicantName())) {
					criteria.createAlias("licensee", "lsncy");
					criteria.add(Restrictions.ilike("lsncy.applicantName", "%" + this.searchForm.getApplicantName() + "%"));
				}
				if (this.searchForm.getApplicationFromDate() != null) {
					criteria.add(Restrictions.ge("applicationDate", this.searchForm.getApplicationFromDate()));
				}
				if (this.searchForm.getApplicationToDate() != null) {
					criteria.add(Restrictions.le("applicationDate", this.searchForm.getApplicationToDate()));
				}
				if (StringUtils.isNotBlank(this.searchForm.getEstablishmentName())) {
					criteria.add(Restrictions.ilike("nameOfEstablishment", "%" + this.searchForm.getEstablishmentName() + "%"));
				}
				if (StringUtils.isNotBlank(this.searchForm.getOldLicenseNumber())) {
					criteria.add(Restrictions.ilike("oldLicenseNumber", "%" + this.searchForm.getOldLicenseNumber() + "%"));
				}
				if (StringUtils.isNotBlank(this.searchForm.getTradeName()) && !this.searchForm.getTradeName().equals("-1")) {
					criteria.createAlias("tradeName", "trdname");
					criteria.add(Restrictions.eq("trdname.id", Long.valueOf(this.searchForm.getTradeName())));
				}
				if (this.searchForm.getLicenseFeeFrom() != null) {
					criteria.add(Restrictions.ge("totalLicenseFee", this.searchForm.getLicenseFeeFrom()));
				}
				if (this.searchForm.getLicenseFeeTo() != null) {
					criteria.add(Restrictions.le("totalLicenseFee", this.searchForm.getLicenseFeeTo()));
				}
				if (StringUtils.isNotBlank(this.searchForm.getZone()) && !this.searchForm.getZone().equals("-1")) {
					/* To solve hibernate query problem */
					criteria.createAlias("boundary", "bndry");
					criteria.createAlias("boundary.parent", "parentBndry");

					Criterion ct1 = Restrictions.eq("parentBndry.id", Integer.valueOf(this.searchForm.getZone()));
					Criterion ct2 = Restrictions.eq("bndry.id", Integer.valueOf(this.searchForm.getZone()));	
					criteria.add(Restrictions.or(ct1,ct2));
					this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE,this.licenseUtils.getChildBoundaries(this.searchForm.getZone()));
				}
				if (StringUtils.isNotBlank(this.searchForm.getDivision()) && !this.searchForm.getDivision().equals("-1")) {
					criteria.add(Restrictions.eq("bndry.id", Integer.valueOf(this.searchForm.getDivision())));
				}
				if (this.searchForm.isMotorInstalled()) {
					criteria.add(Restrictions.eq("motorInstalled", this.searchForm.isMotorInstalled()));
				}
				criteria.createAlias("status", "sts");
				if (this.searchForm.isLicenseCancelled()) {
					criteria.add(Restrictions.eq("sts.statusCode", "CAN"));
				} else if (this.searchForm.isLicenseObjected()) {
					criteria.add(Restrictions.eq("sts.statusCode", "OBJ"));
				} else if (this.searchForm.isLicenseRejected()) {
					criteria.add(Restrictions.eq("sts.statusCode", "REJ"));
				} else if (this.searchForm.isLicenseexpired()) {
					criteria.add(Restrictions.eq("sts.statusCode", "EXP"));
				} else if (this.searchForm.isLicenseSuspended()) {
					criteria.add(Restrictions.eq("sts.statusCode", "SUS"));
				} else {
					final Criterion actstat = Restrictions.eq("sts.statusCode", "ACT");
					final Criterion ackstat = Restrictions.eq("sts.statusCode", "ACK");
					final Criterion uwfstat = Restrictions.eq("sts.statusCode", "UWF");
					final LogicalExpression orExp = Restrictions.or(actstat, ackstat);
					final LogicalExpression orExp2 = Restrictions.or(orExp, uwfstat);
					// criteria.add(orExp);
					criteria.add(orExp2);
				}

			}
			criteria.addOrder(Order.desc("createdDate"));
			return criteria;
		}
		//Search for notices of license
		else{
			//this.getPersistenceService().setType(Notice.class);
			criteria = this.getPersistenceService().getSession().createCriteria(Notice.class);
			criteria.createAlias("objection", "object");
			criteria.createAlias("object.license", "objl");
			if (StringUtils.isNotBlank(this.searchForm.getLicenseNumber())) {
				criteria.add(Restrictions.ilike("objl.licenseNumber", "%" + this.searchForm.getLicenseNumber() + "%"));
			}
			if (StringUtils.isNotBlank(this.searchForm.getApplNumber())) {
					criteria.add(Restrictions.ilike("objl.applicationNumber", "%" + this.searchForm.getApplNumber() + "%"));
			}
			if (StringUtils.isNotBlank(this.searchForm.getApplicantName())) {
				criteria.createAlias("objl.licensee", "lsncy");
				criteria.add(Restrictions.ilike("lsncy.applicantName", "%" + this.searchForm.getApplicantName() + "%"));
			}
			if (this.searchForm.getApplicationFromDate() != null) {
				criteria.add(Restrictions.ge("objl.applicationDate", this.searchForm.getApplicationFromDate()));
			}
			
			if (this.searchForm.getApplicationToDate() != null) {
				criteria.add(Restrictions.le("objl.applicationDate", this.searchForm.getApplicationToDate()));
			}
			
			if (StringUtils.isNotBlank(this.searchForm.getEstablishmentName())) {
				criteria.add(Restrictions.ilike("objl.nameOfEstablishment", "%" + this.searchForm.getEstablishmentName() + "%"));
			}
			
			if (StringUtils.isNotBlank(this.searchForm.getOldLicenseNumber())) {
				criteria.add(Restrictions.ilike("objl.oldLicenseNumber", "%" + this.searchForm.getOldLicenseNumber() + "%"));
			}
			if (StringUtils.isNotBlank(this.searchForm.getTradeName()) && !this.searchForm.getTradeName().equals("-1")) {
				criteria.createAlias("objl.tradeName", "trdname");
				criteria.add(Restrictions.eq("trdname.id", Long.valueOf(this.searchForm.getTradeName())));
			}
			
			if (this.searchForm.getLicenseFeeFrom() != null) {
				criteria.add(Restrictions.ge("objl.totalLicenseFee", this.searchForm.getLicenseFeeFrom()));
			}
			if (this.searchForm.getLicenseFeeTo() != null) {
				criteria.add(Restrictions.le("objl.totalLicenseFee", this.searchForm.getLicenseFeeTo()));
			}
			if (StringUtils.isNotBlank(this.searchForm.getZone()) && !this.searchForm.getZone().equals("-1")) {
				/* To solve hibernate query problem */
				criteria.createAlias("objl.boundary", "bndry");
				criteria.createAlias("objl.boundary.parent", "parentBndry");

				criteria.add(Restrictions.eq("parentBndry.id", Integer.valueOf(this.searchForm.getZone())));
				this.addDropdownData(Constants.DROPDOWN_DIVISION_LIST_LICENSE,this.licenseUtils.getChildBoundaries(this.searchForm.getZone()));
			}
			if (StringUtils.isNotBlank(this.searchForm.getDivision()) && !this.searchForm.getDivision().equals("-1")) {
				criteria.add(Restrictions.eq("bndry.id", Integer.valueOf(this.searchForm.getDivision())));
			}
			if (this.searchForm.isMotorInstalled()) {
				criteria.add(Restrictions.eq("objl.motorInstalled", this.searchForm.isMotorInstalled()));
			}
			
			if(StringUtils.isNotBlank(this.searchForm.getNoticeNumber())){
				criteria.add(Restrictions.ilike("noticeNumber", "%" + this.searchForm.getNoticeNumber() + "%"));
			}
			if (StringUtils.isNotBlank(this.searchForm.getDocNumber())) {
				criteria.add(Restrictions.ilike("docNumber", "%" + this.searchForm.getDocNumber() + "%"));
			}
			if (StringUtils.isNotBlank(this.searchForm.getNoticeType()) && !this.searchForm.getNoticeType().equals("-1")) {
				criteria.add(Restrictions.ilike("noticeType", "%" + this.searchForm.getNoticeType() + "%"));
			}
			if (this.searchForm.getNoticeFromDate() != null) {
				criteria.add(Restrictions.ge("noticeDate", this.searchForm.getNoticeFromDate()));
			}
			if (this.searchForm.getNoticeToDate() != null) {
				criteria.add(Restrictions.le("noticeDate", this.searchForm.getNoticeToDate()));
			}
			
			criteria.createAlias("object.license.status", "sts");
			if (this.searchForm.isLicenseCancelled()) {
				criteria.add(Restrictions.eq("sts.statusCode", "CAN"));
			} else if (this.searchForm.isLicenseObjected()) {
				criteria.add(Restrictions.eq("sts.statusCode", "OBJ"));
			} else if (this.searchForm.isLicenseRejected()) {
				criteria.add(Restrictions.eq("sts.statusCode", "REJ"));
			} else if (this.searchForm.isLicenseexpired()) {
				criteria.add(Restrictions.eq("sts.statusCode", "EXP"));
			} else if (this.searchForm.isLicenseSuspended()) {
				criteria.add(Restrictions.eq("sts.statusCode", "SUS"));
			} else {
				final Criterion actstat = Restrictions.eq("sts.statusCode", "ACT");
				final Criterion ackstat = Restrictions.eq("sts.statusCode", "ACK");
				final Criterion uwfstat = Restrictions.eq("sts.statusCode", "UWF");
				final Criterion objstat = Restrictions.eq("sts.statusCode", "OBJ");
				final Criterion rejstat = Restrictions.eq("sts.statusCode", "REJ");
				final Criterion expstat = Restrictions.eq("sts.statusCode", "EXP");
				final Criterion susstat = Restrictions.eq("sts.statusCode", "SUS");
				final LogicalExpression orExp = Restrictions.or(actstat, ackstat);
				final LogicalExpression orExp2 = Restrictions.or(orExp, uwfstat);
				final LogicalExpression orExp3 = Restrictions.or(orExp2, objstat);
				final LogicalExpression orExp4 = Restrictions.or(orExp3, rejstat);
				final LogicalExpression orExp5 = Restrictions.or(orExp4, expstat);
				final LogicalExpression orExp6 = Restrictions.or(orExp5, susstat);
				// criteria.add(orExp);
				criteria.add(orExp6);
			}
			criteria.addOrder(Order.desc("createdDate"));
			return criteria;
		}
		
	}
	
	@SkipValidation
	public String searchPortal() {
		final HttpServletRequest request = ServletActionContext.getRequest();
		final Criteria criteria = this.createSearchQueryPortal();
		if (this.page == 0) {
			criteria.setProjection(Projections.rowCount());
			this.reportSize = (Integer) criteria.uniqueResult();
		}

		final ParamEncoder paramEncoder = new ParamEncoder("license");
		final boolean isReport = this.parameters.get(paramEncoder.encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE)) != null;
		final Page page = new Page(this.createSearchQueryPortal(), isReport ? 1 : this.page, isReport ? null : 20);
		this.pagedResults = new EgovPaginatedList(page, this.reportSize, null, null);
		request.setAttribute("hasResult", !page.getList().isEmpty());
		return "portal";
	}

	private Criteria createSearchQueryPortal() {
		final Criteria criteria = this.getPersistenceService().getSession().createCriteria(TradeLicense.class);
		criteria.add(Restrictions.isNull("licenseNumber"));
		criteria.add(Restrictions.isNull("state"));
		criteria.addOrder(Order.desc("createdDate"));
		return criteria;
	}

	@Override
	public boolean acceptableParameterName(final String paramName) {
		final List<String> nonAcceptable = Arrays.asList(new String[] { "struts.token.name", "struts.token", "token.name" });
		final boolean retValue = super.acceptableParameterName(paramName);
		return retValue ? !nonAcceptable.contains(paramName) : retValue;
	}

	public boolean checkForRenewalNotice(Date dateOfExpiry) {
		boolean readyForRenewal = false;
		final Calendar currentDate = Calendar.getInstance();
		final Calendar renewalDate = Calendar.getInstance();
		renewalDate.setTime(dateOfExpiry);
		renewalDate.add(renewalDate.DATE, Constants.RENEWALTIMEPERIOD);

		if (renewalDate.before(currentDate) || renewalDate.equals(currentDate)) {
			readyForRenewal = true;
		}
		return readyForRenewal;
	}
//need to fix for Repeating 
	public boolean isRenewable(Long licenseId)
	    {
	    	License license=(License)	persistenceService.find("from License where id=?",licenseId);
	    	boolean isRenewable=false;
	    	String dateDiffToExpiryDate = license.getDateDiffToExpiryDate(new Date());
	    	if(dateDiffToExpiryDate!=null)
	    	{
	    		boolean isExpired;
	    		String[] split = dateDiffToExpiryDate.split("/");
	    		isExpired=split[0].equalsIgnoreCase("false")?false:true;
	    		int noOfMonths = Integer.parseInt(split[1]);
	    		
	    		if(isExpired==false && noOfMonths>=-1 && noOfMonths<=1)
	    			isRenewable=true;
	    		else if(isExpired==true && noOfMonths<=6)
	    		{
	    			isRenewable=true;
	    		}
	    		   	
	    		else 
	    			isRenewable=false;
	    	}
	    	return isRenewable;  
	    }
	
	public boolean isNocApplicable(Long licenseId)
	{
		License license=(License)	persistenceService.find("from License where id=?",licenseId);
    	boolean isNocApplicable=false;
    	isNocApplicable = license.getTradeName().isNocApplicable();
    	return isNocApplicable;
	}


	public void setLicenseUtils(final LicenseUtils licenseUtils) {
		this.licenseUtils = licenseUtils;
	}

	public void setPage(final int page) {
		this.page = page;
	}

	public void setPagedResults(final PaginatedList pagedResults) {
		this.pagedResults = pagedResults;
	}

	public void setReportSize(final int reportSize) {
		this.reportSize = reportSize;
	}
	
	public String getRoleName() {
    	return roleName;
    }

	public void setRoleName(String roleName) {
    	this.roleName = roleName;
    }
}
