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
import org.egov.bpa.models.extd.ZoneReportResultExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.utils.ApplicationMode;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.web.struts.actions.BaseFormAction;
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
@SuppressWarnings("serial")
public class StatusByZoneReportExtnAction extends BaseFormAction {

	@PersistenceContext
	private EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public static final String getWardByZone = "getWardByZone";
	public static final String showdetail = "showdetail";
	private String zone;
	private Date applicationFromDate;
	private Date applicationToDate;
	private Long serviceType;
	private Integer egwStatus;
	private String searchMode;
	private String appMode;
	private Integer bndryId;
	private Integer adminboundaryid;
	private Integer locboundaryid;
	private String status;

	private String zonecode;
	private String wardcode;
	private String statecode;
	private BpaCommonExtnService bpaCommonExtnService;
	// private Boundary bndry=new Boundary();
	private List<EgwStatus> statusList = new ArrayList(0);
	private List<Boundary> zoneList = new ArrayList(0);
	private List<Boundary> wardList = new ArrayList(0);
	private Map<String, TreeMap<String, Long>> appstatusmap = new TreeMap<String, TreeMap<String, Long>>();
	private List<RegistrationExtn> regList = new ArrayList();
	StringBuffer qryStr = new StringBuffer();

	public StatusByZoneReportExtnAction() {

	}
	@Action(value = "/statusByZoneReportExtn-newForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newForm() {

		return NEW;
	}

	public void prepare() {
		super.prepare();
		addDropdownData("serviceTypeList",
				persistenceService
						.findAllBy("from ServiceTypeExtn order by code"));
		addDropdownData("statusList", persistenceService.findAllBy(
				"from EgwStatus where moduletype=? order by code ",
				BpaConstants.NEWBPAREGISTRATIONMODULE));
		addDropdownData(
				"adminboundaryList",
				persistenceService
						.findAllBy(
								"from Boundary bndry where bndry.boundaryType.id in(select id from BoundaryTypeImpl where name=?) ",
								BpaConstants.ZONE_BNDRY_TYPE));
		addDropdownData("applicationModeList",
				Arrays.asList(ApplicationMode.values()));
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	// @ValidationErrorPage(NEW)
	@Action(value = "/statusByZoneReportExtn-reportResult", results = { @Result(name = NEW,type = "dispatcher") })
	public String reportResult() {
		search();
		setSearchMode("result");

		return NEW;
	}
	@Action(value = "/statusByZoneReportExtn-showdetail", results = { @Result(name = showdetail,type = "dispatcher") })
	public String showdetail() {
		if (getWardcode() != null && "total".equals(getWardcode())) {

			qryStr.append("select reg from RegistrationExtn reg,Boundary bndry where reg.adminboundaryid=bndry.id  and bndry.parent.id in (select id from Boundary where name=:zonecode) and reg.egwStatus.code= :statcode ");
		} else
			qryStr.append(" from RegistrationExtn reg where id is not null and reg.adminboundaryid.name= :wardcode and reg.egwStatus.code= :statcode ");
		getappendquery();
		qryStr.append(" order by reg.id");

		Query query = getCurrentSession().createQuery(qryStr.toString());
		if (getWardcode() != null && "total".equals(getWardcode())) {
			query.setString("zonecode", getZonecode() != null ? getZonecode()
					: getZonecode());

		} else if (getWardcode() != null && !"total".equals(getWardcode())) {
			query.setString("wardcode", getWardcode() != null ? getWardcode()
					: getWardcode());
		}
		query.setString("statcode", getStatecode());
		if (getStatecode() == null) {
			query.setInteger("statcode", getEgwStatus());
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
		/*
		 * if(getLocboundaryid()!=null && getLocboundaryid()!=-1){
		 * query.setInteger("locid", getLocboundaryid());
		 * 
		 * }
		 */
		if (getAppMode() != null && !"-1".equals(getAppMode())) {
			query.setString("appmodename", getAppMode());
		}
		regList = query.list();
		// regList=persistenceService.findAllBy("from Registration where adminboundaryid.name=? and egwStatus.code=? order by id",getWardcode(),getStatecode());
		return showdetail;
	}

	public String getWardByZone() {
		if (getStatecode() != null) {

			statusList = persistenceService
					.findAllBy(
							"from EgwStatus where moduletype=? and code=? order by code",
							BpaConstants.NEWBPAREGISTRATIONMODULE,
							getStatecode());
		} else
			statusList = persistenceService.findAllBy(
					"from EgwStatus where moduletype=? order by code",
					BpaConstants.NEWBPAREGISTRATIONMODULE);

		wardList = persistenceService
				.findAllBy(
						"from Boundary where parent.id in (select id from Boundary where name=? ) ",
						getZonecode());
		for (EgwStatus status : statusList) {
			TreeMap<String, Long> wardmap = getEmptywardWithZeroValue(wardList);
			appstatusmap.put(status.getCode(), wardmap);
		}
		if (getStatecode() != null) {
			qryStr.append("SELECT DISTINCT count(reg.id) as count,bndry.name as wardName, reg.egwStatus.code as statusName from RegistrationExtn reg,Boundary bndry  where reg.adminboundaryid=bndry.id AND  bndry.boundaryType.id in (select id from BoundaryTypeImpl where name= :name) AND bndry.parent.id in (select id from Boundary where name=:zonenamecode and reg.egwStatus.code= :status)");

		} else
			qryStr.append("SELECT DISTINCT count(reg.id) as count,bndry.name as wardName, reg.egwStatus.code as statusName from RegistrationExtn reg,Boundary bndry  where reg.adminboundaryid=bndry.id AND  bndry.boundaryType.id in (select id from BoundaryTypeImpl where name= :name) AND bndry.parent.id in (select id from Boundary where name=:zonenamecode)");
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

		if (getApplicationFromDate() != null) {
			qryStr.append(" and reg.planSubmissionDate>= :fromdate ");
		}
		if (getApplicationToDate() != null) {
			qryStr.append(" and reg.planSubmissionDate<= :todate ");
		}
		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			Boundary boundary = (Boundary) persistenceService.find(
					"from Boundary where id=?", getAdminboundaryid());

			if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Ward")) {

				qryStr.append(" and reg.adminboundaryid.id= :adminid");
			} else if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Zone")) {
				qryStr.append("and reg.adminboundaryid.id in (select id from Boundary where parent.id= :adminid )");

			}
		}
		if (getAppMode() != null && !"-1".equals(getAppMode())) {
			qryStr.append(" and reg.appMode= :appmodename");
		}

