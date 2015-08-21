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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.service.bill;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.QUARTZ_BULKBILL_JOBS;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_DEMANDNOTICE_GENERATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STRING_EMPTY;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgBill;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.calculator.DemandNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.demand.BulkBillGeneration;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides API to Generate a Demand Notice or the Bill giving the break up of
 * the tax amounts
 */
@Transactional(readOnly = true)
public class BillService {

	private static final Logger LOGGER = Logger.getLogger(BillService.class);
	private static final String STR_BILL_SHORTCUT = "B";

	private ReportService reportService;
	private NoticeService noticeService;
	@Autowired
        private PropertyTaxUtil propertyTaxUtil;
	private PTBillServiceImpl ptBillServiceImpl;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private String billNo;
	InputStream billPDF;
	@Autowired
	private ModuleService moduleDao;
	@Autowired
	private InstallmentDao installmentDao;
	@Autowired
	private PtDemandDao ptDemandDAO;
	@Autowired
	private PropertyTaxBillable propertyTaxBillable;
	private PersistenceService persistenceService;
	@Autowired
        private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private DemandNoticeInfo demandNoticeInfo;

	/**
	 * Generates a Demand Notice or the Bill giving the break up of the tax
	 * amounts and the <code>EgBill</code>
	 *
	 * @see EgBill
	 * @param basicProperty
	 * @return
	 */
	public ReportOutput generateBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into generateBill BasicProperty : " + basicProperty);
		ReportOutput reportOutput=null;
		try{
        		setBillNo(propertyTaxNumberGenerator
                                .generateManualBillNumber(basicProperty.getPropertyID()));
        		int noOfBillGenerated = getNumberOfBills(basicProperty);
        		if (noOfBillGenerated > 0) {
        		    setBillNo(getBillNo() + "/" + STR_BILL_SHORTCUT + noOfBillGenerated);
        		}
        		//To generate Notice having installment and reasonwise balance for a property
                         demandNoticeInfo.setBasicProperty(basicProperty);
                         demandNoticeInfo.setBillNo(getBillNo());
                         demandNoticeInfo.setPropertyWiseConsumptions(propertyTaxUtil.getPropertyWiseConsumptions(basicProperty.getUpicNo().toString()));
                         demandNoticeInfo.setDemandNoticeDetailsInfo(propertyTaxUtil.getDemandNoticeDetailsInfo(basicProperty));
                         
                         ReportRequest reportRequest = null;
                         reportRequest = new ReportRequest(REPORT_TEMPLATENAME_DEMANDNOTICE_GENERATION,demandNoticeInfo,new HashMap<String, Object>());
                         reportOutput = getReportService().createReport(reportRequest); 
                         if (reportOutput != null && reportOutput.getReportOutputData() != null) {
                             billPDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
                         }
        		saveEgBill(basicProperty, userId);// saving eg_bill 
        		basicProperty.setIsBillCreated(STATUS_BILL_CREATED);
                        basicProperty.setBillCrtError(STRING_EMPTY);
        		noticeService.saveNotice(getBillNo(), NOTICE_TYPE_BILL, basicProperty, billPDF);// Save Notice
        		noticeService.getSession().flush(); // Added as notice was not getting saved
		} catch (final Exception e) {
		    e.printStackTrace();
	              throw new EGOVRuntimeException("Bill Generation Exception : " + e);
	        }
		LOGGER.debug("Exiting from generateBill");
		return reportOutput;
	}

	/**
	 * Gives the count of generated bills
	 *
	 * @param basicProperty
	 * @return
	 */
	private int getNumberOfBills(BasicProperty basicProperty) {
		Installment currentInstallment = propertyTaxUtil.getCurrentInstallment();

		Long count = (Long) HibernateUtil
				.getCurrentSession()
				.createQuery(
						"SELECT COUNT (*) FROM EgBill WHERE module = ? "
								+ "AND egBillType.code = ? AND consumerId = ? AND is_Cancelled = 'N' "
								+ "AND issueDate between ? and ? ").setEntity(0, currentInstallment.getModule())
				.setString(1, BILLTYPE_MANUAL).setString(2, basicProperty.getUpicNo())
				.setDate(3, currentInstallment.getFromDate()).setDate(4, currentInstallment.getToDate()).list().get(0);
		return count.intValue();
	}

	private void saveEgBill(BasicProperty basicProperty, Integer userId) {
		LOGGER.debug("Entered into saveEgBill");
		LOGGER.debug("saveEgBill : BasicProperty: " + basicProperty);
		propertyTaxBillable.setBasicProperty(basicProperty);
		propertyTaxBillable.setUserId(userId.longValue());
		propertyTaxBillable.setReferenceNumber(getBillNo());
		propertyTaxBillable.setBillType(propertyTaxUtil.getBillTypeByCode(BILLTYPE_MANUAL));
		propertyTaxBillable.setLevyPenalty(Boolean.TRUE);
		EgBill egBill = ptBillServiceImpl.generateBill(propertyTaxBillable);
		LOGGER.debug("Exit from saveEgBill, EgBill: " + egBill);
	}
	
	/**
	 * @description Called from ptisSchedular for bulk bill generation
	 * @param modulo
	 * @param billsCount
	 */
	public void bulkBillGeneration(Integer modulo,Integer billsCount){
	    LOGGER.debug("Entered into executeJob" + modulo);

            Long currentTime = System.currentTimeMillis();

            //returns all the property for which bill is created or cancelled
            Query query = getQuery(modulo,billsCount);

            List<String> assessmentNumbers = query.list();

            LOGGER.info("executeJob" + modulo + " - got " + assessmentNumbers
                            + "indexNumbers for bill generation");
            Long timeTaken = currentTime - System.currentTimeMillis();
            LOGGER.debug("executeJob" + modulo + " took " + (timeTaken / 1000)
                            + " secs for BasicProperty selection");
            LOGGER.debug("executeJob" + modulo + " - BasicProperties = "
                            + assessmentNumbers.size());
            LOGGER.info("executeJob" + modulo + " - Generating bills.....");

            currentTime = System.currentTimeMillis();
            int noOfBillsGenerated = 0;

            for (String assessmentNumber : assessmentNumbers) {
                    BasicProperty basicProperty = null;
                    try {
                            basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNumber);
                            generateBill(basicProperty,EgovThreadLocals.getUserId().intValue());
                            noOfBillsGenerated++;
                    } catch (Exception e) {
                            basicProperty.setIsBillCreated('F');
                            basicProperty.setBillCrtError(e.getMessage());
                            String msg = " Error while generating Demand bill via BulkBillGeneration Job "
                                            + modulo.toString();
                            String propertyType = " for "
                                            + (basicProperty.getIsMigrated().equals('Y') ? " migrated property "
                                                            : " non-migrated property ");
                            LOGGER.error(msg + propertyType + basicProperty.getUpicNo(), e);
                    }
            }

            timeTaken = currentTime - System.currentTimeMillis();

            LOGGER.info("executeJob" + modulo + " - " + noOfBillsGenerated + "/"
                            + assessmentNumbers.size() + " Bill(s) generated in "
                            + (timeTaken / 1000) + " (secs)");

            LOGGER.debug("Exiting from executeJob" + modulo);
	}
	
	/**
	 * @description returns list of property assestment number(Zone and ward wise). 
	 *              properties for which bill is not created or cancelled.
	 * @param modulo
	 * @param billsCount
	 * @return
	 */
	private Query getQuery(Integer modulo,Integer billsCount) {

            StringBuilder queryString = new StringBuilder(200);
            StringBuilder zoneParamString = new StringBuilder();
            StringBuilder wardParamString = new StringBuilder();
            Installment currentInstallment = propertyTaxUtil
                            .getCurrentInstallment();
            Module ptModule = moduleDao
                            .getModuleByName(PropertyTaxConstants.PTMODULENAME);
            //read zone and ward saved in bulkbillgeneration table.
            List<BulkBillGeneration> bulkBillGeneration = getPersistenceService().findAllBy(
                    "from BulkBillGeneration where zone.id is not null and installment.id = ? order by id", currentInstallment.getId());
            queryString = queryString
                    .append("select bp.upicNo ")
                    .append("from BasicPropertyImpl bp ")
                    .append("where bp.active = true ")
                    .append("and bp.upicNo IS not NULL ")
                    .append("and (bp.isBillCreated is NULL or bp.isBillCreated='N' or bp.isBillCreated='false') ")
                    .append("and MOD(bp.id, ").append(QUARTZ_BULKBILL_JOBS)
                    .append(") = :modulo ");
            if(bulkBillGeneration!=null && !bulkBillGeneration.isEmpty()){
                wardParamString.append("(");
                zoneParamString.append("(");
                int count = 1; 
                for (BulkBillGeneration bbg : bulkBillGeneration) {
                    if(bbg.getWard()!=null){
                        if (count == bulkBillGeneration.size()) {
                            wardParamString.append("'").append(bbg.getZone().getId()).append('-').append(bbg.getWard().getId())
                                            .append("')");
                        } else {
                            wardParamString.append("'").append(bbg.getZone().getId()).append('-').append(bbg.getWard().getId())
                                                .append("', ");
                        }
                      
                    } else {
                        if (count == bulkBillGeneration.size()) {
                            zoneParamString.append(bbg.getZone().getId())
                                            .append(")");
                        } else {
                            zoneParamString.append(bbg.getZone().getId())
                                                .append(", ");
                        }
                    }
                    count++;
                }
                if(wardParamString!=null){
                    if(wardParamString.charAt(wardParamString.length()-2)==',')
                       wardParamString.setCharAt(wardParamString.length()-2, ')');
                }
                if(zoneParamString!=null){
                    if(zoneParamString.charAt(zoneParamString.length()-2)==',')
                        zoneParamString.setCharAt(zoneParamString.length()-2, ')');
                }
                    
                if(wardParamString!=null && zoneParamString==null){
                   queryString.append(" AND ")
                    .append("bp.propertyID.ward.parent.id||'-'||bp.propertyID.ward.id")
                    .append(" IN ").append(wardParamString.toString());
                }
                else if(zoneParamString!=null && wardParamString==null){
                    queryString.append(" AND ")
                     .append("bp.propertyID.zone.id")
                     .append(" IN ").append(zoneParamString.toString());
                }
                else if(wardParamString!=null && zoneParamString!=null){
                    queryString.append(" AND ") 
                    .append("(bp.propertyID.ward.parent.id||'-'||bp.propertyID.ward.id")
                    .append(" IN ").append(wardParamString.toString())
                    .append(" OR ")
                    .append("bp.propertyID.zone.id")
                    .append(" IN ").append(zoneParamString.toString()).append(')');
                }
            }
            queryString = queryString
                            .append(" AND bp NOT IN (SELECT bp FROM BasicPropertyImpl bp, EgBill b ")
                            .append("WHERE bp.active = true ")
                            .append("AND bp.upicNo = substring(b.consumerId, 1, strpos(b.consumerId,'(')-1) ")
                            .append("AND b.module = :ptModule ")
                            .append("AND b.egBillType = :billType ")
                            .append("AND b.is_History = 'N' ")
                            .append("AND b.is_Cancelled = 'N' ")
                            .append("AND (b.issueDate BETWEEN :fromDate AND :toDate)) ");
            Query query = getPersistenceService()
                            .getSession()
                            .createQuery(queryString.toString())
                            .setInteger("modulo", modulo)
                            .setEntity("ptModule", ptModule)
                            .setEntity("billType", propertyTaxUtil.getBillTypeByCode(
                                                            PropertyTaxConstants.BILLTYPE_MANUAL))
                            .setDate("fromDate", currentInstallment.getFromDate())
                            .setDate("toDate", currentInstallment.getToDate());
            query.setMaxResults(billsCount);

            return query;
    }


	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Map<String, Map<String, BigDecimal>> getReasonwiseDues() {
		return reasonwiseDues;
	}

	public void setReasonwiseDues(Map<String, Map<String, BigDecimal>> reasonwiseDues) {
		this.reasonwiseDues = reasonwiseDues;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public InputStream getBillPDF() {
		return billPDF;
	}

	public void setBillPDF(InputStream billPDF) {
		this.billPDF = billPDF;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

        public PTBillServiceImpl getPtBillServiceImpl() {
            return ptBillServiceImpl;
        }
    
        public void setPtBillServiceImpl(PTBillServiceImpl ptBillServiceImpl) {
            this.ptBillServiceImpl = ptBillServiceImpl;
        }

        public PersistenceService getPersistenceService() {
            return persistenceService;
        }

        public void setPersistenceService(PersistenceService persistenceService) {
            this.persistenceService = persistenceService;
        }

}
