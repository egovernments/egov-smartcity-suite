/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.web.actions.extd.report;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.ReportResultExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.utils.ApplicationMode;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.utils.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@ParentPackage("egov")
public class RegisterReportExtnAction extends BaseFormAction {
	
	@PersistenceContext
	private EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public static final String showdetail = "showdetail";
	private List<EgwStatus> statusList = new ArrayList<EgwStatus>(0);
	private List<ServiceTypeExtn> servicelist = new ArrayList<ServiceTypeExtn>(
			0);
	private List<RegistrationExtn> regList = new ArrayList<RegistrationExtn>(0);

	private Long serviceType;
	private Integer egwStatus;
	private Long stateid;
	private String appMode;
	private String searchMode;
	private Integer adminboundaryid;
	private Integer locboundaryid;
	private Date applicationFromDate;
	private Date applicationToDate;
	private String statecode;
	private Long servcode;
	private BpaCommonExtnService bpaCommonExtnService;
	private Map<String, TreeMap<String, Long>> statusmap = new TreeMap<String, TreeMap<String, Long>>();
	StringBuffer qryStr = new StringBuffer();

	public RegisterReportExtnAction() {

	}
	@Action(value = "/registerReportExtn-newform", results = { @Result(name = NEW,type = "dispatcher") })
	public String newform() {
		return NEW;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "/registerReportExtn-showdetail", results = { @Result(name = showdetail,type = "dispatcher") })
	public String showdetail() {
		if (getServcode() != null && getServcode() == 0)
			qryStr.append(" from RegistrationExtn reg where id is not null and reg.egwStatus.code= :statcode ");
		else
			qryStr.append(" from RegistrationExtn reg where id is not null and reg.serviceType.code = :servecode and reg.egwStatus.code= :statcode ");
		getappendquery();
		qryStr.append(" order by id");

		Query query = getCurrentSession().createQuery(qryStr.toString());
		if (getServcode() != null && getServcode() != 0)
			query.setLong("servecode", getServcode() != null ? getServcode()
					: getServiceType());

		query.setString("statcode", getStatecode());
		if (getStatecode() == null) {
			query.setInteger("statcode", getEgwStatus());
		}
		if (getApplicationFromDate() != null) {
			query.setDate("fromdate", getApplicationFromDate());
		}
		if (getApplicationToDate() != null) {
			query.setDate("todate", getApplicationToDate());
		}
		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			query.setInteger("adminid", getAdminboundaryid());
		}
		if (getLocboundaryid() != null && getLocboundaryid() != -1) {
			query.setInteger("locid", getLocboundaryid());

		}
		if (getAppMode() != null && !"-1".equals(getAppMode())) {
			query.setString("appmodename", getAppMode());
		}
		regList = query.list();
		// regList=persistenceService.findAllBy("from Registration where serviceType.id=? and egwStatus.code=? order by id",getServcode(),getStatecode());
		return showdetail;
	}

	@ValidationErrorPage(NEW)
	@Action(value = "/registerReportExtn-searchResults", results = { @Result(name = NEW,type = "dispatcher") })
	public String searchResults() {
		search();
		setSearchMode("result");
		return NEW;
	}

	public void prepare() {
		super.prepare();
		addDropdownData("statusList", persistenceService.findAllBy(
				"from EgwStatus where moduletype=? order by code ",
				BpaConstants.NEWBPAREGISTRATIONMODULE));
		addDropdownData("serviceTypeList",
				persistenceService
						.findAllBy("from ServiceTypeExtn order by code"));
		addDropdownData("applicationModeList",
				Arrays.asList(ApplicationMode.values()));
	}

	public void getappendquery() {
		if (getApplicationFromDate() != null) {
			qryStr.append(" and reg.planSubmissionDate>= :fromdate ");
		}
		if (getApplicationToDate() != null) {
			qryStr.append(" and reg.planSubmissionDate<= :todate ");
		}
		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			Boundary boundary = (Boundary) persistenceService.find(
					"from BoundaryImpl where id=?", getAdminboundaryid());

			if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Ward")) {

				qryStr.append(" and reg.adminboundaryid.id= :adminid");
			} else if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Zone")) {
				qryStr.append(" and reg.adminboundaryid.id in (select id from BoundaryImpl where parent.id= :adminid )");

			} else if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Region")) {
				qryStr.append(" and reg.adminboundaryid.id in (select id from BoundaryImpl where parent.parent.id= :adminid )");

			}
		}
		if (getLocboundaryid() != null && getLocboundaryid() != -1) {
			Boundary boundaryLoc = (Boundary) persistenceService.find(
					"from BoundaryImpl where id=?", getLocboundaryid());

			if (boundaryLoc != null
					&& boundaryLoc.getBoundaryType().getName().equals("Street")) {
				qryStr.append(" and reg.locboundaryid.id= :locid ");
			}

			else if (boundaryLoc != null
					&& boundaryLoc.getBoundaryType().getName()
							.equals("Locality")) {
				qryStr.append("	and (reg.locboundaryid.id in (select id from BoundaryImpl where parent.id= :locid) OR  reg.locboundaryid.id= :locid ) ");
			} else if (boundaryLoc != null
					&& boundaryLoc.getBoundaryType().getName().equals("Area")) {
				qryStr.append(" and (reg.locboundaryid.id in (select id from BoundaryImpl where parent.id= :locid OR parent.id in (select id from BoundaryImpl where parent.id= :locid) ) OR reg.locboundaryid.id= :locid) ");

			}
		}
		if (getAppMode() != null && !"-1".equals(getAppMode()))

		{
			qryStr.append(" and reg.appMode= :appmodename");
		}
	}

	@SuppressWarnings("unchecked")
	public void search() {

		if (getEgwStatus() != -1) {
			statusList = persistenceService.findAllBy(
					"from EgwStatus where moduletype=? and id=? order by code",
					BpaConstants.NEWBPAREGISTRATIONMODULE, getEgwStatus());
		} else
			statusList = persistenceService.findAllBy(
					"from EgwStatus where moduletype=? order by code",
					BpaConstants.NEWBPAREGISTRATIONMODULE);

		if (getServiceType() != -1) {
			servicelist = persistenceService.findAllBy(
					"from ServiceTypeExtn where id=? order by code",
					getServiceType());
		} else
			servicelist = persistenceService
					.findAllBy("from ServiceTypeExtn order by code");

		for (EgwStatus status : statusList) {
			TreeMap<String, Long> servicemap = getEmptyServiceCodeWithZeroValue(servicelist);
			statusmap.put(status.getCode(), servicemap);
		}
		// StringBuffer qryStr = new StringBuffer();
		qryStr.append("SELECT DISTINCT reg.serviceType.code as servicetypecode ,COUNT(reg.id) as count,reg.egwStatus.code as statusName from RegistrationExtn reg where id is not null");
		/*
		 * Registration reg=null; reg.getServiceType().getId()
		 */

		if (getEgwStatus() != null && getEgwStatus() != -1) {
			qryStr.append(" and reg.egwStatus.id= :statusid ");
		}
		if (getServiceType() != null && getServiceType() != -1) {
			qryStr.append(" and reg.serviceType.id= :serviceid ");
		}
		if (applicationFromDate != null
				&& applicationToDate != null
				&& !DateUtils.compareDates(getApplicationToDate(),
						getApplicationFromDate()))
			addFieldError("fromDate", getText("greaterthan.endDate.fromDate"));
		if (applicationToDate != null
				&& !DateUtils.compareDates(new Date(), getApplicationToDate()))
			addFieldError("toDate", getText("greaterthan.endDate.currentdate"));

		getappendquery();
		qryStr.append(" GROUP BY  reg.serviceType.code, reg.egwStatus.code");
		qryStr.append(" order by reg.egwStatus.code");
		Query query = getCurrentSession().createQuery(qryStr.toString());
		// query.setLong("servicetypeid", getServiceType());
		// query.setInteger("statusid", getEgwStatus());
		if (getEgwStatus() != null && getEgwStatus() != -1) {
			query.setInteger("statusid", getEgwStatus());
		}
		if (getServiceType() != null && getServiceType() != -1) {
			query.setLong("serviceid", getServiceType());
		}
		if (getApplicationFromDate() != null) {
			query.setDate("fromdate", getApplicationFromDate());
		}
		if (getApplicationToDate() != null) {
			query.setDate("todate", getApplicationToDate());
		}
		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			query.setInteger("adminid", getAdminboundaryid());
		}
		if (getLocboundaryid() != null && getLocboundaryid() != -1) {
			query.setInteger("locid", getLocboundaryid());

		}
		if (getAppMode() != null && !"-1".equals(getAppMode())) {
			query.setString("appmodename", getAppMode());
		}
		query.setResultTransformer(Transformers
				.aliasToBean(ReportResultExtn.class));

		List<ReportResultExtn> reportList = query.list();
		for (ReportResultExtn result : reportList) {

			statecode = result.getStatusName();
			// getStatus().getCode();

			TreeMap<String, Long> tempserviceMap = statusmap.get(statecode);// new
																			// HashMap<String,Integer>();
			tempserviceMap.put(result.getServicetypecode().toUpperCase(),
					result.getCount());
			tempserviceMap.put("total",
					tempserviceMap.get("total") + result.getCount());
			statusmap.put(statecode, tempserviceMap);
		}

		/*
		 * for (Map.Entry<String, TreeMap<String,Long>> entry :
		 * statusmap.entrySet()) {
		 * 
		 * System.out.println(entry.getKey() + "/" + entry.getValue());
		 * 
		 * }
		 */

	}

	private TreeMap<String, Long> getEmptyServiceCodeWithZeroValue(
			List<ServiceTypeExtn> getservicelist) {
		TreeMap<String, Long> servicemap = new TreeMap<String, Long>();

		for (ServiceTypeExtn service : getservicelist) {
			servicemap.put(service.getCode(), (long) 0);

		}
		servicemap.put("total", 0L);
		return servicemap;
	}

	public String getUsertName(Integer id) {
		String owner = bpaCommonExtnService.getUsertName(id);
		return owner;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EgwStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<EgwStatus> statusList) {
		this.statusList = statusList;
	}

	public Date getApplicationFromDate() {
		return applicationFromDate;
	}

	public void setApplicationFromDate(Date applicationFromDate) {
		this.applicationFromDate = applicationFromDate;
	}

	public Date getApplicationToDate() {
		return applicationToDate;
	}

	public void setApplicationToDate(Date applicationToDate) {
		this.applicationToDate = applicationToDate;
	}

	public String getStatecode() {
		return statecode;
	}

	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}

	public Long getStateid() {
		return stateid;
	}

	public void setStateid(Long stateid) {
		this.stateid = stateid;
	}

	public void setServcode(Long servcode) {
		this.servcode = servcode;
	}

	public Long getServcode() {
		return servcode;
	}

	public void setEgwStatus(Integer egwStatus) {
		this.egwStatus = egwStatus;
	}

	public Integer getEgwStatus() {
		return egwStatus;
	}

	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}

	public Long getServiceType() {
		return serviceType;
	}

	public List<ServiceTypeExtn> getServicelist() {
		return servicelist;
	}

	public void setServicelist(List<ServiceTypeExtn> servicelist) {
		this.servicelist = servicelist;
	}

	public List<RegistrationExtn> getRegList() {
		return regList;
	}

	public void setRegList(List<RegistrationExtn> regList) {
		this.regList = regList;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(
			BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}

	public Integer getAdminboundaryid() {
		return adminboundaryid;
	}

	public void setAdminboundaryid(Integer adminboundaryid) {
		this.adminboundaryid = adminboundaryid;
	}

	public Integer getLocboundaryid() {
		return locboundaryid;
	}

	public void setLocboundaryid(Integer locboundaryid) {
		this.locboundaryid = locboundaryid;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public Map<String, TreeMap<String, Long>> getStatusmap() {
		return statusmap;
	}

	public void setStatusmap(Map<String, TreeMap<String, Long>> statusmap) {
		this.statusmap = statusmap;
	}

	public String getAppMode() {
		return appMode;
	}

	public void setAppMode(String appMode) {
		this.appMode = appMode;
	}

}
