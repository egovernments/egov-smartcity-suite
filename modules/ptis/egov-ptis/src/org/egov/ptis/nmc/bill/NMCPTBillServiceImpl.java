/**
 * 
 */
package org.egov.ptis.nmc.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.PTISCONFIGFILE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARR_LP_DATE_CONSTANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_REBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_TAXREBATE;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.EgBillDetailsDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.utils.EGovConfig;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;

public class NMCPTBillServiceImpl extends BillServiceInterface {
	private static final Logger LOGGER = Logger.getLogger(NMCPTBillServiceImpl.class);
	PropertyTaxUtil propertyTaxUtil;
	PropertyTaxCollection propertyTaxCollection;
	DateFormat dateFormat = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);

	@Override
	public String getBillXML(Billable billObj) {
		if (billObj == null) {
			throw new EGOVRuntimeException("Exception in getBillXML....Billable is null");
		}
		return super.getBillXML(billObj);
	}

	/**
	 * Setting the EgBillDetails to generate XML as a part of Erpcollection
	 * Integration
	 * 
	 * @see org.egov.demand.interfaces.BillServiceInterface
	 * 
	 */
	public List<EgBillDetails> getBilldetails(Billable billObj) {
		List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();

		LOGGER.debug("Entered method getBilldetails : " + billObj);
		BigDecimal balance = BigDecimal.ZERO;
		EgBillDetails billdetail = null;
		String key = "";
		NMCPropertyTaxBillable nmcBillable = (NMCPropertyTaxBillable) billObj;
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		Ptdemand ptDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(nmcBillable.getBasicProperty().getProperty());
		HashMap<String, Integer> orderMap = propertyTaxUtil
				.generateOrderForDemandDetails(ptDemand.getEgDemandDetails());
		List<EgDemandDetails> pendmdList = new ArrayList<EgDemandDetails>();
		Map<Installment, BigDecimal> instWiseAmtCollMap = propertyTaxUtil.prepareRsnWiseCollForProp(nmcBillable
				.getBasicProperty().getProperty());

		for (EgDemandDetails demandDetail : ptDemand.getEgDemandDetails()) {
			balance = BigDecimal.ZERO;
			key = "";
			balance = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

			if (balance.compareTo(BigDecimal.ZERO) == 1) {
				EgDemandReason reason = demandDetail.getEgDemandReason();
				Installment installment = reason.getEgInstallmentMaster();
				Calendar cal = Calendar.getInstance();
				cal.setTime(installment.getInstallmentYear());
				BigDecimal collection = instWiseAmtCollMap.get(installment);
				if (ptDemand.getEgInstallmentMaster().equals(installment)
						&& reason.getEgDemandReasonMaster().getCode().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)
						&& collection.compareTo(BigDecimal.ZERO) == 0) {
					if (propertyTaxCollection.isRebatePeriodActive()) {
						BigDecimal rebate = propertyTaxCollection.calcEarlyPayRebate(demandDetail.getAmount());
						key = cal.get(Calendar.YEAR) + "-" + DEMANDRSN_REBATE;
						billdetail = createBillDet(installment, orderMap.get(key), BigDecimal.ZERO, rebate,
								GLCODE_FOR_TAXREBATE, DEMANDRSN_REBATE + " - " + installment.getDescription(),
								Integer.valueOf(0));
						billDetails.add(billdetail);

						key = cal.get(Calendar.YEAR) + "-" + reason.getEgDemandReasonMaster().getCode();
						billdetail = createBillDet(installment, orderMap.get(key),
								demandDetail.getAmount().subtract(demandDetail.getAmtCollected()), BigDecimal.ZERO,
								reason.getGlcodeId().getGlcode(), reason.getEgDemandReasonMaster().getReasonMaster()
										+ " - " + installment.getDescription(), Integer.valueOf(1));
						billDetails.add(billdetail);
					} else {
						key = cal.get(Calendar.YEAR) + "-" + reason.getEgDemandReasonMaster().getCode();
						billdetail = createBillDet(installment, orderMap.get(key),
								demandDetail.getAmount().subtract(demandDetail.getAmtCollected()), BigDecimal.ZERO,
								reason.getGlcodeId().getGlcode(), reason.getEgDemandReasonMaster().getReasonMaster()
										+ " - " + installment.getDescription(), Integer.valueOf(1));
						billDetails.add(billdetail); 
					}
				} else {
					if (!reason.getEgDemandReasonMaster().getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
						key = cal.get(Calendar.YEAR) + "-" + reason.getEgDemandReasonMaster().getCode();
						billdetail = createBillDet(installment, orderMap.get(key),
								demandDetail.getAmount().subtract(demandDetail.getAmtCollected()), BigDecimal.ZERO,
								reason.getGlcodeId().getGlcode(), reason.getEgDemandReasonMaster().getReasonMaster()
										+ " - " + installment.getDescription(), Integer.valueOf(1));
						billDetails.add(billdetail);
					}
				}
			}
		}
		Map<Installment, PropertyInstTaxBean> instTaxBean = nmcBillable.getInstTaxBean();
		for (Map.Entry<Installment, PropertyInstTaxBean> instMap : instTaxBean.entrySet()) {
			Installment installment = instMap.getKey();
			Calendar cal = Calendar.getInstance();
			cal.setTime(installment.getInstallmentYear());
			EgDemandDetails insertPenDmdDetail = null;
			if (instMap.getValue().getInstPenaltyAmt().compareTo(BigDecimal.ZERO) > 0) {
				insertPenDmdDetail = prepareDmdAndBillDetails(billObj, billDetails, nmcBillable, orderMap, instMap
						.getValue().getInstPenaltyAmt(), installment, cal);
			}
			if (insertPenDmdDetail != null) {
				pendmdList.add(insertPenDmdDetail);
			}
		}
		// inserting penalty demand details
		if (pendmdList != null) {
			for (EgDemandDetails penDmdDtls : pendmdList) {
				ptDemand.addEgDemandDetails(penDmdDtls);
			}
		}
		LOGGER.debug("Exiting method getBilldetails : " + billDetails);
		return billDetails;
	}

	private EgDemandDetails prepareDmdAndBillDetails(Billable billObj, List<EgBillDetails> billDetails,
			NMCPropertyTaxBillable nmcBillable, HashMap<String, Integer> orderMap, BigDecimal penAmt,
			Installment installment, Calendar cal) {
		LOGGER.info("Entered into prepareDmdAndBillDetails");
		LOGGER.info("preapreDmdAndBillDetails- Installment : " + installment + ", Penalty Amount: " + penAmt);
		EgBillDetails billdetail;
		String key;
		EgDemandDetails penDmdDtls = getPenaltyDmdDtls(billObj, installment);
		EgDemandDetails insertPenDmdDetail = null;

		if (nmcBillable.getLevyPenalty()) {// Checking whether to impose penalty
											// or not
			if (penDmdDtls != null) {
				BigDecimal collection = penDmdDtls.getAmtCollected() == null ? BigDecimal.ZERO : penDmdDtls
						.getAmtCollected();
				penDmdDtls.setAmount(collection.add(penAmt));
			} else {
				insertPenDmdDetail = insertPenaltyDmdDetail(installment, penAmt);
			}
			if (penAmt != null && !penAmt.equals(BigDecimal.ZERO)) {
				key = cal.get(Calendar.YEAR) + "-" + DEMANDRSN_CODE_PENALTY_FINES;
				billdetail = createBillDet(installment, orderMap.get(key), penAmt, BigDecimal.ZERO, GLCODE_FOR_PENALTY,
						DEMANDRSN_CODE_PENALTY_FINES + " - " + installment.getDescription(), Integer.valueOf(1));
				billDetails.add(billdetail);
			}
		}
		return insertPenDmdDetail;
	}

	// This Api is used to check for lat updated timestamp of demand details
	private Calendar getDmdDtlsLastUptTimeSt(EgDemandDetails penDmdDtls) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(penDmdDtls.getLastUpdatedTimeStamp());
		return calendar;
	}

	private Date getLPDateForArr(NMCPropertyTaxBillable nmcBillable) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date arrLpDate = null;
		try {
			Date LpDate = sdf.parse(ARR_LP_DATE_CONSTANT);

			Date propCrDt = nmcBillable.getBasicProperty().getPropCreateDate();
			if (propCrDt != null) {
				if (propCrDt.after(LpDate)) {
					arrLpDate = propCrDt;
				} else {
					arrLpDate = LpDate;
				}
			} else {
				arrLpDate = LpDate;
			}
		} catch (ParseException e) {
			throw new EGOVRuntimeException("Error while parsing Arrear late pay penalty break date", e);
		}
		return arrLpDate;
	}

	private EgDemandDetails createbillDetails(EgBill bill, Billable billObj, Installment inst, BigDecimal lpAmt,
			HashMap<String, Integer> orderMap) {

		EgDemandDetails insertPenDmdDetail = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(inst.getInstallmentYear());
		EgBillDetailsDao billDetailsDAO = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		EgBillDetails billdetail = null;
		EgDemandDetails penDmdDtls = getPenaltyDmdDtls(billObj, inst);
		if (penDmdDtls != null) {
			BigDecimal collection = penDmdDtls.getAmtCollected() == null ? BigDecimal.ZERO : penDmdDtls
					.getAmtCollected();
			penDmdDtls.setAmount(collection.add(lpAmt));
		} else {
			insertPenDmdDetail = insertPenaltyDmdDetail(inst, lpAmt);
		}
		if (lpAmt != null && !lpAmt.equals(BigDecimal.ZERO)) {
			String key = cal.get(Calendar.YEAR) + "-" + DEMANDRSN_CODE_PENALTY_FINES;
			billdetail = createBillDet(inst, orderMap.get(key), lpAmt, BigDecimal.ZERO, GLCODE_FOR_PENALTY,
					DEMANDRSN_CODE_PENALTY_FINES + " - " + inst.getDescription(), Integer.valueOf(1));
		}
		if (billdetail != null) {
			bill.addEgBillDetails(billdetail);
			billdetail.setEgBill(bill);
			billDetailsDAO.create(billdetail);
		}
		return insertPenDmdDetail;
	}

	private EgDemandDetails updateBillDetails(EgBillDetails billDtls, NMCPropertyTaxBillable billObj, BigDecimal penAmt) {
		EgDemandDetails insertPenDmdDetail = null;
		EgBillDetailsDao billDetailsDAO = DCBDaoFactory.getDaoFactory().getEgBillDetailsDao();
		EgDemandDetails penDmdDtls = getPenaltyDmdDtls(billObj, billDtls.getEgInstallmentMaster());
		if (penDmdDtls != null) {
			BigDecimal collection = penDmdDtls.getAmtCollected() == null ? BigDecimal.ZERO : penDmdDtls
					.getAmtCollected();
			penDmdDtls.setAmount(collection.add(penAmt));
		} else {
			insertPenDmdDetail = insertPenaltyDmdDetail(billDtls.getEgInstallmentMaster(), penAmt);
		}
		if (penAmt != null) {
			billDtls.setCrAmount(penAmt);
			billDetailsDAO.update(billDtls);
		}
		return insertPenDmdDetail;
	}

	private Installment getCurrInstallment() {
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		InstallmentDao isntalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		return isntalDao.getInsatllmentByModuleForGivenDate(module, new Date());
	}

	private EgDemandDetails insertPenaltyDmdDetail(Installment inst, BigDecimal lpAmt) {
		return propertyTaxCollection.insertPenalty(DEMANDRSN_CODE_PENALTY_FINES, lpAmt, inst);
	}

	private EgDemandDetails getPenaltyDmdDtls(Billable billObj, Installment inst) {
		return propertyTaxCollection.getDemandDetail(billObj.getCurrentDemand(), inst, DEMANDRSN_CODE_PENALTY_FINES);
	}

	/**
	 * @param installment
	 * @param orderNo
	 * @param billDetAmt
	 * @param rebateAmt
	 * @param glCode
	 * @param description
	 * @return
	 */
	EgBillDetails createBillDet(Installment installment, Integer orderNo, BigDecimal billDetAmt, BigDecimal rebateAmt,
			String glCode, String description, Integer isActualDemand) {

		if (orderNo == null || billDetAmt == null || glCode == null) {
			throw new EGOVRuntimeException("Exception in createBillDet....");
		}
		EgBillDetails billdetail = new EgBillDetails();
		// billdetail.setFunctionCode(functionCode);
		billdetail.setOrderNo(orderNo);
		billdetail.setCreateTimeStamp(new Date());
		billdetail.setLastUpdatedTimestamp(new Date());
		billdetail.setCrAmount(billDetAmt);
		billdetail.setDrAmount(rebateAmt);
		billdetail.setGlcode(glCode);
		billdetail.setDescription(description);
		billdetail.setAdditionalFlag(isActualDemand);
		// In case of currentInstallment >12, then currentInstallment - 13 is
		// stored.
		billdetail.setEgInstallmentMaster(installment);
		return billdetail;
	}

	@Override
	public void cancelBill() {
	}

	public Module getModule() {
		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		return moduleDao.getModuleByName(EGovConfig.getProperty(PTISCONFIGFILE, "MODULE_NAME", "", "PT"));
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setPropertyTaxCollection(PropertyTaxCollection propertyTaxCollection) {
		this.propertyTaxCollection = propertyTaxCollection;
	}
}
