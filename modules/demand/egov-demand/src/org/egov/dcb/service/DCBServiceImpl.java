package org.egov.dcb.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.DCBException;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBRecord;
import org.egov.dcb.bean.DCBReport;
import org.egov.dcb.bean.Receipt;
import org.egov.dcb.bean.ReceiptDetail;
import org.egov.demand.dao.DCBDaoFactory;
import org.egov.demand.dao.DemandGenericDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgDemandReasonMasterDao;
import org.egov.demand.dao.EgReasonCategoryDao;
import org.egov.demand.interfaces.Billable;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.demand.model.EgReasonCategory;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infstr.commons.Module;

public class DCBServiceImpl implements DCBService {

	private static final Logger LOGGER = Logger.getLogger(DCBServiceImpl.class);
	private EgReasonCategoryDao reasonCategoryDAO = DCBDaoFactory.getDaoFactory()
			.getEgReasonCategoryDao();
	private EgDemandReasonMasterDao reasonMasterDAO = DCBDaoFactory.getDaoFactory()
			.getEgDemandReasonMasterDao();
	private DemandGenericDao dmdGenDao = new DemandGenericHibDao();
	private InstallmentDao instalDAO = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
    private Billable billable;
    
    /**
     * This constructor will typically be used only for cases where this is a spring bean. The 
     * billable MUST subsequently be set via the setBillable() method.
     */
    public DCBServiceImpl() {
    }

    /**
     * If you are using this class as-is and not as a spring-injected bean, use this form of
     * construction.
     * 
     * @param billable
     */
    public DCBServiceImpl(Billable billable) {
        this.billable = billable;
    }
    
	public DCBReport getCurrentDCBAndReceipts(DCBDisplayInfo dcbDispInfo) {
	    validate();
		LOGGER.info("getCurrentDCBAndReceipts() called for: " + billable.getPropertyId());
		DCBReport dcbReport = new DCBReport();
		populateDCB(dcbDispInfo, dcbReport);
		populateReceipts(dcbReport);
		LOGGER.info("getCurrentDCBAndReceipts() returned: " + dcbReport);
		return dcbReport;
	}

    public DCBReport getCurrentDCBOnly(DCBDisplayInfo dcbDispInfo) {
        validate();
        LOGGER.info("getCurrentDCBOnly() called for: " + billable.getPropertyId());
        DCBReport dcbReport = new DCBReport();
        populateDCB(dcbDispInfo, dcbReport);
        LOGGER.info("getCurrentDCBOnly() returned: " + dcbReport);
        return dcbReport;
    }

    public DCBReport getReceiptsOnly() {
        validate();
        LOGGER.info("getReceiptsOnly() called for: " + billable.getPropertyId());
        DCBReport dcbReport = new DCBReport();
        populateReceipts(dcbReport);
        LOGGER.info("getReceiptsOnly() returned: " + dcbReport);
        return dcbReport;
    }

    private void populateDCB(DCBDisplayInfo dcbDispInfo, DCBReport report) {
        EgDemand egDemand = billable.getCurrentDemand();
        List<EgDemandReasonMaster> reasonMaster = prepareReasonMasters(egDemand, dcbDispInfo);
        List<String> fieldNames = prepareFieldNames(reasonMaster, dcbDispInfo);
        Module module = getModuleFromDemand(egDemand);
        Map<Installment, DCBRecord> dcbReportMap = new TreeMap<Installment, DCBRecord>();
        dcbReportMap = iterateDCB(dmdGenDao.getDCB(egDemand, module), dcbReportMap, fieldNames,
                egDemand);
        report.setFieldNames(fieldNames);
        report.setRecords(dcbReportMap);
        LOGGER.debug("Got DCB...");
    }
    
