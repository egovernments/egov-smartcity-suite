package org.egov.tradelicense.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsHibernateDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.infstr.commons.Module;
import org.egov.tradelicense.utils.Constants;

public class LicenseDemand extends EgDemand {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(LicenseDemand.class);
	private License license;
	private DemandGenericDao dmdGenDao = new DemandGenericHibDao();
	private Installment egInstallmentMaster;
	private Date renewalDate;
	private char isLateRenewal;

	public License getLicense() {
		return license;
	}

	public LicenseDemand createDemand(List<FeeMatrix> feeMatrixList, NatureOfBusiness nature, LicenseAppType applType,
			Installment installment, License license, Set<EgDemandReasonMaster> egDemandReasonMasters,
			BigDecimal totalAmount, Module module) {
		LOGGER.debug("Initializing Demand...");
		generateDemand(feeMatrixList, installment, license, totalAmount, module);
		LOGGER.debug("Initializing Demand completed.");
		return this;
	}

	private BigDecimal generateDemand(List<FeeMatrix> feeMatrixList, Installment installment, License license,
			BigDecimal totalAmount, Module module) {
		setIsHistory("N");
		setEgInstallmentMaster(installment);
		setCreateTimestamp(new Date());
		setLicense(license);
		setIsLateRenewal('0');
		LOGGER.debug("calculating FEE          ...............................................");
		Set<EgDemandDetails> demandDetails = null;
		if(getEgDemandDetails().isEmpty() || getEgDemandDetails()==null)
			demandDetails = new LinkedHashSet<EgDemandDetails>();
		else
			demandDetails = getEgDemandDetails();
		for (FeeMatrix fm : feeMatrixList) {
			EgDemandReasonMaster reasonMaster = fm.getDemandReasonMaster();
			EgDemandReason reason = dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
					module);
			LOGGER.info("Reson for Reason Master"+":master:"+reasonMaster.getReasonMaster()+"Reason:"+reason);
			if (fm.getFeeType().getName().contains("Late")) {
				continue;
			} else  {
				if (license.getFeeTypeStr().equalsIgnoreCase("PFA")
						&& fm.getFeeType().getName().equalsIgnoreCase("CNC")) {
					InstallmentDao installmentDao = CommonsHibernateDaoFactory.getDAOFactory().getInstallmentDao();
					List<Installment> effectiveInstallment = installmentDao.getEffectiveInstallmentsforModuleandDate(
							installment.getFromDate(), 12 * 5, module);
					for (Installment inst : effectiveInstallment) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("REASON MASTER::::" + reasonMaster.getReasonMaster() + "::" + inst + "::"
									+ fm.getAmount() + ":::" + module.getModuleName());
						}
						EgDemandReason demandReason = dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
								inst, module);
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(reason + "::" + inst + "::" + fm.getAmount());
						}
						demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), demandReason,
								BigDecimal.ZERO));
						totalAmount = totalAmount.add(fm.getAmount());
					}
				} else {
					if(module.getModuleName().equals(Constants.PWDLICENSE_MODULENAME) || module.getModuleName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
					{
						if(license.getFeeExemption()!=null && license.getFeeExemption().equals("YES") && !license.getTradeName().getLicenseType().getName().equalsIgnoreCase((Constants.LICENSE_APP_TYPE_RENEW) )) {
							totalAmount = totalAmount.add(getDemandDetailsForFeeExemption(demandDetails, fm, reason, totalAmount, license));
						}
						else {
							if(license.getOldLicenseNumber()!=null) {
								// For Enter Pwd License generated full demand
								demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
								totalAmount = totalAmount.add(fm.getAmount());
							} else {
								totalAmount = getDemandDetails(demandDetails, fm, reason, totalAmount, license );
								
								if(!totalAmount.equals(BigDecimal.ZERO) && license.getState()!=null && (license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED) || license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED))){
										for(EgDemandDetails demDetails : demandDetails)
										{
											
											totalAmount = totalAmount.add(demDetails.getAmount()).subtract(demDetails.getAmtCollected());
										}
									}
								}
						}
					}
					else
					{
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(reason + "::" + fm.getAmount());
					}
					demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
					totalAmount = totalAmount.add(fm.getAmount());
					}
				}
			}
		}
		
		if(license.getState() == null || (license.getState()!=null && license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_END))){
			if (license.getDeduction() != null && license.getDeduction().compareTo(BigDecimal.ZERO) != 0) {
				EgReasonCategory  reasonCategory = dmdGenDao.getReasonCategoryByCode(Constants.DEMANDRSN_REBATE);
				List<EgDemandReasonMaster> demandReasonMasterByCategory = dmdGenDao.getDemandReasonMasterByCategoryAndModule(reasonCategory,module);
				for(EgDemandReasonMaster demandReasonMaster : demandReasonMasterByCategory){
				EgDemandReason reason = dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMaster, installment,module);
				 List<EgDemandReason>  egDemandReason = dmdGenDao.getDemandReasonByInstallmentAndModule(installment,module);
				 for(EgDemandReason egDemReason: egDemandReason) {
					 if (egDemReason.getId().equals(reason.getEgDemandReason().getId())) {
						 for(EgDemandDetails demDetails : demandDetails) {	
							 if(demDetails.getEgDemandReason().getId().equals(reason.getEgDemandReason().getId()))
							 demDetails.setAmtRebate(license.getDeduction());
							 setAmtRebate(license.getDeduction());
						 }
				     }
				  }
				}
				totalAmount = totalAmount.subtract(license.getDeduction());
	       }
		
			if (license.getOtherCharges() != null && license.getOtherCharges().compareTo(BigDecimal.ZERO) != 0) {
				EgDemandReasonMaster demandReasonMasterByCode = dmdGenDao.getDemandReasonMasterByCode("Other Charges", module);
				EgDemandReason reason = dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMasterByCode, installment,
						module);
				demandDetails.add(EgDemandDetails.fromReasonAndAmounts(license.getOtherCharges() != null ? license
						.getOtherCharges() : BigDecimal.ZERO, reason, BigDecimal.ZERO));
				totalAmount = totalAmount.add(license.getOtherCharges());
			}
			
			if (license.getSwmFee() != null && license.getSwmFee().compareTo(BigDecimal.ZERO) != 0) {
				EgDemandReasonMaster demandReasonMasterByCode = dmdGenDao.getDemandReasonMasterByCode("SWM", module);
				EgDemandReason reason = dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMasterByCode, installment,
						module);
				demandDetails.add(EgDemandDetails.fromReasonAndAmounts(license.getSwmFee() != null ? license
						.getSwmFee() : BigDecimal.ZERO, reason, BigDecimal.ZERO));
				totalAmount = totalAmount.add(license.getSwmFee());
			}
		}
				setEgDemandDetails(demandDetails);
				setBaseDemand(totalAmount);
		return totalAmount;
	}

	
	/**
	 * To generate Demand Details for Registration Fee and CNC Fee based on the State of the license, 
	 * if the license is created for the first time then only Registration Fee demand is generated and 
	 * if in the next state after Registration Fee Collected, then CNC demand is generated.
	 * @param demandDetails
	 * @param fm
	 * @param reason
	 * @param totalAmount
	 * @param license2
	 * @return
	 */
	private BigDecimal getDemandDetails(Set<EgDemandDetails> demandDetails,
			FeeMatrix fm, EgDemandReason reason, BigDecimal totalAmount, License license2) {
		
		if(license2.getState() == null && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE))
		{
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		}
		else if(license2.getState()!=null && license2.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED)  && fm.getDemandReasonMaster().getCode().equals(Constants.CNC))
		{
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		}
		else if(license2.getState()!=null && license2.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_END) && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE))
		{
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		}
		else if(license2.getState()!=null && license2.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED)  && fm.getDemandReasonMaster().getCode().equals(Constants.CNC))
		{
			demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
			totalAmount = totalAmount.add(fm.getAmount());
		}
		return totalAmount;
	}

	
	/**
	 * To generate Demand Details for Fee Exemption, if Fee Exemption is yes then Amt Rebate is set same as Registration Fee  
	 * @param demandDetails
	 * @param fm
	 * @param reason
	 * @param totalAmount
	 * @param license2
	 * @return
	 */
	private BigDecimal getDemandDetailsForFeeExemption(Set<EgDemandDetails> demandDetails,
			FeeMatrix fm, EgDemandReason reason, BigDecimal totalAmount, License license2) {
		
		
		EgDemandDetails  egDemandDetails= EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO);
			//If FeeExempted then set Amount Rebate equal to Registration Fee Demand
			if(license2.getFeeExemption()!=null && license2.getFeeExemption().equals("YES") &&  fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE))
			{
				BigDecimal amtRebate = egDemandDetails.getAmount();
				egDemandDetails.setAmtRebate(amtRebate);
				setAmtRebate(amtRebate);
			}
			totalAmount = egDemandDetails.getAmount();
			demandDetails.add(egDemandDetails);
		return totalAmount;
	}

	private BigDecimal generateLatePenalty(BigDecimal totalAmount, Set<EgDemandDetails> demandDetails, FeeMatrix fm,
			EgDemandReason reason, boolean isExpired, int noOfMonths) {
		if (fm.getFeeType().getName().contains("Late")) {
			if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) {
				BigDecimal calculatedLateFee = fm.getAmount().multiply(BigDecimal.valueOf(noOfMonths));
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("calculatedLateFee::" + calculatedLateFee);
					demandDetails.add(EgDemandDetails.fromReasonAndAmounts(calculatedLateFee, reason, BigDecimal.ZERO));
					totalAmount = totalAmount.add(calculatedLateFee);
				}
			}
		}
		return totalAmount;
	}

	public LicenseDemand renewDemand(List<FeeMatrix> feeMatrixList, NatureOfBusiness nature, LicenseAppType applType,
			Installment installment, License license, Set<EgDemandReasonMaster> egDemandReasonMasters,
			BigDecimal totalAmount, Module module, Date renewalDate, List<EgDemandDetails> oldDetails) {
		LOGGER.debug("Initializing Demand...");
		setIsHistory("N");
		setEgInstallmentMaster(installment);
		setCreateTimestamp(new Date());
		setLicense(license);
		setRenewalDate(new Date());
		String dateDiffToExpiryDate = license.getDateDiffToExpiryDate(renewalDate);
		String[] split = dateDiffToExpiryDate.split("/");
		
		boolean isExpired = split[0].equalsIgnoreCase("false") ? false : true;
		int noOfMonths = Integer.parseInt(split[1]);
		Set<EgDemandDetails> demandDetails = new LinkedHashSet<EgDemandDetails>();
		for (FeeMatrix fm : feeMatrixList) {
			if (fm.getFeeType().getName().contains("Late")) {
				if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) {
					EgDemandReasonMaster reasonMaster = fm.getDemandReasonMaster();
					EgDemandReason reason = dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
							installment, module);
					totalAmount = generateLatePenalty(totalAmount, demandDetails, fm, reason, isExpired, noOfMonths);
				}
			}
			
		}
		totalAmount=totalAmount.add(generateDemand(feeMatrixList, installment, license, totalAmount, module));
		demandDetails.addAll(getEgDemandDetails());
		if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) 
		{
			setIsLateRenewal((char)(noOfMonths - Constants.GRACEPERIOD ));
		}
		if(license.getState()!=null && !license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED) )
		demandDetails.addAll(oldDetails);
		//setEgDemandDetails(demandDetails);
		for(EgDemandDetails details:getEgDemandDetails())   
		{
			if(details.getEgDemandReason()==null)
			{
				LOGGER.info("Reason not Found for "+details.getAmount()+" ");
			}
		}
		setIsLateRenewal((""+noOfMonths).charAt(0));
		setBaseDemand(totalAmount);
		LOGGER.debug("Initializing Demand completed.");
		return this;
	}

	public LicenseDemand setViolationFeeForHawker(Installment installment,
			License license, Module module) {
		LOGGER.debug("Initializing Demand...");
		BigDecimal totalAmount = getBaseDemand();
		setLicense(license);
		Set<EgDemandDetails> demandDetails = null;
		if (getEgDemandDetails().isEmpty() || getEgDemandDetails() == null)
			demandDetails = new LinkedHashSet<EgDemandDetails>();
		else
			demandDetails = getEgDemandDetails();
		EgDemandReasonMaster reasonMaster = dmdGenDao.getDemandReasonMasterByCode(Constants.VIOLATION_FEE_DEMAND_REASON, module);
		EgDemandReason reason = dmdGenDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,installment, module);
		demandDetails.add(EgDemandDetails.fromReasonAndAmounts(license.getViolationFee() != null ? license.getViolationFee() : BigDecimal.ZERO, reason, BigDecimal.ZERO));
		totalAmount = totalAmount.add(license.getViolationFee());
		setEgDemandDetails(demandDetails);
		setBaseDemand(totalAmount);
		LOGGER.debug("Initializing Demand completed.");
		return this;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		if (license == null)
			str.append("license=null");
		else
			str.append(license.getApplicationNumber());
		str.append(super.toString());
		return str.toString();
	}

	public void getDemandForInstallment(Installment installment) {
	}

	public void updateCollectedForExisting() {
		for (EgDemandDetails dd : this.getEgDemandDetails()) {
			dd.setAmtCollected(dd.getAmount());
		}
	}

	public Installment getEgInstallmentMaster() {
		return egInstallmentMaster;
	}

	public void setEgInstallmentMaster(Installment egInstallmentMaster) {
		this.egInstallmentMaster = egInstallmentMaster;
	}

	public Date getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Date renewalDate) {
		this.renewalDate = renewalDate;
	}

	public char getIsLateRenewal() {
		return isLateRenewal;
	}

	public void setIsLateRenewal(char isLateRenewal) {
		this.isLateRenewal = isLateRenewal;
	}
}