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
	