    private void populateReceipts(DCBReport report) {
        List<InstallmentReceiptTuple> tuples = enumerateTuples();
        Map<Installment, List<Receipt>> receipts = consolidateTuplesInstallmentWise(tuples);
        Map<Receipt, List<ReceiptDetail>> receiptBreakups = consolidateTuplesReceiptWise(tuples);
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
	 *@param dcbList
	 *            -List of the DCB Details which are retrieved from the
	 *            DataBase.
	 *@param dcbReportMap
	 *            -
	 *@param fieldNames
	 *            -Names of the ReasonMasters which are used to show in DCB
	 *            Screen.
	 *@param dmd
	 *            - Demand Object which is provided by the caller in Billable
	 *            Object
	 * @return java.util.Map<Installment, DCBRecord>
	 */
	Map<Installment, DCBRecord> iterateDCB(List dcbList, Map<Installment, DCBRecord> dcbReportMap,
			List<String> fieldNames, EgDemand dmd) {
		if (dcbList != null && fieldNames != null && !fieldNames.isEmpty()) {
			Installment installment = null;
			Map<String, BigDecimal> demands = null;
            Map<String, BigDecimal> collections = null;
            Map<String, BigDecimal> rebates = null;
			for (int i = 0; i < dcbList.size();) {
				Object[] dcbData = (Object[]) dcbList.get(i);
				installment = (Installment) instalDAO.findById(Integer.parseInt(dcbData[0]
						.toString()), false);
				DCBRecord dcbRecord = null;
				demands = new HashMap<String, BigDecimal>();
				collections = new HashMap<String, BigDecimal>();
				rebates = new HashMap<String, BigDecimal>();
				initDemandAndCollectionMap(fieldNames, demands, collections, rebates);
				for (int j = i; j < dcbList.size(); j++, i++) {
					Object[] dcbData2 = (Object[]) dcbList.get(i);
					if (dcbData[0].equals(dcbData2[0])) {
						dcbRecord = prepareDCMap(dcbData2, dcbRecord, demands, collections, rebates,
						        dmd, fieldNames);
					} else {
						break;
					}
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
	 *@param dcbData2
	 *            - Object array which is retrieved from the Database with DCB
	 *            Details
	 *@param dcbRecord
	 *@param demands
	 *@param collections
	 *@param dmd
	 *            - Demand Object which is provided by the caller in Billable
	 *            Object
	 *@param fieldNames
	 *            -Names of the ReasonMasters which are used to show in DCB
	 *            Screen.
	 * @return java.util.Map<Installment, DCBRecord>
	 */
	
	DCBRecord prepareDCMap(Object[] dcbData2, DCBRecord dcbRecord, Map<String, BigDecimal> demands,
			Map<String, BigDecimal> collections, Map<String, BigDecimal> rebates, EgDemand dmd, 
			List<String> fieldNames) {
		if (dcbData2 != null && dmd != null) {
			String reason = getReason(dcbData2, fieldNames);
			if (reason != null && demands != null && demands.containsKey(reason)) {
				demands.put(reason, demands.get(reason).add((BigDecimal) dcbData2[1]));
                collections.put(reason, collections.get(reason).add((BigDecimal) dcbData2[2]));
                rebates.put(reason, rebates.get(reason).add((BigDecimal) dcbData2[5]));
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
	 *@param dcbData2
	 *            - Object array which is retrieved from the Database with DCB
	 *            Details
	 *@param fieldNames
	 *            -Names of the ReasonMasters which are used to show in DCB
	 *            Screen.
	 * @return reason - Master reason(i.e either the ReasonMaster name or
	 *         ReasonCategory name)
	 */
	String getReason(Object[] dcbData2, List<String> fieldNames) {
		String reason = "";
		if (dcbData2 != null && fieldNames != null && !fieldNames.isEmpty()) {
			EgReasonCategory reasonCategory = (EgReasonCategory) reasonCategoryDAO.findById(Long
					.parseLong(dcbData2[4].toString()), false);
			EgDemandReasonMaster reasonMaster = (EgDemandReasonMaster) reasonMasterDAO.findById(
					Long.parseLong(dcbData2[3].toString()), false);
			if (reasonCategory != null && fieldNames != null
					&& fieldNames.contains(reasonCategory.getName())) {
				reason = reasonCategory.getName();
			} else if (reasonMaster != null && fieldNames != null
					&& fieldNames.contains(reasonMaster.getReasonMaster())) {
				reason = reasonMaster.getReasonMaster();
			}
		}
		return reason;
	}

	/**
	 * Method called internally to prepare the Map with fieldNames
	 * dynamically(i.e field names can be verified depending upon the client)
	 *
	 *@param prepareFieldNames
	 *@param demand
	 *@param collection
	 */
	void initDemandAndCollectionMap(List<String> prepareFieldNames,
			Map<String, BigDecimal> demand, Map<String, BigDecimal> collection,
			Map<String, BigDecimal> rebates) {
		if (prepareFieldNames != null && !prepareFieldNames.isEmpty()) {
			for (String fieldName : prepareFieldNames) {
				demand.put(fieldName, BigDecimal.ZERO);
				collection.put(fieldName, BigDecimal.ZERO);
				rebates.put(fieldName, BigDecimal.ZERO);
			}
		}
	}

	/**
	 * Method called internally to prepare the List of fieldNames
	 * dynamically(i.e field names can be verified depending upon the client)
	 * With the EgDemand reason masters (i.e either given by client or all the
	 * Reason Master of that module)
	 *
	 *@param dmdReasonMasters
	 *@param dcbDisPlayInfo
	 * @return java.util.List<String>
	 */

	List<String> prepareFieldNames(List<EgDemandReasonMaster> dmdReasonMasters,
			DCBDisplayInfo dcbDisPlayInfo) {
		List<String> fieldNames = new ArrayList<String>();
		if (dmdReasonMasters != null && !dmdReasonMasters.isEmpty()) {
			List<String> reasonCatgoryCodes = null;
			if (dcbDisPlayInfo != null && dcbDisPlayInfo.getReasonCategoryCodes() != null
					&& !dcbDisPlayInfo.getReasonCategoryCodes().isEmpty()) {
				reasonCatgoryCodes = dcbDisPlayInfo.getReasonCategoryCodes();
				fieldNames.addAll(getNamesFromCodes(reasonCatgoryCodes));
			}
			for (EgDemandReasonMaster reasonMaster : dmdReasonMasters) {
				if (reasonMaster != null && reasonMaster.getEgReasonCategory() != null) {
					String categoryReason = reasonMaster.getEgReasonCategory().getName();
					if (!fieldNames.contains(categoryReason)) {
						fieldNames.add(reasonMaster.getReasonMaster());
					}
				}
			}
		}
		return fieldNames;
	}
	
    public List<String> getNamesFromCodes(List<String> reasonCatgoryCodes) {
        List<String> categoryMasters = new ArrayList<String>();
        for (String reasonMasterCode : reasonCatgoryCodes) {
            categoryMasters.add(dmdGenDao.getReasonCategoryByCode(reasonMasterCode).getName());
        }
        return categoryMasters;
    }

	/**
	 * Method called internally to prepare EgDemand reason masters (i.e either
	 * given by client or all the Reason Master of that module)
	 *
	 *@param demand
	 *@param dcbDisPlayInfo
	 *@return java.util.List<EgDemandReasonMaster>
	 *@throws org.egov.DCBException.DCBException
	 *             (If the Module is null For the EgDemand)
	 */
	List<EgDemandReasonMaster> prepareReasonMasters(EgDemand demand, DCBDisplayInfo dcbDisPlayInfo) {
		List<String> RsonMasterCodes = null;
		List<EgDemandReasonMaster> reasonMsters = null;
		if (demand != null) {
			Module module = getModuleFromDemand(demand);
			if (module == null) { throw new DCBException(
					" EgModule are missing for the provided EgDemand Id =" + demand.getId()); }
			if (dcbDisPlayInfo != null && dcbDisPlayInfo.getReasonMasterCodes() != null
					&& !dcbDisPlayInfo.getReasonMasterCodes().isEmpty()) {
				RsonMasterCodes = dcbDisPlayInfo.getReasonMasterCodes();
			}
			if (RsonMasterCodes == null || RsonMasterCodes.isEmpty()) {
				reasonMsters = getEgdemandReasonMasters(module);
			} else {
				reasonMsters = getEgdemandReasonMasters(RsonMasterCodes, module);
			}
		}
		return reasonMsters;
	}

	/**
	 * Method called internally to get all EgDemand reason masters (i.e either
	 * given by client or all the Reason Master of that module)
	 *
	 *@param demandReasonMsterCodes
	 *            - ReasonMasters codes of which the module is used
	 *@param module
	 *@return java.util.List<EgDemandReasonMaster> - All the ReasonMaster
	 *         Objects for the given Master codes
	 */
	List<EgDemandReasonMaster> getEgdemandReasonMasters(List<String> demandReasonMsterCodes,
			Module module) {
		List<EgDemandReasonMaster> reasonMsters = null;
		if (demandReasonMsterCodes != null && !demandReasonMsterCodes.isEmpty() && module != null) {
			reasonMsters = new ArrayList<EgDemandReasonMaster>();
			for (String dmdReasonMasterCode : demandReasonMsterCodes) {
				EgDemandReasonMaster dmdReasonMaster = dmdGenDao.getDemandReasonMasterByCode(
						dmdReasonMasterCode, module);
				if (dmdReasonMaster != null) {
					reasonMsters.add(dmdReasonMaster);
				}
			}
		}
		return reasonMsters;
	}

	/**
	 * Method called internally to get EgModule From Egdemand.Module is taken
	 * from the ReasonMaster Object.
	 *
	 *@param demand
	 * @return java.util.List<EgDemandReasonMaster>
	 *@throws org.egov.DCBException.DCBException
	 *             (If the Module is null For the Provided EgDemand)
	 */
	Module getModuleFromDemand(EgDemand demand) {
		Module module = null;
		List demandRsonMastersList = new ArrayList();
		if (demand != null) {
			demandRsonMastersList = dmdGenDao.getEgDemandReasonMasterIds(demand);
			if (demandRsonMastersList == null || demandRsonMastersList.isEmpty()) { 
			    throw new DCBException("EgDemandReasonMasters missing for the provided EgDemand Id "
							+ demand.getId()); 
			}
			module = ((EgDemandReasonMaster) reasonMasterDAO.findById(Long.valueOf(new BigDecimal(
					demandRsonMastersList.get(0).toString()).toString()), true)).getEgModule();
		}
		return module;
	}

	/**
	 * Method called internally to get all Reason masters (i.e either provided
	 * by caller or get all the Reason Masters of particular module of the
	 * Demand Provided)
	 *
	 *@param demandReasonMsterCodes
	 *            - ReasonMasters codes of which the module is used
	 *@param module
	 *@return java.util.List<EgDemandReasonMaster> - All the ReasonMaster
	 *         Objects for the given Master codes
	 */
	List<EgDemandReasonMaster> getEgdemandReasonMasters(Module module) {
		List<EgDemandReasonMaster> reasonMsters = null;
		if (module != null) {
			reasonMsters = dmdGenDao.getDemandReasonMasterByModule(module);
		}
		return reasonMsters;
	}

    /**
     * Constructs a list of (installment, receipt) tuples, including duplicates.
     * @return
     */
    private List<InstallmentReceiptTuple> enumerateTuples() {
        List<InstallmentReceiptTuple> allReceiptTuples = new ArrayList<InstallmentReceiptTuple>();
        InstallmentReceiptTuple tuple = null;
        for (EgDemand demand : billable.getAllDemands()) {
            for (EgDemandDetails det : demand.getEgDemandDetails()) {
                for (EgdmCollectedReceipt collReceipt : det.getEgdmCollectedReceipts()) {
                    tuple = new InstallmentReceiptTuple(
                            det.getEgDemandReason().getEgInstallmentMaster(),
                            Receipt.mapFrom(collReceipt));
                    allReceiptTuples.add(tuple);
                }
            }
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
    private Map<Installment, List<Receipt>> consolidateTuplesInstallmentWise(
            List<InstallmentReceiptTuple> tuples) {
        Map<Installment, List<Receipt>> consolidated = new HashMap<Installment, List<Receipt>>();
        
        for (InstallmentReceiptTuple t : tuples) {
            List<Receipt> receiptsForInstallment = consolidated.get(t.installment);
            if (receiptsForInstallment == null) { // does not exist
                receiptsForInstallment = new ArrayList<Receipt>();
                consolidated.put(t.installment, receiptsForInstallment);
            }
            if (!receiptsForInstallment.contains(t.receipt)) { // no need to do data crunching if already there
                receiptsForInstallment.add(t.receipt);
            }
        }
        LOGGER.info("consolidateTuplesInstallmentWise() returned: " + consolidated);
        return consolidated;
    }
    
    /**
     * Constructs a list of receipts, each having its installment-wise amount breakups.
     * 
     * @param tuples
     * @return
     */
    private Map<Receipt, List<ReceiptDetail>> consolidateTuplesReceiptWise(
            List<InstallmentReceiptTuple> tuples) {
        Map<Receipt, List<ReceiptDetail>> consolidated = new HashMap<Receipt, List<ReceiptDetail>>();
        List<ReceiptDetail> breakups = null;
        for (InstallmentReceiptTuple t : tuples) {
            breakups = consolidated.get(t.receipt);
            if (breakups == null) {
                breakups = new ArrayList<ReceiptDetail>();
                consolidated.put(t.receipt, breakups);
            }
            ReceiptDetail det = t.receipt.getReceiptDetails().get(0);
            breakups.add(det);
        }
        
        LOGGER.info("consolidateTuplesReceiptWise() returned: " + consolidated);
        return consolidated;
    }
    
	@Override
    public void setBillable(Billable billable) {
        this.billable = billable;
    }

    private void validate() {
        if (billable == null) {
            throw new IllegalStateException("Please call the setBillable() method first!");
        }
    }

    public DemandGenericDao getDmdGenDao() {
		return dmdGenDao;
	}

	public void setDmdGenDao(DemandGenericDao dmdGenDao) {
		this.dmdGenDao = dmdGenDao;
	}

	public InstallmentDao getInstalDAO() {
		return instalDAO;
	}

	public void setInstalDAO(InstallmentDao instalDAO) {
		this.instalDAO = instalDAO;
	}

	public EgReasonCategoryDao getReasonCategoryDAO() {
		return reasonCategoryDAO;
	}

	public void setReasonCategoryDAO(EgReasonCategoryDao reasonCategoryDAO) {
		this.reasonCategoryDAO = reasonCategoryDAO;
	}

	public EgDemandReasonMasterDao getReasonMasterDAO() {
		return reasonMasterDAO;
	}

	public void setReasonMasterDAO(EgDemandReasonMasterDao reasonMasterDAO) {
		this.reasonMasterDAO = reasonMasterDAO;
	}
	
    /**
	 * This class is used when constructing a list of installment-receipt combinations. (We can't
	 * use a BidiMap because there is duplication of both values.) 
	 */
	private static class InstallmentReceiptTuple implements Comparable<InstallmentReceiptTuple> {
	    private Installment installment;
	    private Receipt receipt;
        private InstallmentReceiptTuple(Installment installment, Receipt receipt) {
            this.installment = installment;
            this.receipt = receipt;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb
            .append(installment).append(":")
            .append(receipt);
            return sb.toString();
        }
        
        /**
         * Sorting based on installments.
         */
        @Override
        public int compareTo(InstallmentReceiptTuple other) {
            return this.installment.compareTo(other.installment);
        }
	}

}