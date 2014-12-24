package org.egov.payroll.services.payhead;


import java.util.List;


import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.payroll.dao.BulkRuleMstrDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.dao.SalaryCategoryMasterDAO;
import org.egov.payroll.dao.SalaryCodesDAO;
import org.egov.payroll.model.PayGenUpdationRule;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCategoryMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.pims.model.EmployeeGroupMaster;


public class PayheadServiceImpl implements PayheadService{



	public void createPayHead(SalaryCodes salaryCode) throws Exception{
			SalaryCodesDAO salaryCodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			EgovMasterDataCaching.getInstance().removeFromCache("pay-salaryCodes");
			salaryCodeDAO.create(salaryCode);

	}

	public SalaryCategoryMaster getSalCategoryMaster(String name) throws Exception{
			SalaryCategoryMasterDAO salCategoryMasterDAO = PayrollDAOFactory.getDAOFactory().getSalaryCategoryMasterDAO();
			return salCategoryMasterDAO.getSalCategoryMaster(name);

	}

	public SalaryCategoryMaster getSalCategoryMasterCach(String name)throws Exception{
			SalaryCategoryMasterDAO salCategoryMasterDAO = PayrollDAOFactory.getDAOFactory().getSalaryCategoryMasterDAO();
			return salCategoryMasterDAO.getSalCategoryMasterCach(name);
	}

	public List getAllSalaryCodes()throws Exception{
			SalaryCodesDAO salaryCodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return salaryCodeDAO.findAll();
	}

	public List<SalaryCodes> getAllSAlaryCodesSortedByOrder()throws Exception{
			SalaryCodesDAO salarycodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return salarycodeDAO.getAllSAlaryCodesSortedByOrder();
	}

	public List<SalaryCodes> getAllSalaryCodesByCache()throws Exception{
			List<SalaryCodes> salarycodes = EgovMasterDataCaching.getInstance().get("pay-salaryCodes");
			return salarycodes;
	}

	public SalaryCodes getSalaryCodeByHead(String head)throws Exception{
			SalaryCodesDAO salaryCodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return salaryCodeDAO.getSalaryCodesByHead(head);
	}

	public List getAllSalaryCodesByType(String type)throws Exception{
			SalaryCodesDAO salaryCodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return salaryCodeDAO.getAllSalarycodesByCategoryType(type);
	}
	public List getAllSalaryCodesByTypeAsSortedByOrder(String type)throws Exception{
		SalaryCodesDAO salaryCodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
		return salaryCodeDAO.getAllSalaryCodesByTypeAsSortedByOrder(type);
	}
	
	public List<SalaryCategoryMaster> getAllCategoryMasterByType(String type)throws Exception{
			SalaryCategoryMasterDAO salaryCategoryMasterDAO = PayrollDAOFactory.getDAOFactory().getSalaryCategoryMasterDAO();
			return salaryCategoryMasterDAO.getCategorymasterByType(type);
	}

	public List<SalaryCodes> getAllSalarycodesByCategoryName(String categoryName)throws Exception{
			SalaryCodesDAO salCodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return salCodeDAO.getSalaryCodesByCategoryName(categoryName);
	}
	public List<SalaryCodes> getAllSalarycodesByCategoryId(Integer categoryId)throws Exception{
			SalaryCodesDAO salCodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return salCodeDAO.getSalaryCodesByCategoryId(categoryId);
	}

	public void createRule(PayGenUpdationRule ruleObj) throws Exception{
		BulkRuleMstrDAO ruleMstrDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
		ruleMstrDAO.create(ruleObj);
	}
		
		public PayGenUpdationRule checkRuleBasedOnSalCodeMonFnYrEmpGrp(Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId) throws Exception
		{
			BulkRuleMstrDAO ruleDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
			PayGenUpdationRule payUniqueObj=ruleDAO.checkRuleBasedOnSalCodeMonFnYrEmpGrp(salaryCodeId, monthId, finId,empGrpMstrId);
			return payUniqueObj;
		}
		public PayGenUpdationRule getRuleMstrById(Integer ruleId)throws Exception
		{
			BulkRuleMstrDAO ruleDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
			PayGenUpdationRule payUniqueObj=ruleDAO.getRuleMstrById(ruleId);
			return payUniqueObj;
		}
		public List getAllRuleMstr()throws Exception
		{
			BulkRuleMstrDAO ruleMstrDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
			return ruleMstrDAO.findAll();
		}
		
		public void updateRule(PayGenUpdationRule ruleObj) throws Exception{
			BulkRuleMstrDAO ruleMstrDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
			ruleMstrDAO.update(ruleObj);
		}
		public List getRulemasterByMonFnYr(Integer monthId,Integer finId)
		{
			BulkRuleMstrDAO ruleMstrDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
			return ruleMstrDAO.getRulemasterByMonFnYr(monthId, finId);
		}
		
		//To Modify rule 
		public boolean checkRuleExists(Integer ruleId,Integer salaryCodeId,Integer monthId,Integer finId,Integer empGrpMstrId)throws Exception
		{
			BulkRuleMstrDAO ruleMstrDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
			return ruleMstrDAO.checkRuleExists(ruleId, salaryCodeId, monthId, finId,empGrpMstrId);
		}
		
		public PayheadRule getPayheadRuleById(Integer id){
			return (PayheadRule)PayrollDAOFactory.getDAOFactory().getPayheadRuleDAO().findById(id, false);
		}
		
		/**
		 *	save payheadRule 
		 */
		public PayheadRule savePayheadRule(PayheadRule payheadRule){
			PayrollDAOFactory.getDAOFactory().getPayheadRuleDAO().create(payheadRule);
			return payheadRule;
		}
		public  void updatePayheadRule(PayheadRule payheadRule)
		{
			PayrollDAOFactory.getDAOFactory().getPayheadRuleDAO().update(payheadRule);
		}
		
		/**
		 * Get all payheadrule
		 */
		public List<PayheadRule> getAllPayheadRule(){
			return PayrollDAOFactory.getDAOFactory().getPayheadRuleDAO().getAllPayheadRule();
		}
		public List<PayheadRule> getAllPayheadRulesBySalCode(Integer id)
		{
			return PayrollDAOFactory.getDAOFactory().getPayheadRuleDAO().getAllPayheadRulesBySalCode(id);
		}
		public void deletePayrule(PayheadRule prule)

		{
			PayrollDAOFactory.getDAOFactory().getPayheadRuleDAO().delete(prule);
		}

}
