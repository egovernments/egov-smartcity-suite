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
import org.egov.bpa.models.extd.FeesGroupEnum;
import org.egov.bpa.models.extd.FeesReportResultExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.bpa.services.extd.report.BpaReportExtnService;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.models.ReceiptInstrumentInfo;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("egov")
@SuppressWarnings("serial")
public class DDDetailsForFeeGroupReportAction extends BaseFormAction {
	@PersistenceContext
	private EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	private Long serviceType;

	private Long adminboundaryid;
	private Long locboundaryid;
	private String WARD = "Ward";
	private Date orderissuedFromDate;
	private String planSubmissionNumber;
	private Date orderissuedToDate;
	private String feesGroup;
	private String searchMode;
	private BpaReportExtnService bpaReportExtnService;
	private FeeExtnService feeExtnService;
	private List<Long> serviceTypeList = new ArrayList<Long>();
	private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
	private BpaCommonExtnService bpaCommonExtnService;
	private List<FeesReportResultExtn> regResultList = new ArrayList<FeesReportResultExtn>(
			0);
	private Map<String, LinkedHashMap<String, String>> FeeDetailmap = new LinkedHashMap<String, LinkedHashMap<String, String>>();

	public void prepare() {
		List<ServiceTypeExtn> servList = persistenceService.findAllBy(
				"from ServiceTypeExtn where code NOT IN (?) order by code",
				BpaConstants.RECLASSIFICATION);
		addDropdownData("serviceTypeDropDownList", servList);
		addDropdownData("feeGroupList",
				persistenceService
						.findAllBy("from BpaFeeExtn order by feeCode"));
		addDropdownData("feesGroupList", Arrays.asList(FeesGroupEnum.values()));
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Action(value = "/dDDetailsForFeeGroupReport-newform", results = { @Result(name = NEW,type = "dispatcher") })
	public String newform() {
		return NEW;
	}
	@Action(value = "/dDDetailsForFeeGroupReport-searchResults", results = { @Result(name = NEW,type = "dispatcher") })
	public String searchResults() {
		search();
		setSearchMode("result");
		return NEW;
	}

	/*
	 * To get Application records whose Order Issued To Applicant DAte Update is
	 * Done....
	 */
	@SuppressWarnings("unchecked")
	public void search() {
		StringBuffer qryStr = new StringBuffer();
		List<RegistrationExtn> regList = new ArrayList<RegistrationExtn>();

		if (getServiceTypeList() != null && getServiceTypeList().size() > 0) {
			regList = persistenceService
					.findAllBy("from RegistrationExtn  where egwStatus.code='Order Issued to Applicant' and  serviceType.id in "
							+ getServiceTypeList().toString().replace('[', '(')
									.replace(']', ')'));

		} else {
			regList = persistenceService
					.findAllBy(
							"from RegistrationExtn where egwStatus.code in(?) and serviceType.id NOT IN (select id from ServiceTypeExtn where code IN (?)) order by id",
							BpaConstants.ORDERISSUEDTOAPPLICANT,
							BpaConstants.RECLASSIFICATION);

		}
		List<BpaFeeExtn> regFeelist = persistenceService
				.findAllBy(
						"SELECT DISTINCT feeDescription FROM BpaFeeExtn where serviceType.code NOT IN (?) order by feeDescription",
						BpaConstants.RECLASSIFICATION);
		Object[] objListToStringArray = regFeelist.toArray();
		for (RegistrationExtn regobj : regList) {
			LinkedHashMap<String, String> regObjwithZeroValue = getEmptyFeeDetailCodeWithZeroValue(objListToStringArray);
			FeeDetailmap
					.put(regobj.getPlanSubmissionNum(), regObjwithZeroValue);

		}

		String dateqry = "";
		qryStr.append("SELECT fees AS feeObj,reg as registration,reg.planSubmissionNum as psnNum,feeDet as feeDetailObj,feeDet.amount as amount,feeDet.bpaFee.feeDescription as bpafeecode FROM RegistrationFeeExtn fees,RegistrationFeeDetailExtn  feeDet,RegistrationExtn reg ,BpaFeeExtn bpafee ");
		/*
		 * joining RegnStatusDetails table only if user selects permit order
		 * issued FromDate and Todate Or Anyone of these.
		 */
		qryStr.append(",RegnStatusDetailsExtn regstatus ");
		dateqry = " and reg.id=regstatus.registration.id and regstatus.status.code= :regstatuscode";
		if ((getOrderissuedFromDate() != null && !""
				.equals(getOrderissuedFromDate()))
				|| (getOrderissuedToDate() != null && !""
						.equals(getOrderissuedToDate()))) {

			if (getOrderissuedFromDate() != null
					&& !"".equals(getOrderissuedFromDate())) {
				dateqry = dateqry
						+ " and regstatus.statusdate>= :issuedfromdate ";
			}
			if (getOrderissuedToDate() != null
					&& !"".equals(getOrderissuedToDate())) {
				dateqry = dateqry
						+ " and regstatus.statusdate<= :issuedtodate ";
			}
		}
		qryStr.append(" where reg.id=fees.registration.id  and fees.id=feeDet.registrationFee.id and feeDet.bpaFee.id=bpafee.id");
		/*
		 * if(getPlanSubmissionNumber()!=null &&
		 * !getPlanSubmissionNumber().equals("")) {
		 * qryStr.append(" and reg.planSubmissionNum= :planNumber"); }
		 */
		if (getServiceTypeList() != null && getServiceTypeList().size() > 0) {
			qryStr.append(" and reg.serviceType.id in (:serviceid ) ");
		}
		if (getFeesGroup() != null && !"-1".equals(getFeesGroup())) {
			qryStr.append(" and bpafee.feeGroup= :feegroup");
		}

		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			Boundary boundary = bpaCommonExtnService
					.getBoundaryObjById(adminboundaryid);

			if (boundary != null
					&& boundary.getBoundaryType().getName().equals(WARD)) {

				qryStr.append(" and reg.adminboundaryid.id= :adminid");
			} else if (boundary != null
					&& boundary.getBoundaryType().getName().equals("Zone")) {
				qryStr.append(" and reg.adminboundaryid.id in (select id from BoundaryImpl where parent.id= :adminid )");

			}
		}
		if (getOrderissuedFromDate() != null
				&& !"".equals(getOrderissuedFromDate())) {
			qryStr.append(" and regstatus.statusdate>= :issuedfromdate ");
		}
		if (getOrderissuedToDate() != null
				&& !"".equals(getOrderissuedToDate())) {
			qryStr.append(" and regstatus.statusdate<= :issuedtodate ");
		}
		if (dateqry != "") {
			qryStr.append(dateqry);
			qryStr.append(" order by regstatus.statusdate desc");
		} else {
			qryStr.append(" order by reg.id ");
		}
		Query query = getCurrentSession().createQuery(qryStr.toString());
		query.setString("regstatuscode", BpaConstants.ORDERISSUEDTOAPPLICANT);
		if (getServiceTypeList() != null && getServiceTypeList().size() > 0) {
			query.setParameterList("serviceid", getServiceTypeList());
		}
		/*
		 * if(getPlanSubmissionNumber()!=null &&
		 * !getPlanSubmissionNumber().equals("")) {
		 * query.setString("planNumber",getPlanSubmissionNumber()); }
		 */
		if (getFeesGroup() != null && !"-1".equals(getFeesGroup())) {
			query.setString("feegroup", getFeesGroup());
		}

		if (getAdminboundaryid() != null && getAdminboundaryid() != -1) {
			query.setLong("adminid", getAdminboundaryid());
		}
		if ((getOrderissuedFromDate() != null && !""
				.equals(getOrderissuedFromDate()))
				|| (getOrderissuedToDate() != null && !""
						.equals(getOrderissuedToDate()))) {

			if (getOrderissuedFromDate() != null
					&& !"".equals(getOrderissuedFromDate())) {
				query.setDate("issuedfromdate", getOrderissuedFromDate());
			}
			if (getOrderissuedToDate() != null
					&& !"".equals(getOrderissuedToDate())) {
				query.setDate("issuedtodate", getOrderissuedToDate());
			}
		}

		query.setResultTransformer(Transformers
				.aliasToBean(FeesReportResultExtn.class));
		regResultList = (List<FeesReportResultExtn>) query.list();
		for (FeesReportResultExtn fedetTempObj : regResultList) {
			String psnNumber = fedetTempObj.getPsnNum();
			LinkedHashMap<String, String> tempserviceMap = FeeDetailmap
					.get(psnNumber);
			if (tempserviceMap != null && !tempserviceMap.isEmpty()) {

				if ((tempserviceMap.get("DD Number").isEmpty() || tempserviceMap
						.get("DD Number") == null)
						&& tempserviceMap.get("DD Amount").isEmpty()
						&& tempserviceMap.get("Site Address").isEmpty()) {
					ReceiptInstrumentInfo instrumentObj = getReceiptinstrumentInfo(fedetTempObj
							.getRegistration());
					if (instrumentObj != null
							&& instrumentObj.getInstrumentType().equals("dd")) {
						tempserviceMap.put("DD Number",
								instrumentObj.getInstrumentNumber());
						tempserviceMap
								.put("DD Date", getIssuedDate(instrumentObj
										.getInstrumentDate()));
						tempserviceMap.put("DD Amount", instrumentObj
								.getInstrumentAmount().toString());
						tempserviceMap.put("Bank Name",
								instrumentObj.getBankName());
					}

					tempserviceMap.put("Site Address", fedetTempObj
							.getRegistration().getBpaSiteAddress());
					tempserviceMap.put("Applicant Name", fedetTempObj
							.getRegistration().getOwner().getName());
				}
				tempserviceMap.put(fedetTempObj.getBpafeecode(), fedetTempObj
						.getAmount().toString());
				FeeDetailmap.put(psnNumber, tempserviceMap);
			}
		}

	}

