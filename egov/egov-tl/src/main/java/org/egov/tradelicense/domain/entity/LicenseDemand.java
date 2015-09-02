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
package org.egov.tradelicense.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.infra.admin.master.entity.Module;
import org.egov.tradelicense.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

public class LicenseDemand extends EgDemand {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LicenseDemand.class);
    private License license;
    private Installment egInstallmentMaster;
    private Date renewalDate;
    private char isLateRenewal;
    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private DemandGenericDao demandGenericDao;

    public License getLicense() {
        return license;
    }

    public LicenseDemand createDemand(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature,
            final LicenseAppType applType,
            final Installment installment, final License license, final Set<EgDemandReasonMaster> egDemandReasonMasters,
            final BigDecimal totalAmount, final Module module) {
        LOGGER.debug("Initializing Demand...");
        generateDemand(feeMatrixList, installment, license, totalAmount, module);
        LOGGER.debug("Initializing Demand completed.");
        return this;
    }

    private BigDecimal generateDemand(final List<FeeMatrix> feeMatrixList, final Installment installment, final License license,
            BigDecimal totalAmount, final Module module) {
        setIsHistory("N");
        setEgInstallmentMaster(installment);
        setCreateDate(new Date());
        setLicense(license);
        setIsLateRenewal('0');
        LOGGER.debug("calculating FEE          ...............................................");
        Set<EgDemandDetails> demandDetails = null;
        if (getEgDemandDetails().isEmpty() || getEgDemandDetails() == null)
            demandDetails = new LinkedHashSet<EgDemandDetails>();
        else
            demandDetails = getEgDemandDetails();
        for (final FeeMatrix fm : feeMatrixList) {
            final EgDemandReasonMaster reasonMaster = fm.getDemandReasonMaster();
            final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
                    module);
            LOGGER.info("Reson for Reason Master" + ":master:" + reasonMaster.getReasonMaster() + "Reason:" + reason);
            if (fm.getFeeType().getName().contains("Late"))
                continue;
            else if (license.getFeeTypeStr().equalsIgnoreCase("PFA")
                    && fm.getFeeType().getName().equalsIgnoreCase("CNC")) {
                final List<Installment> effectiveInstallment = installmentDao.getEffectiveInstallmentsforModuleandDate(
                        installment.getFromDate(), 12 * 5, module);
                for (final Installment inst : effectiveInstallment) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("REASON MASTER::::" + reasonMaster.getReasonMaster() + "::" + inst + "::"
                                + fm.getAmount() + ":::" + module.getName());
                    final EgDemandReason demandReason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
                            inst, module);
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug(reason + "::" + inst + "::" + fm.getAmount());
                    demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), demandReason,
                            BigDecimal.ZERO));
                    totalAmount = totalAmount.add(fm.getAmount());
                }
            } else if (module.getName().equals(Constants.PWDLICENSE_MODULENAME)
                    || module.getName().equals(Constants.ELECTRICALLICENSE_MODULENAME))
            {
                if (license.getFeeExemption() != null && license.getFeeExemption().equals("YES")
                        && !license.getTradeName().getLicenseType().getName().equalsIgnoreCase(Constants.LICENSE_APP_TYPE_RENEW))
                    totalAmount = totalAmount
                    .add(getDemandDetailsForFeeExemption(demandDetails, fm, reason, totalAmount, license));
                else if (license.getOldLicenseNumber() != null) {
                    // For Enter Pwd License generated full demand
                    demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
                    totalAmount = totalAmount.add(fm.getAmount());
                } else {
                    totalAmount = getDemandDetails(demandDetails, fm, reason, totalAmount, license);

                    if (!totalAmount.equals(BigDecimal.ZERO)
                            && license.getState() != null
                            && (license
                                    .getState()
                                    .getValue()
                                    .equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE
                                            + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED) || license
                                            .getState()
                                            .getValue()
                                            .equals(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE
                                                    + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED)))
                        for (final EgDemandDetails demDetails : demandDetails)
                            totalAmount = totalAmount.add(demDetails.getAmount()).subtract(demDetails.getAmtCollected());
                }
            }
            else
            {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(reason + "::" + fm.getAmount());
                demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
                totalAmount = totalAmount.add(fm.getAmount());
            }
        }

        if (license.getState() == null || license.getState() != null
                && license.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_END)) {
            if (license.getDeduction() != null && license.getDeduction().compareTo(BigDecimal.ZERO) != 0) {
                final EgReasonCategory reasonCategory = demandGenericDao.getReasonCategoryByCode(Constants.DEMANDRSN_REBATE);
                final List<EgDemandReasonMaster> demandReasonMasterByCategory = demandGenericDao
                        .getDemandReasonMasterByCategoryAndModule(reasonCategory, module);
                for (final EgDemandReasonMaster demandReasonMaster : demandReasonMasterByCategory) {
                    final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(demandReasonMaster,
                            installment, module);
                    final List<EgDemandReason> egDemandReason = demandGenericDao.getDemandReasonByInstallmentAndModule(
                            installment,
                            module);
                    for (final EgDemandReason egDemReason : egDemandReason)
                        if (egDemReason.getId().equals(reason.getEgDemandReason().getId()))
                            for (final EgDemandDetails demDetails : demandDetails) {
                                if (demDetails.getEgDemandReason().getId().equals(reason.getEgDemandReason().getId()))
                                    demDetails.setAmtRebate(license.getDeduction());
                                setAmtRebate(license.getDeduction());
                            }
                }
                totalAmount = totalAmount.subtract(license.getDeduction());
            }

            if (license.getOtherCharges() != null && license.getOtherCharges().compareTo(BigDecimal.ZERO) != 0) {
                final EgDemandReasonMaster demandReasonMasterByCode = demandGenericDao.getDemandReasonMasterByCode(
                        "Other Charges",
                        module);
                final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                        demandReasonMasterByCode,
                        installment,
                        module);
                demandDetails.add(EgDemandDetails.fromReasonAndAmounts(license.getOtherCharges() != null ? license
                        .getOtherCharges() : BigDecimal.ZERO, reason, BigDecimal.ZERO));
                totalAmount = totalAmount.add(license.getOtherCharges());
            }

            if (license.getSwmFee() != null && license.getSwmFee().compareTo(BigDecimal.ZERO) != 0) {
                final EgDemandReasonMaster demandReasonMasterByCode = demandGenericDao.getDemandReasonMasterByCode("SWM", module);
                final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                        demandReasonMasterByCode,
                        installment,
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
     * To generate Demand Details for Registration Fee and CNC Fee based on the State of the license, if the license is created
     * for the first time then only Registration Fee demand is generated and if in the next state after Registration Fee
     * Collected, then CNC demand is generated.
     * @param demandDetails
     * @param fm
     * @param reason
     * @param totalAmount
     * @param license2
     * @return
     */
    private BigDecimal getDemandDetails(final Set<EgDemandDetails> demandDetails,
            final FeeMatrix fm, final EgDemandReason reason, BigDecimal totalAmount, final License license2) {

        if (license2.getState() == null && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE))
        {
            demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
            totalAmount = totalAmount.add(fm.getAmount());
        }
        else if (license2.getState() != null
                && license2.getState().getValue()
                .equals(Constants.WORKFLOW_STATE_TYPE_CREATENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED)
                && fm.getDemandReasonMaster().getCode().equals(Constants.CNC))
        {
            demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
            totalAmount = totalAmount.add(fm.getAmount());
        }
        else if (license2.getState() != null && license2.getState().getValue().equals(Constants.WORKFLOW_STATE_TYPE_END)
                && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE))
        {
            demandDetails.add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO));
            totalAmount = totalAmount.add(fm.getAmount());
        }
        else if (license2.getState() != null
                && license2.getState().getValue()
                .equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED)
                && fm.getDemandReasonMaster().getCode().equals(Constants.CNC))
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
    private BigDecimal getDemandDetailsForFeeExemption(final Set<EgDemandDetails> demandDetails,
            final FeeMatrix fm, final EgDemandReason reason, BigDecimal totalAmount, final License license2) {

        final EgDemandDetails egDemandDetails = EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, BigDecimal.ZERO);
        // If FeeExempted then set Amount Rebate equal to Registration Fee Demand
        if (license2.getFeeExemption() != null && license2.getFeeExemption().equals("YES")
                && fm.getDemandReasonMaster().getCode().equals(Constants.DEMAND_REASON_REGN_FEE))
        {
            final BigDecimal amtRebate = egDemandDetails.getAmount();
            egDemandDetails.setAmtRebate(amtRebate);
            setAmtRebate(amtRebate);
        }
        totalAmount = egDemandDetails.getAmount();
        demandDetails.add(egDemandDetails);
        return totalAmount;
    }

    private BigDecimal generateLatePenalty(BigDecimal totalAmount, final Set<EgDemandDetails> demandDetails, final FeeMatrix fm,
            final EgDemandReason reason, final boolean isExpired, final int noOfMonths) {
        if (fm.getFeeType().getName().contains("Late"))
            if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) {
                final BigDecimal calculatedLateFee = fm.getAmount().multiply(BigDecimal.valueOf(noOfMonths));
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("calculatedLateFee::" + calculatedLateFee);
                    demandDetails.add(EgDemandDetails.fromReasonAndAmounts(calculatedLateFee, reason, BigDecimal.ZERO));
                    totalAmount = totalAmount.add(calculatedLateFee);
                }
            }
        return totalAmount;
    }

    public LicenseDemand renewDemand(final List<FeeMatrix> feeMatrixList, final NatureOfBusiness nature,
            final LicenseAppType applType,
            final Installment installment, final License license, final Set<EgDemandReasonMaster> egDemandReasonMasters,
            BigDecimal totalAmount, final Module module, final Date renewalDate, final List<EgDemandDetails> oldDetails) {
        LOGGER.debug("Initializing Demand...");
        setIsHistory("N");
        setEgInstallmentMaster(installment);
        setCreateDate(new Date());
        setLicense(license);
        setRenewalDate(new Date());
        final String dateDiffToExpiryDate = license.getDateDiffToExpiryDate(renewalDate);
        final String[] split = dateDiffToExpiryDate.split("/");

        final boolean isExpired = split[0].equalsIgnoreCase("false") ? false : true;
        final int noOfMonths = Integer.parseInt(split[1]);
        final Set<EgDemandDetails> demandDetails = new LinkedHashSet<EgDemandDetails>();
        for (final FeeMatrix fm : feeMatrixList)
            if (fm.getFeeType().getName().contains("Late"))
                if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1) {
                    final EgDemandReasonMaster reasonMaster = fm.getDemandReasonMaster();
                    final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster,
                            installment, module);
                    totalAmount = generateLatePenalty(totalAmount, demandDetails, fm, reason, isExpired, noOfMonths);
                }
        totalAmount = totalAmount.add(generateDemand(feeMatrixList, installment, license, totalAmount, module));
        demandDetails.addAll(getEgDemandDetails());
        if (isExpired == true && noOfMonths - Constants.GRACEPERIOD >= 1)
            setIsLateRenewal((char) (noOfMonths - Constants.GRACEPERIOD));
        if (license.getState() != null
                && !license.getState().getValue()
                .equals(Constants.WORKFLOW_STATE_TYPE_RENEWLICENSE + Constants.WORKFLOW_REG_FEE_STATE_COLLECTED))
            demandDetails.addAll(oldDetails);
        // setEgDemandDetails(demandDetails);
        for (final EgDemandDetails details : getEgDemandDetails())
            if (details.getEgDemandReason() == null)
                LOGGER.info("Reason not Found for " + details.getAmount() + " ");
        setIsLateRenewal(("" + noOfMonths).charAt(0));
        setBaseDemand(totalAmount);
        LOGGER.debug("Initializing Demand completed.");
        return this;
    }

    public LicenseDemand setViolationFeeForHawker(final Installment installment,
            final License license, final Module module) {
        LOGGER.debug("Initializing Demand...");
        BigDecimal totalAmount = getBaseDemand();
        setLicense(license);
        Set<EgDemandDetails> demandDetails = null;
        if (getEgDemandDetails().isEmpty() || getEgDemandDetails() == null)
            demandDetails = new LinkedHashSet<EgDemandDetails>();
        else
            demandDetails = getEgDemandDetails();
        final EgDemandReasonMaster reasonMaster = demandGenericDao.getDemandReasonMasterByCode(
                Constants.VIOLATION_FEE_DEMAND_REASON,
                module);
        final EgDemandReason reason = demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
                module);
        demandDetails.add(EgDemandDetails.fromReasonAndAmounts(license.getViolationFee() != null ? license.getViolationFee()
                : BigDecimal.ZERO, reason, BigDecimal.ZERO));
        totalAmount = totalAmount.add(license.getViolationFee());
        setEgDemandDetails(demandDetails);
        setBaseDemand(totalAmount);
        LOGGER.debug("Initializing Demand completed.");
        return this;
    }

    public void setLicense(final License license) {
        this.license = license;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        if (license == null)
            str.append("license=null");
        else
            str.append(license.getApplicationNumber());
        str.append(super.toString());
        return str.toString();
    }

    public void getDemandForInstallment(final Installment installment) {
    }

    public void updateCollectedForExisting() {
        for (final EgDemandDetails dd : getEgDemandDetails())
            dd.setAmtCollected(dd.getAmount());
    }

    @Override
    public Installment getEgInstallmentMaster() {
        return egInstallmentMaster;
    }

    @Override
    public void setEgInstallmentMaster(final Installment egInstallmentMaster) {
        this.egInstallmentMaster = egInstallmentMaster;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(final Date renewalDate) {
        this.renewalDate = renewalDate;
    }

    public char getIsLateRenewal() {
        return isLateRenewal;
    }

    public void setIsLateRenewal(final char isLateRenewal) {
        this.isLateRenewal = isLateRenewal;
    }
}
