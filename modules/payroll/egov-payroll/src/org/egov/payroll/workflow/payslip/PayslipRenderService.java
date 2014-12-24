package org.egov.payroll.workflow.payslip;
/**
 * @author surya
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.models.State;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.inbox.DefaultWorkflowTypeService;
import org.egov.infstr.workflow.inbox.WorkflowTypeService;
import org.egov.payroll.model.EmpPayroll;
import org.egov.pims.utils.EisManagersUtill;
import org.hibernate.Query;



public class PayslipRenderService implements WorkflowTypeService<EmpPayroll> {

	private DefaultWorkflowTypeService<EmpPayroll> dftWFTypeService;

	public PayslipRenderService(PersistenceService persistenceService, Class<? extends EmpPayroll> workflowType) {
		dftWFTypeService = new DefaultWorkflowTypeService<EmpPayroll>(persistenceService);
		dftWFTypeService.setWorkflowType(workflowType);

	}

	public List<EmpPayroll> getAssignedWorkflowItems(Integer owner, Integer userId, String order) {
		//List<EmpPayroll> listPayslipView = new ArrayList<EmpPayroll>();		
		List<EmpPayroll>  listPayslip = dftWFTypeService.getAssignedWorkflowItems(owner, userId, order);
		return getGroupingPayslips(listPayslip);
	}

	private List<EmpPayroll> getGroupingPayslips(List<EmpPayroll> listPayslip) {
		List<EmpPayroll> listPayslipView = new ArrayList<EmpPayroll>();
		HashMap<String,List<EmpPayroll>> payslipVewMap = new HashMap<String, List<EmpPayroll>>();
		for(EmpPayroll payslip : listPayslip){
			//Grouping parameter from config table
			String month = EisManagersUtill.getMonthsStrVsDays(payslip.getMonth().intValue());
			String groupingByComb;
			groupingByComb = month + " " + payslip.getFinancialyear().getFinYearRange() + "-" + payslip.getEmpAssignment().getDeptId().getDeptName() ;
		 	String functionaryGroup = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTIONARY",new Date()).getValue();
		 	//Making functionary to false
		 	if("true".equalsIgnoreCase(functionaryGroup)){
		 		groupingByComb = groupingByComb  + "-" + payslip.getEmpAssignment().getFunctionary().getName();
		 	}
		 	String fundGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUND",new Date()).getValue();
		 	if("true".equalsIgnoreCase(fundGroup)){
		 		groupingByComb = groupingByComb  + "-" + payslip.getEmpAssignment().getFundId().getName();
		 	}
		 	String functionGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTION",new Date()).getValue();
		 	if("true".equalsIgnoreCase(functionGroup)){
		 		groupingByComb = groupingByComb  + "-" + payslip.getEmpAssignment().getFunctionId().getName();
		 	}
		 	//group by bill number
		 	String billNumberGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_BILL_NUMBER",new Date()).getValue();
		 	if("true".equalsIgnoreCase(billNumberGroup)){
		 		groupingByComb = groupingByComb  + "-" + payslip.getBillNumber().getBillNumber();
		 	}
		 	//PayslipService payslipService = new PayslipService();
		 	//if("Manual".equals(payslipService.typeOfPayslipWF())){
		 		groupingByComb = groupingByComb  + "-" + payslip.getState().getNextAction();
		 	//}
		 			 	
			//String comb = payslip.getMonth().toString()+"-"+payslip.getFinancialyear().getFinYearRange()+"-"+payslip.getEmpAssignment().getDeptId().getDeptName();			
			
			List<EmpPayroll> groupedPayslips = (List<EmpPayroll>)payslipVewMap.get(groupingByComb);			
			if(groupedPayslips == null){				
				groupedPayslips = new ArrayList<EmpPayroll>();
				groupedPayslips.add(payslip);
				payslipVewMap.put(groupingByComb, groupedPayslips);
			}
			else {
				groupedPayslips.add(payslip);
				payslipVewMap.put(groupingByComb, groupedPayslips);
			}
		}		
		Iterator <List<EmpPayroll>> iter = payslipVewMap.values().iterator();
		while(iter.hasNext()){
			final List<EmpPayroll> payslips = iter.next();
			EmpPayroll payslipView = new EmpPayroll()
			{
				@Override
				public String myLinkId() {
					String link = "";					
					for(EmpPayroll payslipObj : payslips){
						link += "ownerPosId="+payslipObj.getState().getOwner().getId().toString()+"&userId="+EGOVThreadLocals.getUserId();
						link += "&month="+payslipObj.getMonth().toString()+"&yearId="+payslipObj.getFinancialyear().getId().toString()+
								"&departmentId="+payslipObj.getEmpAssignment().getDeptId().getId().toString();
						String functionaryGroup = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTIONARY",new Date()).getValue();
					 	if("true".equalsIgnoreCase(functionaryGroup)){
							link += "&functionaryId="+payslipObj.getEmpAssignment().getFunctionary().getId().toString();
						}
					 	String fundGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUND",new Date()).getValue();
					 	if("true".equalsIgnoreCase(fundGroup)){
							link += "&fundId="+payslipObj.getEmpAssignment().getFundId().getId().toString();
						}
					 	String functionGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTION",new Date()).getValue();
					 	if("true".equalsIgnoreCase(functionGroup)){
							link += "&functionId="+payslipObj.getEmpAssignment().getFunctionId().getId().toString();
						}
					 	String billNumberGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_BILL_NUMBER",new Date()).getValue();
					 	if("true".equalsIgnoreCase(billNumberGroup)){
					 		link += "&billNumberId="+payslipObj.getBillNumber().getId();
					 	}
					 	//PayslipService payslipService = new PayslipService();
					 	//if("Manual".equals(payslipService.typeOfPayslipWF())){
					 		if(payslipObj.getCurrentState().getValue() != null){
					 			link += "&state="+payslipObj.getCurrentState().getValue();
					 		}
						//}
					 	break;
					}
				 	
				 	
					/*for(EmpPayroll payslipObj : payslips){
						link = link + payslipObj.getId() + "-";
					}*/
					return link;
				}
				@Override
				public String getStateType(){
					return "EmpPayroll";
				}
			};			
			payslipView.changeState(payslips.get(0).getCurrentState());
			payslipView.setMonth(payslips.get(0).getMonth());
			payslipView.setFinancialyear(payslips.get(0).getFinancialyear());
			payslipView.setEmpAssignment(payslips.get(0).getEmpAssignment());
			payslipView.getEmpAssignment().setDeptId(payslips.get(0).getEmpAssignment().getDeptId());		
			payslipView.setBillNumber(payslips.get(0).getBillNumber());
			listPayslipView.add(payslipView);
		}		
		return listPayslipView;
	}

	public List<EmpPayroll> getDraftWorkflowItems(Integer owner, Integer userId, String order) {
		List<EmpPayroll> listPayslipView = new ArrayList<EmpPayroll>();
		List<EmpPayroll>  listPayslip = dftWFTypeService.getDraftWorkflowItems(owner, userId, order);
		HashMap<String,List<EmpPayroll>> payslipVewMap = new HashMap<String, List<EmpPayroll>>();
		for(EmpPayroll payslip : listPayslip){			
			String comb = payslip.getMonth().toString()+"-"+payslip.getFinancialyear().getFinYearRange()+"-"+
																						payslip.getEmpAssignment().getDeptId().getDeptName();
			List<EmpPayroll> groupedPayslips = (List<EmpPayroll>)payslipVewMap.get(comb);			
			if(groupedPayslips == null){				
				groupedPayslips = new ArrayList<EmpPayroll>();
				groupedPayslips.add(payslip);
				payslipVewMap.put(comb, groupedPayslips);
			}
			else {
				groupedPayslips.add(payslip);
				payslipVewMap.put(comb, groupedPayslips);
			}
		}		
		Iterator <List<EmpPayroll>> iter = payslipVewMap.values().iterator();
		while(iter.hasNext()){
			final List<EmpPayroll> payslips = iter.next();
			EmpPayroll payslipView = new EmpPayroll()
			{
				@Override
				public String myLinkId() {
					String link = "";
					for(EmpPayroll payslipObj : payslips){
						link = link + payslipObj.getId() + "-";
					}
					return link;
				}
				public String getStateType(){
					return "EmpPayroll";
				}
			};			
			payslipView.changeState(payslips.get(0).getCurrentState());
			payslipView.setMonth(payslips.get(0).getMonth());
			payslipView.setFinancialyear(payslips.get(0).getFinancialyear());
			payslipView.setEmpAssignment(payslips.get(0).getEmpAssignment());
			payslipView.getEmpAssignment().setDeptId(payslips.get(0).getEmpAssignment().getDeptId());		
			payslipView.setBillNumber(payslips.get(0).getBillNumber());
			listPayslipView.add(payslipView);
		}
		return listPayslipView;
	}

	@Override
	public List<EmpPayroll> getFilteredWorkflowItems(Integer owner, Integer userId, Integer sender, Date fromDate, Date toDate) {
		return new ArrayList<EmpPayroll>();
	}

	public List<EmpPayroll> getWorkflowItems(final Map<String, Object> criteria) {		
		List<EmpPayroll>  listPayslip = dftWFTypeService.getWorkflowItems(criteria);
		return getGroupingPayslips(listPayslip);
	}

	@Override
	public List<EmpPayroll> getWorkflowItems(String arg0) {
		String param[] = arg0.split("&");		
		Map<String, String> parameters = new HashMap<String, String>();
		for(String paramObj : param){
			parameters.put(paramObj.split("=")[0], paramObj.split("=")[1]);
		}			
		BigDecimal month = new BigDecimal(parameters.get("month"));
		Long yearId = Long.parseLong(parameters.get("yearId"));
		Integer departmentId = Integer.parseInt(parameters.get("departmentId"));		
		Integer functionaryId = parameters.get("functionaryId")==null ? null:Integer.parseInt(parameters.get("functionaryId"));
		Integer fundId = parameters.get("fundId")==null ? null:Integer.parseInt(parameters.get("fundId"));
		Long functionId = parameters.get("functionId")==null ? null:Long.parseLong(parameters.get("functionId"));	
		Integer billNumberId=parameters.get("billNumberId")==null ? null:Integer.parseInt(parameters.get("billNumberId"));
		String query = "from EmpPayroll pay where pay.state.value !=:end and pay.state.value !=:new " +
						"and pay.empAssignment.deptId.id =:departmentId and " +
						"pay.month =:month and pay.financialyear.id =:yearId ";
		if(functionaryId != null){
			query += "and pay.empAssignment.functionary.id =:functionaryId ";
		}
		if(fundId != null){
			query += "and pay.empAssignment.fundId.id =:fundId ";
		}
		if(functionId != null){
			query += "and pay.empAssignment.functionId.id =:functionId ";
		}	
		if(billNumberId != null){
			query += "and pay.billNumber.id =:billNumberId ";
		}
		System.out.println("qry------"+query);
		Query qry = HibernateUtil.getCurrentSession().createQuery(query);		
		qry.setString("end", State.END);
		qry.setString("new", State.NEW);		
		qry.setInteger("departmentId", departmentId);
		qry.setBigDecimal("month", month);
		qry.setLong("yearId", yearId);
		if(functionaryId != null){
			qry.setInteger("functionaryId", functionaryId);
		}
		if(fundId != null){
			qry.setInteger("fundId", fundId);
		}
		if(functionId != null){
			qry.setLong("functionId", functionId);
		}		
		if(billNumberId != null){
			qry.setInteger("billNumberId",billNumberId);	
		}
		List<EmpPayroll>  listPayslip = qry.list();			
		return listPayslip;
	}
	

}