	public String getIssuedDate(Date stringDate) {
		return sdf1.format(stringDate);
	}

	public ReceiptInstrumentInfo getReceiptinstrumentInfo(
			RegistrationExtn registration) {
		List<BillReceiptInfo> billRecptInfoList = new ArrayList<BillReceiptInfo>();
		boolean instrumentDetailsPrepared = false;
		ReceiptInstrumentInfo instrumentObj = null;
		Map<String, BillReceiptInfo> billReceiptInfoMap = bpaReportExtnService
				.getReceiptInfoMap(registration);
		List<BpaFeeExtn> santionFeeList = feeExtnService
				.getAllSanctionedFeesbyServiceTypeSortByOrderNumber(registration
						.getServiceType().getId());
		for (Map.Entry<String, BillReceiptInfo> map : billReceiptInfoMap
				.entrySet()) {
			billRecptInfoList.add(map.getValue());
		}
		for (BillReceiptInfo billeciept : billRecptInfoList) {
			for (ReceiptAccountInfo recAccInfo : billeciept.getAccountDetails()) {
				if (recAccInfo != null
						&& recAccInfo.getCrAmount() != null
						&& recAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0) {
					String demandMasterReasonDesc = "";
					if (recAccInfo.getDescription() != null
							&& !"".equals(recAccInfo.getDescription())) {
						demandMasterReasonDesc = recAccInfo
								.getDescription()
								.substring(
										0,
										recAccInfo
												.getDescription()
												.indexOf(
														BpaConstants.COLL_RECEIPTDETAIL_DESC_PREFIX))
								.trim();
					}
					for (BpaFeeExtn reportFeesDtls : santionFeeList) {
						if (null != reportFeesDtls.getFeeDescription()
								&& !"".equals(reportFeesDtls
										.getFeeDescription())
								&& reportFeesDtls.getFeeDescription()
										.equalsIgnoreCase(
												demandMasterReasonDesc)) {

							for (ReceiptInstrumentInfo instrument : billeciept
									.getInstrumentDetails()) {
								instrumentObj = instrument;
								instrumentDetailsPrepared = true;
							}

							break;

						}
					}
					if (instrumentDetailsPrepared)
						break;
				}
				if (instrumentDetailsPrepared)
					break;
			}
			if (instrumentDetailsPrepared)
				break;
		}
		return instrumentObj;
	}

