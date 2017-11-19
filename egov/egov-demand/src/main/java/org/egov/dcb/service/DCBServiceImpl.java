/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.dcb.service;

import org.apache.log4j.Logger;
import org.egov.DCBException;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBRecord;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.bean.Receipt;
import org.egov.dcb.bean.ReceiptDetail;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.EgDemandReasonMasterDao;
import org.egov.demand.dao.EgReasonCategoryDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infra.admin.master.entity.Module;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DCBServiceImpl implements DCBService {

    private static final Logger LOGGER = Logger.getLogger(DCBServiceImpl.class);
    @Autowired
    private EgReasonCategoryDao egReasonCategoryDAO;
    @Autowired
    private EgDemandReasonMasterDao egDemandReasonMasterDAO;
    @Autowired
    private DemandGenericDao demandGenericDAO;
    @Autowired
    private InstallmentHibDao installmentDAO;

    private Billable billable;

    /**
     * This constructor will typically be used only for cases where this is a
     * spring bean. The billable MUST subsequently be set via the setBillable()
     * method.
     */
    public DCBServiceImpl() {
    }

    /**
     * If you are using this class as-is and not as a spring-injected bean, use
     * this form of construction.
     *
     * @param billable
     */
    public DCBServiceImpl(final Billable billable) {
        this.billable = billable;
    }

    @Override
    public DCBReport getCurrentDCBAndReceipts(final DCBDisplayInfo dcbDispInfo) {
        validate();
        LOGGER.info("getCurrentDCBAndReceipts() called for: " + billable.getConsumerId());
        final DCBReport dcbReport = new DCBReport();
        populateDCB(dcbDispInfo, dcbReport);
        populateReceipts(dcbReport);
        LOGGER.info("getCurrentDCBAndReceipts() returned: " + dcbReport);
        return dcbReport;
    }

    @Override
    public DCBReport getCurrentDCBOnly(final DCBDisplayInfo dcbDispInfo) {
        validate();
        LOGGER.info("getCurrentDCBOnly() called for: " + billable.getConsumerId());
        final DCBReport dcbReport = new DCBReport();
        populateDCB(dcbDispInfo, dcbReport);
        LOGGER.info("getCurrentDCBOnly() returned: " + dcbReport);
        return dcbReport;
    }

    @Override
    public DCBReport getReceiptsOnly() {
        validate();
        LOGGER.info("getReceiptsOnly() called for: " + billable.getConsumerId());
        final DCBReport dcbReport = new DCBReport();
        populateReceipts(dcbReport);
        LOGGER.info("getReceiptsOnly() returned: " + dcbReport);
        return dcbReport;
    }

    private void populateDCB(final DCBDisplayInfo dcbDispInfo, final DCBReport report) {
        final EgDemand egDemand = billable.getCurrentDemand();
        final List<EgDemandReasonMaster> reasonMaster = prepareReasonMasters(egDemand, dcbDispInfo);
        final List<String> fieldNames = prepareFieldNames(reasonMaster, dcbDispInfo);
        final Module module = getModuleFromDemand(egDemand);
        Map<Installment, DCBRecord> dcbReportMap = new TreeMap<Installment, DCBRecord>();
        dcbReportMap = iterateDCB(demandGenericDAO.getDCB(egDemand, module), dcbReportMap, fieldNames, egDemand);
        report.setFieldNames(fieldNames);
        report.setRecords(dcbReportMap);
        LOGGER.debug("Got DCB...");
    }

    private void populateReceipts(final DCBReport report) {
        final List<InstallmentReceiptTuple> tuples = enumerateTuples();
        final Map<Installment, List<Receipt>> receipts = consolidateTuplesInstallmentWise(tuples);
        final Map<Receipt, List<ReceiptDetail>> receiptBreakups = consolidateTuplesReceiptWise(tuples);
        report.setReceipts(receipts);
        report.backfillReceiptDetails(receiptBreakups);
        LOGGER.debug("Got receipts...");
    }

    /**
     * Method called internally to iterate the DCB List which we get from
     * Database with the EgDemand Parameter passed by the caller. A Map is being
     * created with Installment as key and DCBRecord(Demand , Collection and
     * Balance maps) as value
     *
     * @param dcbList
     *            -List of the DCB Details which are retrieved from the
     *            DataBase.
     * @param dcbReportMap
     *            -
     * @param fieldNames
     *            -Names of the ReasonMasters which are used to show in DCB
     *            Screen.
     * @param dmd
     *            - Demand Object which is provided by the caller in Billable
     *            Object
     * @return java.util.Map<Installment, DCBRecord>
     */
    @SuppressWarnings("unchecked")
    Map<Installment, DCBRecord> iterateDCB(final List dcbList, final Map<Installment, DCBRecord> dcbReportMap,
            final List<String> fieldNames, final EgDemand dmd) {
        if (dcbList != null && fieldNames != null && !fieldNames.isEmpty()) {
            Installment installment = null;
            Map<String, BigDecimal> demands = null;
            Map<String, BigDecimal> collections = null;
            Map<String, BigDecimal> rebates = null;
            for (int i = 0; i < dcbList.size();) {
                final Object[] dcbData = (Object[]) dcbList.get(i);
                installment = (Installment) installmentDAO.findById(Integer.parseInt(dcbData[0].toString()), false);
                DCBRecord dcbRecord = null;
                demands = new HashMap<String, BigDecimal>();
                collections = new HashMap<String, BigDecimal>();
                rebates = new HashMap<String, BigDecimal>();
                initDemandAndCollectionMap(fieldNames, demands, collections, rebates);
                for (int j = i; j < dcbList.size(); j++, i++) {
                    final Object[] dcbData2 = (Object[]) dcbList.get(i);
                    if (dcbData[0].equals(dcbData2[0]))
                        dcbRecord = prepareDCMap(dcbData2, dcbRecord, demands, collections, rebates, dmd, fieldNames);
                    else
                        break;
                    dcbReportMap.put(installment, dcbRecord);
                }
            }
        }
        return dcbReportMap;
    }

    /**
     * Method called internally to iterate the DCB List and prepare the Demand
     * and Collection Maps with Field name(i,e either the EGReason Category
     * master name or EgDemandReasonMaster name depending upon the parameters
     * passed by the client) and Amount as the key.
     *
     * @param dcbData2
     *            - Object array which is retrieved from the Database with DCB
     *            Details
     * @param dcbRecord
     * @param demands
     * @param collections
     * @param dmd
     *            - Demand Object which is provided by the caller in Billable
     *            Object
     * @param fieldNames
     *            -Names of the ReasonMasters which are used to show in DCB
     *            Screen.
     * @return java.util.Map<Installment, DCBRecord>
     */

    DCBRecord prepareDCMap(final Object[] dcbData2, DCBRecord dcbRecord, final Map<String, BigDecimal> demands,
            final Map<String, BigDecimal> collections, final Map<String, BigDecimal> rebates, final EgDemand dmd,
            final List<String> fieldNames) {
        if (dcbData2 != null && dmd != null) {
            final String reason = getReason(dcbData2, fieldNames);
            if (reason != null && demands != null && demands.containsKey(reason)) {
                demands.put(reason, demands.get(reason).add(new BigDecimal(dcbData2[1].toString())));
                collections.put(reason, collections.get(reason).add(new BigDecimal(dcbData2[2].toString())));
                rebates.put(reason, rebates.get(reason).add(new BigDecimal(dcbData2[5].toString())));
            }
            dcbRecord = new DCBRecord(demands, collections, rebates);
        }
        return dcbRecord;
    }

    /**
     * Method called internally to get the Reason(i,e either the EGReason
     * Category master name or EgDemandReasonMaster name) to put as a key to
     * Demand and collection Maps.
     *
     * @param dcbData2
     *            - Object array which is retrieved from the Database with DCB
     *            Details
     * @param fieldNames
     *            -Names of the ReasonMasters which are used to show in DCB
     *            Screen.
     * @return reason - Master reason(i.e either the ReasonMaster name or
     *         ReasonCategory name)
     */
    String getReason(final Object[] dcbData2, final List<String> fieldNames) {
        String reason = "";
        if (dcbData2 != null && fieldNames != null && !fieldNames.isEmpty()) {
            final EgReasonCategory reasonCategory = egReasonCategoryDAO.findById(Long.valueOf(dcbData2[4].toString()), false);
            final EgDemandReasonMaster reasonMaster = egDemandReasonMasterDAO.findById(Long.valueOf(dcbData2[3].toString()), false);
            if (reasonCategory != null && fieldNames != null && fieldNames.contains(reasonCategory.getName()))
                reason = reasonCategory.getName();
            else if (reasonMaster != null && fieldNames != null && fieldNames.contains(reasonMaster.getReasonMaster()))
                reason = reasonMaster.getReasonMaster();
        }
        return reason;
    }

    /**
     * Method called internally to prepare the Map with fieldNames
     * dynamically(i.e field names can be verified depending upon the client)
     *
     * @param prepareFieldNames
     * @param demand
     * @param collection
     */
    void initDemandAndCollectionMap(final List<String> prepareFieldNames, final Map<String, BigDecimal> demand,
            final Map<String, BigDecimal> collection, final Map<String, BigDecimal> rebates) {
        if (prepareFieldNames != null && !prepareFieldNames.isEmpty())
            for (final String fieldName : prepareFieldNames) {
                demand.put(fieldName, BigDecimal.ZERO);
                collection.put(fieldName, BigDecimal.ZERO);
                rebates.put(fieldName, BigDecimal.ZERO);
            }
    }

    /**
     * Method called internally to prepare the List of fieldNames
     * dynamically(i.e field names can be verified depending upon the client)
     * With the EgDemand reason masters (i.e either given by client or all the
     * Reason Master of that module)
     *
     * @param dmdReasonMasters
     * @param dcbDisPlayInfo
     * @return java.util.List<String>
     */

    List<String> prepareFieldNames(final List<EgDemandReasonMaster> dmdReasonMasters,
            final DCBDisplayInfo dcbDisPlayInfo) {
        final List<String> fieldNames = new ArrayList<String>();
        if (dmdReasonMasters != null && !dmdReasonMasters.isEmpty()) {
            List<String> reasonCatgoryCodes = null;
            if (dcbDisPlayInfo != null && dcbDisPlayInfo.getReasonCategoryCodes() != null
                    && !dcbDisPlayInfo.getReasonCategoryCodes().isEmpty()) {
                reasonCatgoryCodes = dcbDisPlayInfo.getReasonCategoryCodes();
                fieldNames.addAll(getNamesFromCodes(reasonCatgoryCodes));
            }
            for (final EgDemandReasonMaster reasonMaster : dmdReasonMasters)
                if (reasonMaster != null && reasonMaster.getEgReasonCategory() != null) {
                    final String categoryReason = reasonMaster.getEgReasonCategory().getName();
                    if (!fieldNames.contains(categoryReason))
                        fieldNames.add(reasonMaster.getReasonMaster());
                }
        }
        return fieldNames;
    }

    public List<String> getNamesFromCodes(final List<String> reasonCatgoryCodes) {
        final List<String> categoryMasters = new ArrayList<String>();
        for (final String reasonMasterCode : reasonCatgoryCodes)
            categoryMasters.add(demandGenericDAO.getReasonCategoryByCode(reasonMasterCode).getName());
        return categoryMasters;
    }

    /**
     * Method called internally to prepare EgDemand reason masters (i.e either
     * given by client or all the Reason Master of that module)
     *
     * @param demand
     * @param dcbDisPlayInfo
     * @return java.util.List<EgDemandReasonMaster>
     * @throws org.egov.DCBException.DCBException
     *             (If the Module is null For the EgDemand)
     */
    List<EgDemandReasonMaster> prepareReasonMasters(final EgDemand demand, final DCBDisplayInfo dcbDisPlayInfo) {
        List<String> RsonMasterCodes = null;
        List<EgDemandReasonMaster> reasonMsters = null;
        if (demand != null) {
            final Module module = getModuleFromDemand(demand);
            if (module == null)
                throw new DCBException(" EgModule are missing for the provided EgDemand Id =" + demand.getId());
            if (dcbDisPlayInfo != null && dcbDisPlayInfo.getReasonMasterCodes() != null
                    && !dcbDisPlayInfo.getReasonMasterCodes().isEmpty())
                RsonMasterCodes = dcbDisPlayInfo.getReasonMasterCodes();
            if (RsonMasterCodes == null || RsonMasterCodes.isEmpty())
                reasonMsters = getEgdemandReasonMasters(module);
            else
                reasonMsters = getEgdemandReasonMasters(RsonMasterCodes, module);
        }
        return reasonMsters;
    }

    /**
     * Method called internally to get all EgDemand reason masters (i.e either
     * given by client or all the Reason Master of that module)
     *
     * @param demandReasonMsterCodes
     *            - ReasonMasters codes of which the module is used
     * @param module
     * @return java.util.List<EgDemandReasonMaster> - All the ReasonMaster
     *         Objects for the given Master codes
     */
    List<EgDemandReasonMaster> getEgdemandReasonMasters(final List<String> demandReasonMsterCodes, final Module module) {
        List<EgDemandReasonMaster> reasonMsters = null;
        if (demandReasonMsterCodes != null && !demandReasonMsterCodes.isEmpty() && module != null) {
            reasonMsters = new ArrayList<EgDemandReasonMaster>();
            for (final String dmdReasonMasterCode : demandReasonMsterCodes) {
                final EgDemandReasonMaster dmdReasonMaster = demandGenericDAO.getDemandReasonMasterByCode(
                        dmdReasonMasterCode, module);
                if (dmdReasonMaster != null)
                    reasonMsters.add(dmdReasonMaster);
            }
        }
        return reasonMsters;
    }

    /**
     * Method called internally to get EgModule From Egdemand.Module is taken
     * from the ReasonMaster Object.
     *
     * @param demand
     * @return java.util.List<EgDemandReasonMaster>
     * @throws org.egov.DCBException.DCBException
     *             (If the Module is null For the Provided EgDemand)
     */
    Module getModuleFromDemand(final EgDemand demand) {
        Module module = null;
        List demandRsonMastersList = new ArrayList();
        if (demand != null) {
            demandRsonMastersList = demandGenericDAO.getEgDemandReasonMasterIds(demand);
            if (demandRsonMastersList == null || demandRsonMastersList.isEmpty())
                throw new DCBException("EgDemandReasonMasters missing for the provided EgDemand Id " + demand.getId());
            module = egDemandReasonMasterDAO.findById(Long.valueOf(demandRsonMastersList.get(0).toString()), false)
                    .getEgModule();
        }
        return module;
    }

    /**
     * Method called internally to get all Reason masters (i.e either provided
     * by caller or get all the Reason Masters of particular module of the
     * Demand Provided)
     *
     * @param demandReasonMsterCodes
     *            - ReasonMasters codes of which the module is used
     * @param module
     * @return java.util.List<EgDemandReasonMaster> - All the ReasonMaster
     *         Objects for the given Master codes
     */
    List<EgDemandReasonMaster> getEgdemandReasonMasters(final Module module) {
        List<EgDemandReasonMaster> reasonMsters = null;
        if (module != null)
            reasonMsters = demandGenericDAO.getDemandReasonMasterByModule(module);
        return reasonMsters;
    }

    /**
     * Constructs a list of (installment, receipt) tuples, including duplicates.
     *
     * @return
     */
    private List<InstallmentReceiptTuple> enumerateTuples() {
        final List<InstallmentReceiptTuple> allReceiptTuples = new ArrayList<InstallmentReceiptTuple>();
        InstallmentReceiptTuple tuple = null;
        List<EgDemand> allDemands = billable.getAllDemands();
        if(allDemands != null) 
        	for (final EgDemand demand : allDemands)
        		for (final EgDemandDetails det : demand.getEgDemandDetails())
        			for (final EgdmCollectedReceipt collReceipt : det.getEgdmCollectedReceipts()) {
        				tuple = new InstallmentReceiptTuple(det.getEgDemandReason().getEgInstallmentMaster(),
                            Receipt.mapFrom(collReceipt));
        				allReceiptTuples.add(tuple);
        			}

        Collections.sort(allReceiptTuples);
        LOGGER.info("enumerateTuples() returned: " + allReceiptTuples);
        return allReceiptTuples;
    }

    /**
     * Constructs a list of receipts for each installment.
     *
     * @param tuples
     * @return
     */
    private Map<Installment, List<Receipt>> consolidateTuplesInstallmentWise(final List<InstallmentReceiptTuple> tuples) {
        final Map<Installment, List<Receipt>> consolidated = new HashMap<Installment, List<Receipt>>();

        for (final InstallmentReceiptTuple t : tuples) {
            List<Receipt> receiptsForInstallment = consolidated.get(t.installment);
            if (receiptsForInstallment == null) { // does not exist
                receiptsForInstallment = new ArrayList<Receipt>();
                consolidated.put(t.installment, receiptsForInstallment);
            }
            if (!receiptsForInstallment.contains(t.receipt))
                // data
                // crunching if
                // already there
                receiptsForInstallment.add(t.receipt);
        }
        LOGGER.info("consolidateTuplesInstallmentWise() returned: " + consolidated);
        return consolidated;
    }

    /**
     * Constructs a list of receipts, each having its installment-wise amount
     * breakups.
     *
     * @param tuples
     * @return
     */
    private Map<Receipt, List<ReceiptDetail>> consolidateTuplesReceiptWise(final List<InstallmentReceiptTuple> tuples) {
        final Map<Receipt, List<ReceiptDetail>> consolidated = new HashMap<Receipt, List<ReceiptDetail>>();
        List<ReceiptDetail> breakups = null;
        for (final InstallmentReceiptTuple t : tuples) {
            breakups = consolidated.get(t.receipt);
            if (breakups == null) {
                breakups = new ArrayList<ReceiptDetail>();
                consolidated.put(t.receipt, breakups);
            }
            final ReceiptDetail det = t.receipt.getReceiptDetails().get(0);
            breakups.add(det);
        }

        LOGGER.info("consolidateTuplesReceiptWise() returned: " + consolidated);
        return consolidated;
    }

    @Override
    public void setBillable(final Billable billable) {
        this.billable = billable;
    }

    private void validate() {
        if (billable == null)
            throw new IllegalStateException("Please call the setBillable() method first!");
    }

    /**
     * This class is used when constructing a list of installment-receipt
     * combinations. (We can't use a BidiMap because there is duplication of
     * both values.)
     */
    private static class InstallmentReceiptTuple implements Comparable<InstallmentReceiptTuple> {
        private final Installment installment;
        private final Receipt receipt;

        private InstallmentReceiptTuple(final Installment installment, final Receipt receipt) {
            this.installment = installment;
            this.receipt = receipt;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(installment).append(":").append(receipt);
            return sb.toString();
        }

        /**
         * Sorting based on installments.
         */
        @Override
        public int compareTo(final InstallmentReceiptTuple other) {
            return installment.compareTo(other.installment);
        }
    }

}