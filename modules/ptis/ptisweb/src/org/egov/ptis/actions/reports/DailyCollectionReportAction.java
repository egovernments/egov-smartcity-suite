package org.egov.ptis.actions.reports;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_ARREARTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODEMAP_FOR_CURRENTTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_TAXREBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REPORT_TEMPLATENAME_DAILY_COLLECTION;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.erpcollection.models.ReceiptDetail;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.model.instrument.InstrumentHeader;
import org.egov.ptis.bean.CollectionInfo;
import org.egov.ptis.bean.ReceiptInfo;
import org.egov.ptis.bean.TaxCollectionInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * 
 * @author subhash
 *
 */

public class DailyCollectionReportAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private String REPORT = "report";
	private String CURRENT = "Current";
	private String ARREAR = "Arrears";
	private String REBATE = "Rebate";
	BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
	private ReportService reportService;
	private Integer reportId;
	ReceiptInfo totalRcptInfo = new ReceiptInfo();
	TaxCollectionInfo arrTotalCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo currTotalCollInfo = new TaxCollectionInfo();
	TaxCollectionInfo rebateTotalCollInfo = new TaxCollectionInfo();
	BigDecimal totalCashCollAmt = ZERO;
	BigDecimal totalChequeCollAmt = ZERO;
	BigDecimal totalOthersCollAmt = ZERO;
	private Date fromDate;
	private Date toDate;
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
	PropertyTaxUtil propTaxUtil = new PropertyTaxUtil();
	Boolean searchForm = Boolean.TRUE;
	String currInst = null;
	private String userId;
	@Override
	public Object getModel() {
		return null;
	}
	
	public void prepare() {
		Query qry = persistenceService
				.getSession()
				.createQuery(
						"select distinct UI FROM UserImpl UI left join UI.userRoles ur left join ur.role r " +
						"where r.roleName = :roleName AND UI.isActive=1 AND ur.isHistory='N' order by UI.userName");
		qry.setParameter("roleName", NMCPTISConstants.ROLE_OPERATOR);
		List<UserImpl> userList = qry.list();
		addDropdownData("userList", userList);
	}
	
	@SkipValidation
	public String newForm() {
		return NEW;
	}
	
	public void validate() {
		if (fromDate == null || fromDate.equals("")) {
			addActionError(getText("mandatory.fromdate"));
		}
		if (toDate == null || toDate.equals("")) {
			addActionError(getText("mandatory.todate"));
		}
		if (userId == null || userId.equals("-1") || userId.equals("")) {
			addActionError(getText("mandatory.todate"));
		}
	}
	
	@SuppressWarnings("unchecked")
	@ValidationErrorPage("new")
	public String generateReport(){
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		InstallmentDao instalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		UserDAO userDao = new UserDAO();
		currInst = instalDao.getInsatllmentByModuleForGivenDate(module, new Date()).getDescription();
		StringBuffer qryString = new StringBuffer("SELECT CH.* FROM EGCL_COLLECTIONHEADER CH "
				+ "INNER JOIN EG_SERVICEDETAILS SD ON CH.ID_SERVICE = SD.ID "
				+ "INNER JOIN EGCL_COLLECTIONINSTRUMENT CI ON CH.ID = CI.COLLECTIONHEADERID "
				+ "INNER JOIN EGF_INSTRUMENTHEADER IH ON CI.INSTRUMENTMASTERID = IH.ID "
				+ "INNER JOIN EGW_STATUS STATUS ON IH.ID_STATUS = STATUS.ID "
				+ "WHERE STATUS.DESCRIPTION != 'Cancelled' AND SD.SERVICENAME = 'Property Tax' " 
				+ "AND ch.created_by = :userId "
				+ "AND to_date(to_char(ch.created_date,'dd/MM/yy'),'dd/MM/yy') >= to_date(:fromDate,'dd/MM/yy') "
				+ "AND to_date(to_char(ch.created_date,'dd/MM/yy'),'dd/MM/yy') <= to_date(:toDate,'dd/MM/yy')");
		SQLQuery qry = persistenceService.getSession().createSQLQuery(qryString.toString())
				.addEntity(ReceiptHeader.class);
		qry.setParameter("fromDate", dateFormat.format(fromDate));
		qry.setParameter("toDate", dateFormat.format(toDate));
		qry.setParameter("userId", userId);
		List<ReceiptHeader> rcptHeaderList = qry.list();
		if (rcptHeaderList != null && !rcptHeaderList.isEmpty()) {
			List<ReceiptInfo> rcptInfoList = new ArrayList<ReceiptInfo>();
			CollectionInfo cashCollInfo = new CollectionInfo();
			BasicProperty basicProperty = null;
			initializeTotalsInfo();
			for (ReceiptHeader rcptHeader : rcptHeaderList) {
				ReceiptInfo rcptInfo = new ReceiptInfo();
				rcptInfo.setReceiptNo(rcptHeader.getReceiptnumber());
				String indexNo = StringUtils.trim(rcptHeader.getConsumerCode().contains("(") ? rcptHeader
						.getConsumerCode().substring(0, rcptHeader.getConsumerCode().indexOf('(')) : rcptHeader
						.getConsumerCode());
				rcptInfo.setIndexNo(indexNo);
				basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(indexNo);
				if (basicProperty != null) {
					rcptInfo.setWardNo(basicProperty.getPropertyID().getWard().getBoundaryNum().toString());
					rcptInfo.setHouseNo(basicProperty.getAddress().getHouseNo());
				}
				for (InstrumentHeader instrumentHead : rcptHeader.getReceiptInstrument()) {
					rcptInfo.setChequeNo(instrumentHead.getInstrumentNumber());
					rcptInfo.setChequeDate(instrumentHead.getInstrumentDate());
					rcptInfo.setBankName(instrumentHead.getBankId() != null ? instrumentHead.getBankId().getName()
							: null);
					rcptInfo.setPaymentMode(instrumentHead.getInstrumentType().getType());
				}
				rcptInfo.setPayeeName(rcptHeader.getPaidBy());
				rcptInfo.setCollInfoList(getCollectionInfoList(rcptHeader));
				rcptInfoList.add(rcptInfo);
			}
			List<TaxCollectionInfo> totalsList = new ArrayList<TaxCollectionInfo>();
			totalsList.add(currTotalCollInfo);
			totalsList.add(arrTotalCollInfo);
			totalsList.add(rebateTotalCollInfo);
			totalRcptInfo.setCollInfoList(totalsList);
			totalRcptInfo.setReceiptNo("Total");
			rcptInfoList.add(totalRcptInfo);

			cashCollInfo.setCollByCash(totalCashCollAmt);
			cashCollInfo.setCollByCheque(totalChequeCollAmt);
			cashCollInfo.setOtherColl(totalOthersCollAmt);
			cashCollInfo.setEduEgsArrColl(arrTotalCollInfo.getEduCess().add(arrTotalCollInfo.getEgsCess()));
			cashCollInfo.setEduEgsCurrColl(currTotalCollInfo.getEduCess().add(currTotalCollInfo.getEgsCess()));
			cashCollInfo.setTotalArrColl(arrTotalCollInfo.getTotal());
			cashCollInfo.setTotalCurrColl(currTotalCollInfo.getTotal().subtract(rebateTotalCollInfo.getTotal()));
			cashCollInfo.setGrandTotal(arrTotalCollInfo.getTotal().add(currTotalCollInfo.getTotal()).subtract(rebateTotalCollInfo.getTotal()));
			cashCollInfo.setRcptInfoList(rcptInfoList);
			User user = userDao.getUserByID(Integer.valueOf(userId));
			cashCollInfo.setOperator(user.getUserName());

			ReportRequest reportInput = new ReportRequest(REPORT_TEMPLATENAME_DAILY_COLLECTION, cashCollInfo, null);
			reportInput.setPrintDialogOnOpenReport(true);
			reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportInput), getSession());
			return REPORT;
		} else {
			searchForm = Boolean.FALSE;
			return NEW;
		}
	}

	private List<TaxCollectionInfo> getCollectionInfoList(ReceiptHeader rcptHeader) {
		List<TaxCollectionInfo> collInfoList = new ArrayList<TaxCollectionInfo>();
		Set<ReceiptDetail> rcptDetails = rcptHeader.getReceiptDetails();
		Set<InstrumentHeader> instHeaderList = rcptHeader.getReceiptInstrument();
		String paymentMode = null;
		for (InstrumentHeader instHeader : instHeaderList) {
			paymentMode = instHeader.getInstrumentType().getType();
		}
		TaxCollectionInfo currCollInfo = new TaxCollectionInfo();
		TaxCollectionInfo arrCollInfo = new TaxCollectionInfo();
		TaxCollectionInfo rebateCollInfo = new TaxCollectionInfo();
		currCollInfo.setTaxType(CURRENT);
		arrCollInfo.setTaxType(ARREAR);
		rebateCollInfo.setTaxType(REBATE);
		BigDecimal totalCurrentTax = ZERO;
		BigDecimal totalArrearTax = ZERO;
		BigDecimal totalRebate = ZERO;
		
		for (ReceiptDetail rcptDetail : rcptDetails) {
			String glcode = rcptDetail.getAccounthead().getGlcode();
			if (GLCODE_FOR_PENALTY.equals(glcode)) {
				if(currInst.equals(rcptDetail.getDescription().substring(16, rcptDetail.getDescription().length()))){
					if (currCollInfo.getMiscTax() == null || currCollInfo.getMiscTax().equals(ZERO)) {
						currCollInfo.setMiscTax(rcptDetail.getCramount());
					} else {
						currCollInfo.setMiscTax(currCollInfo.getMiscTax().add(rcptDetail.getCramount()));
					}
					currTotalCollInfo.setMiscTax(currTotalCollInfo.getMiscTax().add(rcptDetail.getCramount()));
					totalCurrentTax = totalCurrentTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO);
				}else {
					if (arrCollInfo.getMiscTax() == null) {
						arrCollInfo.setMiscTax(rcptDetail.getCramount());
					} else {
						arrCollInfo.setMiscTax(arrCollInfo.getMiscTax().add(rcptDetail.getCramount()));
					}
					arrTotalCollInfo.setMiscTax(arrTotalCollInfo.getMiscTax().add(rcptDetail.getCramount()));
					totalArrearTax = totalArrearTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO);
				}
			} else if (GLCODE_FOR_TAXREBATE.equals(glcode)) {
				if (rebateCollInfo.getGeneralTax() == null) {
					rebateCollInfo.setGeneralTax(rcptDetail.getDramount());
				} else {
					rebateCollInfo.setGeneralTax(rebateCollInfo.getGeneralTax().add(
							rcptDetail.getDramount() != null ? rcptDetail.getDramount() : ZERO));
				}
				totalRebate = totalRebate.add(rcptDetail.getDramount() != null ? rcptDetail.getDramount() : ZERO);
				rebateTotalCollInfo.setGeneralTax(rebateTotalCollInfo.getGeneralTax().add(rcptDetail.getDramount()));
			} else if (GLCODEMAP_FOR_ARREARTAX.containsValue(glcode)) {
				if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_TAX).equals(glcode)) {
					if (arrCollInfo.getGeneralTax() == null) {
						arrCollInfo.setGeneralTax(rcptDetail.getCramount());
					} else {
						arrCollInfo.setGeneralTax(arrCollInfo.getGeneralTax().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setGeneralTax(arrTotalCollInfo.getGeneralTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_SEWERAGE_TAX).equals(glcode)) {
					if (arrCollInfo.getSewerageTax() == null) {
						arrCollInfo.setSewerageTax(rcptDetail.getCramount());
					} else {
						arrCollInfo.setSewerageTax(arrCollInfo.getSewerageTax().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setSewerageTax(arrTotalCollInfo.getSewerageTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_LIGHTINGTAX).equals(glcode)) {
					if (arrCollInfo.getLightTax() == null) {
						arrCollInfo.setLightTax(rcptDetail.getCramount());
					} else {
						arrCollInfo.setLightTax(arrCollInfo.getLightTax().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setLightTax(arrTotalCollInfo.getLightTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_GENERAL_WATER_TAX).equals(glcode)) {
					if (arrCollInfo.getWaterTax() == null) {
						arrCollInfo.setWaterTax(rcptDetail.getCramount());
					} else {
						arrCollInfo.setWaterTax(arrCollInfo.getWaterTax().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setWaterTax(arrTotalCollInfo.getWaterTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX).equals(glcode)) {
					if (arrCollInfo.getFireTax() == null) {
						arrCollInfo.setFireTax(rcptDetail.getCramount());
					} else {
						arrCollInfo.setFireTax(arrCollInfo.getFireTax().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setFireTax(arrTotalCollInfo.getFireTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD).equals(glcode)) {
					if (arrCollInfo.getEduCess() == null) {
						arrCollInfo.setEduCess(rcptDetail.getCramount());
					} else {
						arrCollInfo.setEduCess(arrCollInfo.getEduCess().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setEduCess(arrTotalCollInfo.getEduCess().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX).equals(glcode)) {
					if (arrCollInfo.getEgsCess() == null) {
						arrCollInfo.setEgsCess(rcptDetail.getCramount());
					} else {
						arrCollInfo.setEgsCess(arrCollInfo.getEgsCess().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setEgsCess(arrTotalCollInfo.getEgsCess().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_ARREARTAX.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX).equals(glcode)) {
					if (arrCollInfo.getBigBuildingCess() == null) {
						arrCollInfo.setBigBuildingCess(rcptDetail.getCramount());
					} else {
						arrCollInfo.setBigBuildingCess(arrCollInfo.getBigBuildingCess().add(
								rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO));
					}
					arrTotalCollInfo.setBigBuildingCess(arrTotalCollInfo.getBigBuildingCess().add(rcptDetail.getCramount()));
				}
				totalArrearTax = totalArrearTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO);
			} else {
				if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_TAX).equals(glcode)) {
					currCollInfo.setGeneralTax(rcptDetail.getCramount());
					currTotalCollInfo.setGeneralTax(currTotalCollInfo.getGeneralTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_SEWERAGE_TAX).equals(glcode)) {
					currCollInfo.setSewerageTax(rcptDetail.getCramount());
					currTotalCollInfo.setSewerageTax(currTotalCollInfo.getSewerageTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_LIGHTINGTAX).equals(glcode)) {
					currCollInfo.setLightTax(rcptDetail.getCramount());
					currTotalCollInfo.setLightTax(currTotalCollInfo.getLightTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_GENERAL_WATER_TAX).equals(glcode)) {
					currCollInfo.setWaterTax(rcptDetail.getCramount());
					currTotalCollInfo.setWaterTax(currTotalCollInfo.getWaterTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX).equals(glcode)) {
					currCollInfo.setFireTax(rcptDetail.getCramount());
					currTotalCollInfo.setFireTax(currTotalCollInfo.getFireTax().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD).equals(glcode)) {
					currCollInfo.setEduCess(rcptDetail.getCramount());
					currTotalCollInfo.setEduCess(currTotalCollInfo.getEduCess().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX).equals(glcode)) {
					currCollInfo.setEgsCess(rcptDetail.getCramount());
					currTotalCollInfo.setEgsCess(currTotalCollInfo.getEgsCess().add(rcptDetail.getCramount()));
				} else if (GLCODEMAP_FOR_CURRENTTAX.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX).equals(glcode)) {
					currCollInfo.setBigBuildingCess(rcptDetail.getCramount());
					currTotalCollInfo.setBigBuildingCess(currTotalCollInfo.getBigBuildingCess().add(rcptDetail.getCramount()));
				}
				totalCurrentTax = totalCurrentTax.add(rcptDetail.getCramount() != null ? rcptDetail.getCramount() : ZERO);
			}
		}
		arrTotalCollInfo.setTotal(arrTotalCollInfo.getTotal().add(totalArrearTax));
		currTotalCollInfo.setTotal(currTotalCollInfo.getTotal().add(totalCurrentTax));
		rebateTotalCollInfo.setTotal(rebateTotalCollInfo.getTotal().add(totalRebate));
		if (paymentMode.equals("cash")) {
			totalCashCollAmt = totalCashCollAmt.add(totalCurrentTax.add(totalArrearTax).subtract(totalRebate));
		} else if (paymentMode.equals("cheque")) {
			totalChequeCollAmt = totalChequeCollAmt.add(totalCurrentTax.add(totalArrearTax).subtract(totalRebate));
		} else {
			totalOthersCollAmt = totalOthersCollAmt.add(totalCurrentTax.add(totalArrearTax).subtract(totalRebate));
		}
		arrCollInfo.setTotal(totalArrearTax);
		currCollInfo.setTotal(totalCurrentTax);
		rebateCollInfo.setTotal(totalRebate);
		collInfoList.add(currCollInfo);
		collInfoList.add(arrCollInfo);
		collInfoList.add(rebateCollInfo);
		return collInfoList;
	}

	private void initializeTotalsInfo() {
		arrTotalCollInfo.setGeneralTax(ZERO);
		arrTotalCollInfo.setWaterTax(ZERO);
		arrTotalCollInfo.setBigBuildingCess(ZERO);
		arrTotalCollInfo.setEduCess(ZERO);
		arrTotalCollInfo.setEgsCess(ZERO);
		arrTotalCollInfo.setFireTax(ZERO);
		arrTotalCollInfo.setLightTax(ZERO);
		arrTotalCollInfo.setMiscTax(ZERO);
		arrTotalCollInfo.setSewerageTax(ZERO);
		arrTotalCollInfo.setTotal(ZERO);
		arrTotalCollInfo.setTaxType(ARREAR);
		
		currTotalCollInfo.setGeneralTax(ZERO);
		currTotalCollInfo.setWaterTax(ZERO);
		currTotalCollInfo.setBigBuildingCess(ZERO);
		currTotalCollInfo.setEduCess(ZERO);
		currTotalCollInfo.setEgsCess(ZERO);
		currTotalCollInfo.setFireTax(ZERO);
		currTotalCollInfo.setLightTax(ZERO);
		currTotalCollInfo.setMiscTax(ZERO);
		currTotalCollInfo.setSewerageTax(ZERO);
		currTotalCollInfo.setTotal(ZERO);
		currTotalCollInfo.setTaxType(CURRENT);
		
		rebateTotalCollInfo.setGeneralTax(ZERO);
		rebateTotalCollInfo.setTotal(ZERO);
		rebateTotalCollInfo.setTaxType(REBATE);
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Boolean getSearchForm() {
		return searchForm;
	}

	public void setSearchForm(Boolean searchForm) {
		this.searchForm = searchForm;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
