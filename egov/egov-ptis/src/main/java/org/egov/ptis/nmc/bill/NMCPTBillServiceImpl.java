/**
 *
 */
package org.egov.ptis.nmc.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_FEE_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_REBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_STR_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_ADVANCE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_MUTATION_FEE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GLCODE_FOR_TAXREBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.MAX_ADVANCES_ALLOWED;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class NMCPTBillServiceImpl extends BillServiceInterface {
	private static final Logger LOGGER = Logger
			.getLogger(NMCPTBillServiceImpl.class);
	PropertyTaxUtil propertyTaxUtil;
	PropertyTaxCollection propertyTaxCollection;
	DateFormat dateFormat = new SimpleDateFormat(
			NMCPTISConstants.DATE_FORMAT_DDMMYYY);
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;

	@Override
	public String getBillXML(Billable billObj) {
		if (billObj == null) {
			throw new EGOVRuntimeException(
					"Exception in getBillXML....Billable is null");
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
	@Override
	public List<EgBillDetails> getBilldetails(Billable billObj) {
		List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
		LOGGER.debug("Entered method getBilldetails : " + billObj);
		EgBillDetails billdetail = null;
		NMCPropertyTaxBillable nmcBillable = (NMCPropertyTaxBillable) billObj;

		if (nmcBillable.isMiscellaneous()) {
			Installment currInstallment = PropertyTaxUtil
					.getCurrentInstallment();
			billdetail = new EgBillDetails();
			billdetail.setOrderNo(1);
			billdetail.setCreateTimeStamp(new Date());
			billdetail.setLastUpdatedTimestamp(new Date());
			billdetail.setCrAmount(nmcBillable.getMutationFee());
			billdetail.setDrAmount(BigDecimal.ZERO);
			billdetail.setGlcode(GLCODE_FOR_MUTATION_FEE);
			billdetail.setDescription(MUTATION_FEE_STR + "-"
					+ currInstallment.getDescription());
			billdetail.setAdditionalFlag(Integer.valueOf(0));
			billdetail.setEgInstallmentMaster(currInstallment);
			billDetails.add(billdetail);
			return billDetails;
		}

		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory()
				.getPtDemandDao();
		Property activeProperty = nmcBillable.getBasicProperty().getProperty();
		BigDecimal rebateAmt = BigDecimal.ZERO;
		String key = "";
		BigDecimal balance = BigDecimal.ZERO;
		List<EgDemandDetails> pendmdList = new ArrayList<EgDemandDetails>();

		Ptdemand ptDemand = ptDemandDao
				.getNonHistoryCurrDmdForProperty(activeProperty);
		Installment currentInstallment = PropertyTaxUtil
				.getCurrentInstallment();

		HashMap<String, Integer> orderMap = propertyTaxUtil
				.generateOrderForDemandDetails(ptDemand.getEgDemandDetails(),
						nmcBillable);

		Map<Installment, BigDecimal> installmentWiseDemand = propertyTaxUtil
				.prepareRsnWiseDemandForProp(activeProperty);
		BigDecimal currentInstallmentDemand = installmentWiseDemand
				.get(currentInstallment);

		for (EgDemandDetails demandDetail : ptDemand.getEgDemandDetails()) {
			balance = BigDecimal.ZERO;
			key = "";
			balance = demandDetail.getAmount().subtract(
					demandDetail.getAmtCollected());

			if (balance.compareTo(BigDecimal.ZERO) == 1) {
				EgDemandReason reason = demandDetail.getEgDemandReason();
				Installment installment = reason.getEgInstallmentMaster();
				Calendar cal = Calendar.getInstance();
				cal.setTime(installment.getInstallmentYear());
				PropertyInstTaxBean installmentTaxBean = nmcBillable
						.getInstTaxBean().get(installment);

				BillDetailBean billDetailBean = null;

				String reasonMasterCode = reason.getEgDemandReasonMaster()
						.getCode();
				key = cal.get(Calendar.YEAR) + "-" + reasonMasterCode;

				if (reasonMasterCode
						.equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {

					if (installmentTaxBean != null) {
						rebateAmt = installmentTaxBean.getInstRebateAmt() != null ? installmentTaxBean
								.getInstRebateAmt() : BigDecimal.ZERO;
					} else {
						rebateAmt = BigDecimal.ZERO;
					}

					if (rebateAmt != null
							&& rebateAmt.compareTo(BigDecimal.ZERO) == 1) {
						String rebateKey = cal.get(Calendar.YEAR) + "-"
								+ DEMANDRSN_REBATE;
						billDetailBean = new BillDetailBean(installment,
								orderMap.get(rebateKey), rebateKey, rebateAmt,
								GLCODE_FOR_TAXREBATE, DEMANDRSN_REBATE,
								Integer.valueOf(0));

						billDetails.add(createBillDet(billDetailBean));
					}

					billDetailBean = new BillDetailBean(installment,
							orderMap.get(key), key, demandDetail.getAmount()
									.subtract(demandDetail.getAmtCollected()),
							reason.getGlcodeId().getGlcode(), reason
									.getEgDemandReasonMaster()
									.getReasonMaster(), Integer.valueOf(1));
					billDetails.add(createBillDet(billDetailBean));

				} else {

					if (!reasonMasterCode
							.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {

						billDetailBean = new BillDetailBean(installment,
								orderMap.get(key), key,
								demandDetail.getAmount().subtract(
										demandDetail.getAmtCollected()), reason
										.getGlcodeId().getGlcode(), reason
										.getEgDemandReasonMaster()
										.getReasonMaster(), Integer.valueOf(1));
						billDetails.add(createBillDet(billDetailBean));

					}
				}
			}
		}

		Map<Installment, PropertyInstTaxBean> instTaxBean = nmcBillable
				.getInstTaxBean();
		for (Map.Entry<Installment, PropertyInstTaxBean> instMap : instTaxBean
				.entrySet()) {
			Installment installment = instMap.getKey();
			Calendar cal = Calendar.getInstance();
			cal.setTime(installment.getInstallmentYear());
			EgDemandDetails insertPenDmdDetail = null;
			if (instMap.getValue().getInstPenaltyAmt() != null
					&& instMap.getValue().getInstPenaltyAmt()
							.compareTo(BigDecimal.ZERO) >= 0) {
				insertPenDmdDetail = prepareDmdAndBillDetails(billObj,
						billDetails, nmcBillable, orderMap, instMap.getValue()
								.getInstPenaltyAmt(), installment, cal);
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

		/*
		 * Advance payments can be made multiple times till the amount of 5
		 * years demand.
		 */

		/*
		 * Taking absolute value, because advance collection will be negative as
		 * demand is zero.
		 */
		/*
		 * if ((advanceColl.abs()).compareTo(maxAdvCollection.add(currInstBal))
		 * == -1) { billDetails = createAdvanceBillDetails(billDetails,
		 * advanceColl, currInstDem, orderNo); }
		 */

		createAdvanceBillDetails(billDetails, currentInstallment,
				currentInstallmentDemand, orderMap, ptDemand, nmcBillable);

		LOGGER.debug("Exiting method getBilldetails : " + billDetails);
		return billDetails;
	}

	/**
	 * Creates the advance bill details
	 * 
	 * @param billDetails
	 * @param orderMap
	 * @param currentInstallmentDemand
	 * @param demandDetail
	 * @param reason
	 * @param installment
	 */
	private void createAdvanceBillDetails(List<EgBillDetails> billDetails,
			Installment currentInstallment,
			BigDecimal currentInstallmentDemand,
			HashMap<String, Integer> orderMap, Ptdemand ptDemand,
			NMCPropertyTaxBillable nmcBillable) {

		BillDetailBean billDetailBean = null;
		String advanceKey = null;

		List<String> advanceInstDescription = PropertyTaxUtil
				.getAdvanceYearsFromCurrentInstallment();
		BigDecimal advanceCollection = new DemandGenericHibDao()
				.getBalanceByDmdMasterCode(ptDemand, DEMANDRSN_CODE_ADVANCE,
						getModule());

		if (advanceCollection.compareTo(BigDecimal.ZERO) < 0) {
			advanceCollection = advanceCollection.abs();
		}

		BigDecimal partiallyCollectedAmount = advanceCollection
				.remainder(currentInstallmentDemand);

		Integer noOfAdvancesPaid = (advanceCollection
				.subtract(partiallyCollectedAmount)
				.divide(currentInstallmentDemand)).intValue();

		LOGGER.debug("getBilldetails - advanceCollection = "
				+ advanceCollection + ", noOfAdvancesPaid=" + noOfAdvancesPaid);

		String advanceYear = "";

		if (noOfAdvancesPaid < MAX_ADVANCES_ALLOWED) {

			for (int i = noOfAdvancesPaid; i < advanceInstDescription.size(); i++) {

				advanceYear = advanceInstDescription.get(i).substring(0, 4);
				advanceKey = advanceYear + "-" + DEMANDRSN_CODE_ADVANCE;

				billDetailBean = new BillDetailBean(currentInstallment,
						orderMap.get(advanceKey),
						advanceInstDescription.get(i),
						i == noOfAdvancesPaid ? currentInstallmentDemand
								.subtract(partiallyCollectedAmount)
								: currentInstallmentDemand, GLCODE_FOR_ADVANCE,
						DEMANDRSN_STR_ADVANCE, Integer.valueOf(0));
				billDetails.add(createBillDet(billDetailBean));
			}

		} else {
			LOGGER.debug("getBillDetails - All advances are paid...");
		}
	}

	private EgDemandDetails prepareDmdAndBillDetails(Billable billObj,
			List<EgBillDetails> billDetails,
			NMCPropertyTaxBillable nmcBillable,
			HashMap<String, Integer> orderMap, BigDecimal penAmt,
			Installment installment, Calendar cal) {
		LOGGER.info("Entered into prepareDmdAndBillDetails");
		LOGGER.info("preapreDmdAndBillDetails- Installment : " + installment
				+ ", Penalty Amount: " + penAmt);
		EgBillDetails billdetail;
		String key;
		EgDemandDetails penDmdDtls = getPenaltyDmdDtls(billObj, installment);
		EgDemandDetails insertPenDmdDetail = null;

		boolean thereIsPenalty = penAmt != null
				&& !penAmt.equals(BigDecimal.ZERO) ? true : false;

		// Checking whether to impose penalty or not
		if (nmcBillable.getLevyPenalty()) {
			/* do not create penalty demand details if penalty is zero */
			if (penDmdDtls == null && thereIsPenalty) {
				insertPenDmdDetail = insertPenaltyDmdDetail(installment, penAmt);
			}

			if (thereIsPenalty) {
				key = cal.get(Calendar.YEAR) + "-"
						+ DEMANDRSN_CODE_PENALTY_FINES;
				BillDetailBean billDetailBean = new BillDetailBean(installment,
						orderMap.get(key), key, penAmt, GLCODE_FOR_PENALTY,
						NMCPTISConstants.DEMANDRSN_STR_PENALTY_FINES,
						Integer.valueOf(1));
				billDetails.add(createBillDet(billDetailBean));
			}
		}
		return insertPenDmdDetail;
	}

	private EgDemandDetails insertPenaltyDmdDetail(Installment inst,
			BigDecimal lpAmt) {
		return propertyTaxCollection.insertPenalty(
				DEMANDRSN_CODE_PENALTY_FINES, lpAmt, inst);
	}

	private EgDemandDetails getPenaltyDmdDtls(Billable billObj, Installment inst) {
		return propertyTaxCollection.getDemandDetail(
				billObj.getCurrentDemand(), inst, DEMANDRSN_CODE_PENALTY_FINES);
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
	EgBillDetails createBillDet(BillDetailBean billDetailBean) {
		LOGGER.debug("Entered into createBillDet, billDetailBean="
				+ billDetailBean);

		if (billDetailBean.invalidData()) {
			throw new EGOVRuntimeException("Exception in createBillDet....");
		}

		EgBillDetails billdetail = new EgBillDetails();
		billdetail.setOrderNo(billDetailBean.getOrderNo());
		billdetail.setCreateTimeStamp(new Date());
		billdetail.setLastUpdatedTimestamp(new Date());

		if (billDetailBean.isRebate()) {
			billdetail.setDrAmount(billDetailBean.getAmount());
			billdetail.setCrAmount(BigDecimal.ZERO);
		} else {
			billdetail.setCrAmount(billDetailBean.getAmount());
			billdetail.setDrAmount(BigDecimal.ZERO);
		}

		billdetail.setGlcode(billDetailBean.getGlCode());
		billdetail.setDescription(billDetailBean.getDescription());
		billdetail.setAdditionalFlag(billDetailBean.getIsActualDemand());
		// In case of currentInstallment >12, then currentInstallment - 13 is
		// stored.
		billdetail.setEgInstallmentMaster(billDetailBean.getInstallment());

		LOGGER.debug("Exiting from createBillDet");
		return billdetail;
	}

	@Override
	public void cancelBill() {
	}

	public Module getModule() {
		return moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setPropertyTaxCollection(
			PropertyTaxCollection propertyTaxCollection) {
		this.propertyTaxCollection = propertyTaxCollection;
	}
}
