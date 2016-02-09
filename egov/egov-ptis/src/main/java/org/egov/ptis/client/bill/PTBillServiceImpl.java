/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.client.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_REBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_MUTATION_FEE;
import static org.egov.ptis.constants.PropertyTaxConstants.GLCODE_FOR_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_FEE_STR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.demand.dao.EgDemandDetailsDao;
import org.egov.demand.interfaces.BillServiceInterface;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgBillDetails;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.service.collection.PropertyTaxCollection;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

public class PTBillServiceImpl extends BillServiceInterface {
    private static final Logger LOGGER = Logger.getLogger(PTBillServiceImpl.class);

    PropertyTaxCollection propertyTaxCollection;

    @Autowired
    PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private EgDemandDetailsDao egDemandDetailsDAO;

    @Override
    public String getBillXML(final Billable billObj) {
        if (billObj == null)
            throw new ApplicationRuntimeException("Exception in getBillXML....Billable is null");
        return super.getBillXML(billObj);
    }

    /**
     * Setting the EgBillDetails to generate XML as a part of Erpcollection Integration
     *
     * @see org.egov.demand.interfaces.BillServiceInterface
     */
    @Override
    public List<EgBillDetails> getBilldetails(final Billable billObj) {
        final List<EgBillDetails> billDetails = new ArrayList<EgBillDetails>();
        LOGGER.debug("Entered method getBilldetails : " + billObj);
        EgBillDetails billdetail = null;
        final PropertyTaxBillable billable = (PropertyTaxBillable) billObj;

        if (billable.isMutationFeePayment()) {
            final Installment currInstallment = PropertyTaxUtil.getCurrentInstallment();
            billdetail = new EgBillDetails();
            billdetail.setOrderNo(1);
            billdetail.setCreateDate(new Date());
            billdetail.setModifiedDate(new Date());
            billdetail.setCrAmount(billable.getMutationFee());
            billdetail.setDrAmount(BigDecimal.ZERO);
            billdetail.setGlcode(GLCODE_FOR_MUTATION_FEE);
            billdetail.setDescription(MUTATION_FEE_STR);
            billdetail.setAdditionalFlag(Integer.valueOf(0));
            billdetail.setEgInstallmentMaster(currInstallment);
            billdetail.setAdditionalFlag(Integer.valueOf(1));
            billDetails.add(billdetail);
            return billDetails;
        }

        String key = "";
        BigDecimal balance = BigDecimal.ZERO;
        DateTime installmentDate = null;
        BillDetailBean billDetailBean = null;
        EgDemandReason reason = null;
        Installment installment = null;
        String reasonMasterCode = null;
        final BasicProperty basicProperty = billable.getBasicProperty();
        final Property activeProperty = basicProperty.getProperty();
        Map<Installment, PenaltyAndRebate> installmentPenaltyAndRebate = new TreeMap<Installment, PenaltyAndRebate>();
        
        installmentPenaltyAndRebate = billable.getCalculatedPenalty();
        billable.setInstTaxBean(installmentPenaltyAndRebate);

        final Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(activeProperty);
        final HashMap<String, Integer> orderMap = propertyTaxUtil.generateOrderForDemandDetails(
                ptDemand.getEgDemandDetails(), billable);

        for (final EgDemandDetails demandDetail : ptDemand.getEgDemandDetails()) {

            balance = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

            if (!(balance.equals(BigDecimal.ZERO) || balance.equals(BigDecimal.valueOf(0.0)))) {

                reason = demandDetail.getEgDemandReason();
                installment = reason.getEgInstallmentMaster();
                reasonMasterCode = reason.getEgDemandReasonMaster().getCode();

                installmentDate = new DateTime(installment.getInstallmentYear().getTime());

                if (!reasonMasterCode.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES)) {
                    key = installmentDate.getMonthOfYear() + "/" + installmentDate.getYear() + "-" + reasonMasterCode;

                    billDetailBean = new BillDetailBean(installment, orderMap.get(key), key, demandDetail.getAmount()
                            .subtract(demandDetail.getAmtCollected()), demandDetail.getEgDemandReason().getGlcodeId()
                            .getGlcode(), reason.getEgDemandReasonMaster().getReasonMaster(), Integer.valueOf(1));

                    billDetails.add(createBillDet(billDetailBean));
                }

                if (reasonMasterCode.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX)) {

                    key = installmentDate.getMonthOfYear() + "/" + installmentDate.getYear() + "-" + DEMANDRSN_CODE_REBATE;

                    billDetailBean = new BillDetailBean(installment, orderMap.get(key), key,
                            installmentPenaltyAndRebate.isEmpty() ? BigDecimal.ZERO : installmentPenaltyAndRebate
                                    .get(installment).getRebate(),
                                    PropertyTaxConstants.GLCODE_FOR_TAXREBATE, DEMANDRSN_CODE_REBATE, Integer.valueOf(0));

                    billDetails.add(createBillDet(billDetailBean));
                }
            }
        }

        EgDemandDetails penaltyDemandDetail = null;
        for (final Map.Entry<Installment, PenaltyAndRebate> penaltyAndRebate : installmentPenaltyAndRebate.entrySet()) {
            penaltyDemandDetail = insertPenaltyAndBillDetails(billDetails, billable, orderMap, penaltyAndRebate.getValue()
                    .getPenalty(), penaltyAndRebate.getKey());
            if (penaltyDemandDetail != null)
                ptDemand.getEgDemandDetails().add(penaltyDemandDetail);
        }

        LOGGER.debug("Exiting method getBilldetails : " + billDetails);
        return billDetails;
    }

    private EgDemandDetails insertPenaltyAndBillDetails(final List<EgBillDetails> billDetails,
            final PropertyTaxBillable billable, final HashMap<String, Integer> orderMap, BigDecimal penalty,
            final Installment installment) {

        LOGGER.info("Entered into prepareDmdAndBillDetails");
        LOGGER.info("preapreDmdAndBillDetails- Installment : " + installment + ", Penalty Amount: " + penalty);
        String key = null;
        final EgDemandDetails penDmdDtls = getPenaltyDmdDtls(billable, installment);
        EgDemandDetails insertPenDmdDetail = null;

        final boolean thereIsPenalty = penalty != null && !(penalty.equals(BigDecimal.ZERO) || penalty.equals(BigDecimal.valueOf(0.0))) ? true : false;
        final DateTime installmentDate = new DateTime(installment.getInstallmentYear().getTime());

        // Checking whether to impose penalty or not
        if (billable.getLevyPenalty()) {
            /* do not create penalty demand details if penalty is zero */
            if (penDmdDtls == null && thereIsPenalty)
                insertPenDmdDetail = insertPenaltyDmdDetail(installment, penalty);
            else if (penDmdDtls != null)
                penalty = penDmdDtls.getAmount().subtract(penDmdDtls.getAmtCollected());
            if (thereIsPenalty) {
                key = installmentDate.getMonthOfYear() + "/" + installmentDate.getYear() + "-" + DEMANDRSN_CODE_PENALTY_FINES;
                final BillDetailBean billDetailBean = new BillDetailBean(installment, orderMap.get(key), key, penalty,
                        GLCODE_FOR_PENALTY, PropertyTaxConstants.DEMANDRSN_STR_PENALTY_FINES, Integer.valueOf(1));
                billDetails.add(createBillDet(billDetailBean));
            }
        }
        return insertPenDmdDetail;
    }

    private EgDemandDetails insertPenaltyDmdDetail(final Installment inst, final BigDecimal lpAmt) {
        return propertyTaxCollection.insertPenalty(DEMANDRSN_CODE_PENALTY_FINES, lpAmt, inst);
    }

    private EgDemandDetails getPenaltyDmdDtls(final Billable billObj, final Installment inst) {
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
    EgBillDetails createBillDet(final BillDetailBean billDetailBean) {
        LOGGER.debug("Entered into createBillDet, billDetailBean=" + billDetailBean);
        
        if (billDetailBean.invalidData())
            throw new ApplicationRuntimeException("Invalid bill details...");

        final EgBillDetails billdetail = new EgBillDetails();
        billdetail.setOrderNo(billDetailBean.getOrderNo());
        billdetail.setCreateDate(new Date());
        billdetail.setModifiedDate(new Date());

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
        return moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
    }

    public void setPropertyTaxUtil(final PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
    }

    public void setPropertyTaxCollection(final PropertyTaxCollection propertyTaxCollection) {
        this.propertyTaxCollection = propertyTaxCollection;
    }
}
