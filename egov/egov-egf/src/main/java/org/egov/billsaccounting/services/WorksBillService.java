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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.billsaccounting.services;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.dao.BillsAccountingDAOFactory;
import org.egov.billsaccounting.dao.ContractorBillHibernateDAO;
import org.egov.billsaccounting.dao.EgwWorksDeductionsHibernateDAO;
import org.egov.billsaccounting.dao.EgwWorksMisHibernateDAO;
import org.egov.billsaccounting.dao.SupplierBillHibernateDAO;
import org.egov.billsaccounting.dao.WorksDetailHibernateDAO;
import org.egov.billsaccounting.model.Contractorbilldetail;
import org.egov.billsaccounting.model.EgwWorksDeductions;
import org.egov.billsaccounting.model.EgwWorksMis;
import org.egov.billsaccounting.model.Supplierbilldetail;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.commons.Fundsource;
import org.egov.commons.Relation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly=true)
public class WorksBillService   
{ 
private final static Logger LOGGER=Logger.getLogger(WorksBillService.class);
	 
	public List<EgwWorksDeductions> getStatutoryDeductionsByWorksdetail(Worksdetail worksdetail)
    {
         try {
			EgwWorksDeductionsHibernateDAO worksDedDAO = BillsAccountingDAOFactory
					.getDAOFactory().getEgwWorksDeductionsDAO();
			return worksDedDAO.getStatutoryDeductionsByWorksdetail(worksdetail);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception getStatutoryDeductionsByWorksdetail"
					+ e.getMessage(), e);
		}   
    }
	public List<Contractorbilldetail> getContractorBillDetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Relation relation, String vhNoFrom, String vhNoTo,Department dept,String functionary)
	{
		try {
			ContractorBillHibernateDAO contractorBillDAO = BillsAccountingDAOFactory
					.getDAOFactory().getContractorBillDAO();
			return contractorBillDAO.getContractorBillDetailFilterBy(fundId,
					fundSource, vhFromDate, vhToDate, relation, vhNoFrom,
					vhNoTo,dept,functionary);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception getContractorBillDetailFilterBy"
					+ e.getMessage(), e);
		}
	}
	
	public List<Supplierbilldetail> getSupplierBillDetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Relation relation, String vhNoFrom, String vhNoTo,Department dept,String functionary)
	{
		try {
			SupplierBillHibernateDAO supBillDAO = BillsAccountingDAOFactory
					.getDAOFactory().getSupplierBillDAO();
			return supBillDAO.getSupplierBillDetailFilterBy(fundId, fundSource,
					vhFromDate, vhToDate, relation, vhNoFrom, vhNoTo,dept,functionary);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in getSupplierBillDetailFilterBy"
					+ e.getMessage(), e);
		}
	}
	
	public Contractorbilldetail getContractorbilldetailById(Integer id)
	{
		try {
			ContractorBillHibernateDAO conBillDAO = BillsAccountingDAOFactory
					.getDAOFactory().getContractorBillDAO();
			return (Contractorbilldetail) conBillDAO.findById(id, false);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in getContractorbilldetailById"
					+ e.getMessage(), e);
		}
	}
	
	public Supplierbilldetail getSupplierbilldetailById(Integer id)
	{
		try {
			SupplierBillHibernateDAO supBillDAO = BillsAccountingDAOFactory
					.getDAOFactory().getSupplierBillDAO();
			return (Supplierbilldetail) supBillDAO.findById(id, false);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in getSupplierbilldetailById"
					+ e.getMessage(), e);
		}
	}
	
	public Worksdetail getWorksDetailById(Integer worksdetailId)
	{
		try {
					WorksDetailHibernateDAO wdDAO=BillsAccountingDAOFactory.getDAOFactory().getWorksDetailDAO();
		return (Worksdetail)wdDAO.findById(worksdetailId, false);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in getWorksDetailById"
					+ e.getMessage(), e);
		}
	}
	
	public void updateWorksdetail(Worksdetail worksdetail)
	{
		try {
			WorksDetailHibernateDAO wdDAO = BillsAccountingDAOFactory
					.getDAOFactory().getWorksDetailDAO();
			wdDAO.update(worksdetail);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in updateWorksdetail"
					+ e.getMessage(), e);
		}
	}
	public List getTdsForWorkOrder(Worksdetail worksdetailId)
	{
		try {
			WorksDetailHibernateDAO wdDAO = BillsAccountingDAOFactory
					.getDAOFactory().getWorksDetailDAO();
			return wdDAO.getAllTds(worksdetailId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in getTdsForWorkOrder"
					+ e.getMessage(), e);
		}
	}
	
	 public List getTotalBillValue(Worksdetail wd)
	 {
		 try {
			WorksDetailHibernateDAO wdDAO = BillsAccountingDAOFactory
					.getDAOFactory().getWorksDetailDAO();
			return wdDAO.getTotalBillValue(wd);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in getTotalBillValue"
					+ e.getMessage(), e);
		}
	 }
	 public List getTotalAdvAdj(Worksdetail wd)
	 {
		 try {
			WorksDetailHibernateDAO wdDAO = BillsAccountingDAOFactory
					.getDAOFactory().getWorksDetailDAO();
			return wdDAO.getTotalAdvAdj(wd);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in getTotalAdvAdj"
					+ e.getMessage(), e);
		}
	 }
	 public EgwWorksMis findByWorkOrderId(String workOrderId)
	 {
		try {
			EgwWorksMisHibernateDAO wmisDAO = BillsAccountingDAOFactory
					.getDAOFactory().getEgwWorksMisDAO();
			return wmisDAO.findByWorkOrderId(workOrderId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in findByWorkOrderId"
					+ e.getMessage(), e);
		}
	 }
	/* public WorksBillForm getWorksBillById(int billId){
		 WorksBillDelegate wbdelegate=new WorksBillDelegate();
		 WorksBillForm wbForm=new WorksBillForm();
		 wbForm= wbdelegate.getWorksBillById(billId);
		 return wbForm;
	 }*/
	 @Transactional
	 public void  createSupplierbilldetail(Supplierbilldetail sbd)
	 {
		 try {
			SupplierBillHibernateDAO supBillDAO = BillsAccountingDAOFactory
					.getDAOFactory().getSupplierBillDAO();
			supBillDAO.create(sbd);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in createSupplierbilldetail"
					+ e.getMessage(), e);
		}
		 
	 }
	 @Transactional
	 public void createContractorbilldetail( Contractorbilldetail cbd)
	 {
		 try {
			ContractorBillHibernateDAO contractorBillDAO = BillsAccountingDAOFactory
					.getDAOFactory().getContractorBillDAO();
			contractorBillDAO.create(cbd);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in createContractorbilldetail"
					+ e.getMessage(), e);
		}
	 }
	}
	