	private LinkedHashMap<String, String> getEmptyFeeDetailCodeWithZeroValue(
			Object[] getRegistrationFeeDetailList) {
		LinkedHashMap<String, String> servicemap = new LinkedHashMap<String, String>();
		servicemap.put("Applicant Name", "");
		servicemap.put("Site Address", "");
		servicemap.put("DD Number", "");
		servicemap.put("DD Date", "");
		servicemap.put("DD Amount", "");
		servicemap.put("Bank Name", "");
		for (Object service : getRegistrationFeeDetailList) {
			servicemap.put((String) service, BigDecimal.ZERO.toString());
		}
		return servicemap;
	}

	public Long getServiceType() {
		return serviceType;
	}

	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public Long getAdminboundaryid() {
		return adminboundaryid;
	}

	public void setAdminboundaryid(Long adminboundaryid) {
		this.adminboundaryid = adminboundaryid;
	}

	public Long getLocboundaryid() {
		return locboundaryid;
	}

	public void setLocboundaryid(Long locboundaryid) {
		this.locboundaryid = locboundaryid;
	}

	public Date getOrderissuedFromDate() {
		return orderissuedFromDate;
	}

	public void setOrderissuedFromDate(Date orderissuedFromDate) {
		this.orderissuedFromDate = orderissuedFromDate;
	}

	public Date getOrderissuedToDate() {
		return orderissuedToDate;
	}

	public void setOrderissuedToDate(Date orderissuedToDate) {
		this.orderissuedToDate = orderissuedToDate;
	}

	public String getFeesGroup() {
		return feesGroup;
	}

	public void setFeesGroup(String feesGroup) {
		this.feesGroup = feesGroup;
	}

	public List<Long> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<Long> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(
			BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}

	public List<FeesReportResultExtn> getRegResultList() {
		return regResultList;
	}

	public void setRegResultList(List<FeesReportResultExtn> regResultList) {
		this.regResultList = regResultList;
	}

	public BpaReportExtnService getBpaReportExtnService() {
		return bpaReportExtnService;
	}

	public void setBpaReportExtnService(
			BpaReportExtnService bpaReportExtnService) {
		this.bpaReportExtnService = bpaReportExtnService;
	}

	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}

	public void setFeeExtnService(FeeExtnService feeExtnService) {
		this.feeExtnService = feeExtnService;
	}

	public Map<String, LinkedHashMap<String, String>> getFeeDetailmap() {
		return FeeDetailmap;
	}

	public void setFeeDetailmap(
			Map<String, LinkedHashMap<String, String>> feeDetailmap) {
		FeeDetailmap = feeDetailmap;
	}

	public String getPlanSubmissionNumber() {
		return planSubmissionNumber;
	}

	public void setPlanSubmissionNumber(String planSubmissionNumber) {
		this.planSubmissionNumber = planSubmissionNumber;
	}

}