		qryStr.append(" GROUP BY  bndry.name , reg.egwStatus.code");
		Query query = getCurrentSession().createQuery(qryStr.toString());
		query.setString("zonenamecode", getZonecode());
		if (getStatecode() != null) {
			query.setString("status", getStatecode());
		}
		query.setString("name", BpaConstants.WARD_BNDRY_TYPE);

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
		/*
		 * if(getLocboundaryid()!=null && getLocboundaryid()!=-1){
		 * query.setInteger("locid", getLocboundaryid());
		 * 
		 * }
		 */
		if (getAppMode() != null && !"-1".equals(getAppMode())) {
			query.setString("appmodename", getAppMode());
		}
		query.setResultTransformer(Transformers
				.aliasToBean(ZoneReportResultExtn.class));

		List<ZoneReportResultExtn> wardpositionList = query.list();
		for (ZoneReportResultExtn result : wardpositionList) {
			status = result.getStatusName();
			TreeMap<String, Long> tempstatusmap = appstatusmap.get(status);
			tempstatusmap.put(result.getWardName().toUpperCase(),
					result.getCount());
			tempstatusmap.put("total",
					tempstatusmap.get("total") + result.getCount());
			appstatusmap.put(status, tempstatusmap);
		}

		setSearchMode("result");
		return getWardByZone;
	}

	public void getappendquery() {

		if (getEgwStatus() != null && getEgwStatus() != -1
				&& (getStatecode() == null || "".equals(getStatecode()))) {
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

		if (getApplicationFromDate() != null) {
			qryStr.append(" and reg.planSubmissionDate>= :fromdate ");
		}
		if (getApplicationToDate() != null) {
			qryStr.append(" and reg.planSubmissionDate<= :todate ");
		}

		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			Boundary boundary = (Boundary) persistenceService.find(
					"from Boundary where id=?", getAdminboundaryid());

			if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Ward")) {

				qryStr.append(" and reg.adminboundaryid.id= :adminid");
			} else if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Zone")) {
				qryStr.append("and reg.adminboundaryid.id in (select id from Boundary where parent.id= :adminid )");

			}
		}

		if (getAppMode() != null && !"-1".equals(getAppMode())) {
			qryStr.append(" and reg.appMode= :appmodename");
		}

		/*
		 * else if (getAdminboundaryid()!=null) {
		 * qryStr.append(" or reg.adminboundaryid.id= :adminid "); }
		 * 
		 * if(getLocboundaryid()!=null) {
		 * qryStr.append(" or reg.locboundaryid.id= :locid "); }
		 * 
		 * if(getLocboundaryid()!=null){ Boundary
		 * boundaryLoc=(Boundary)persistenceService
		 * .find("from Boundary where id=?",getLocboundaryid());
		 * 
		 * if(boundaryLoc!=null &&
		 * boundaryLoc.getBoundaryType().getName().equals("Street")) {
		 * qryStr.append(" and reg.locboundaryid.id= :locid "); }
		 * 
		 * else if(boundaryLoc!=null &&
		 * boundaryLoc.getBoundaryType().getName().equals("Locality") ) {
		 * qryStr.append(
		 * "	and (reg.locboundaryid.id in (select id from Boundary where parent.id= :locid) OR  reg.locboundaryid.id= :locid ) "
		 * ); } else if(boundaryLoc!=null &&
		 * boundaryLoc.getBoundaryType().getName().equals("Area")) {
		 * qryStr.append(
		 * " and (reg.locboundaryid.id in (select id from Boundary where parent.id= :locid OR parent.id in (select id from Boundary where parent.id= :locid) ) OR reg.locboundaryid.id= :locid) "
		 * );
		 * 
		 * } }
		 */
	}

	@SuppressWarnings("unchecked")
	public void search() {
		if (getEgwStatus() != -1) {
			statusList = persistenceService
					.findAllBy(
							"from EgwStatus where moduletype=? and id=? order by orderId",
							BpaConstants.NEWBPAREGISTRATIONMODULE,
							getEgwStatus());
		} else
			statusList = persistenceService.findAllBy(
					"from EgwStatus where moduletype=? order by orderId ",
					BpaConstants.NEWBPAREGISTRATIONMODULE);

		// persistenceService.findAllBy("from EgwStatus where moduletype=? order by orderId",BpaConstants.BPAREGISTRATIONMODULE);
		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			zoneList = persistenceService
					.findAllBy(
							"from Boundary bndry where bndry.boundaryType.id in(select id from BoundaryTypeImpl where name=?) and bndry.id=? ",
							BpaConstants.ZONE_BNDRY_TYPE, getAdminboundaryid());
		} else {
			zoneList = persistenceService
					.findAllBy(
							"from Boundary bndry where bndry.boundaryType.id in(select id from BoundaryTypeImpl where name=?) ",
							BpaConstants.ZONE_BNDRY_TYPE);
		}
		// zoneList=
		// persistenceService.findAllBy("from Boundary bndry where bndry.boundaryType.id=?",2);

		for (EgwStatus status : statusList) {
			TreeMap<String, Long> zonemap = getEmptyzoneWithZeroValue(zoneList);
			appstatusmap.put(status.getCode(), zonemap);
		}

		// qryStr.append("SELECT DISTINCT bndry.parent.name AS zoneName, COUNT(reg.id) AS count , reg.egwStatus.code AS statusName FROM Registration reg, Boundary bndry WHERE  reg.adminboundaryid=bndry.id AND bndry.boundaryType.id IN (2,3) ");
		qryStr.append("SELECT DISTINCT bndry.parent.name AS zoneName, COUNT(reg.id) AS count , reg.egwStatus.code AS statusName FROM RegistrationExtn reg, Boundary bndry WHERE  reg.adminboundaryid=bndry.id AND bndry.boundaryType.id IN (select id from BoundaryTypeImpl where name= :zonename or name= :name) ");
		getappendquery();
		qryStr.append(" GROUP BY  bndry.parent.name , reg.egwStatus.code");
		Query query = getCurrentSession().createQuery(qryStr.toString());
		query.setString("zonename", BpaConstants.ZONE_BNDRY_TYPE);
		query.setString("name", BpaConstants.WARD_BNDRY_TYPE);

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
		if (getZonecode() == null) {
			if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
				query.setInteger("adminid", getAdminboundaryid());
			}
		}
		if (getAppMode() != null && !"-1".equals(getAppMode())) {
			query.setString("appmodename", getAppMode());
		}
		/*
		 * if(getLocboundaryid()!=null){ query.setInteger("locid",
		 * getLocboundaryid()); }
		 */

		query.setResultTransformer(Transformers
				.aliasToBean(ZoneReportResultExtn.class));
		List<ZoneReportResultExtn> statusResult = (List<ZoneReportResultExtn>) query
				.list();

		for (ZoneReportResultExtn result : statusResult) {
			status = result.getStatusName();
			TreeMap<String, Long> tempstatusmap = appstatusmap.get(status); // new
																			// TreeMap<String,Long>();
			tempstatusmap.put(result.getZoneName().toUpperCase(),
					result.getCount());
			tempstatusmap.put("total",
					tempstatusmap.get("total") + result.getCount());
			appstatusmap.put(status, tempstatusmap);
		}

	}

	private TreeMap<String, Long> getEmptyzoneWithZeroValue(
			List<Boundary> getZoneByZeroList) {
		TreeMap<String, Long> zonemap = new TreeMap<String, Long>();

		for (Boundary service : getZoneByZeroList) {
			zonemap.put(service.getName().toUpperCase(), (long) 0);
		}
		zonemap.put("total", 0L);
		return zonemap;
	}

	private TreeMap<String, Long> getEmptywardWithZeroValue(
			List<Boundary> getWardByZeroList) {
		TreeMap<String, Long> wardmap = new TreeMap<String, Long>();

		for (Boundary service : getWardByZeroList) {
			wardmap.put(service.getName().toUpperCase(), (long) 0);
		}
		wardmap.put("total", 0L);
		return wardmap;
	}

	public String getUsertName(Integer id) {
		String owner = bpaCommonExtnService.getUsertName(id);
		return owner;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
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

	public Long getServiceType() {
		return serviceType;
	}

	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getEgwStatus() {
		return egwStatus;
	}

	public void setEgwStatus(Integer egwStatus) {
		this.egwStatus = egwStatus;
	}

	public List<EgwStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<EgwStatus> statusList) {
		this.statusList = statusList;
	}

	public Integer getBndryId() {
		return bndryId;
	}

	public void setBndryId(Integer bndryId) {
		this.bndryId = bndryId;
	}

	public String getZonecode() {
		return zonecode;
	}

	public void setZonecode(String zonecode) {
		this.zonecode = zonecode;
	}

	public String getStatecode() {
		return statecode;
	}

	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}

	public Map<String, TreeMap<String, Long>> getAppstatusmap() {
		return appstatusmap;
	}

	public void setAppstatusmap(Map<String, TreeMap<String, Long>> appstatusmap) {
		this.appstatusmap = appstatusmap;
	}

	public String getWardcode() {
		return wardcode;
	}

	public void setWardcode(String wardcode) {
		this.wardcode = wardcode;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public String getAppMode() {
		return appMode;
	}

	public void setAppMode(String appMode) {
		this.appMode = appMode;
	}

	public List<Boundary> getZoneList() {
		return zoneList;
	}

	public void setZoneList(List<Boundary> zoneList) {
		this.zoneList = zoneList;
	}

	public List<Boundary> getWardList() {
		return wardList;
	}

	public void setWardList(List<Boundary> wardList) {
		this.wardList = wardList;
	}

	/*
	 * public Boundary getAdminboundaryid() { return adminboundaryid; }
	 * 
	 * 
	 * public void setAdminboundaryid(Boundary adminboundaryid) {
	 * this.adminboundaryid = adminboundaryid; }
	 * 
	 * 
	 * public Boundary getLocboundaryid() { return locboundaryid; }
	 * 
	 * 
	 * public void setLocboundaryid(Boundary locboundaryid) { this.locboundaryid
	 * = locboundaryid; }
	 */

}
