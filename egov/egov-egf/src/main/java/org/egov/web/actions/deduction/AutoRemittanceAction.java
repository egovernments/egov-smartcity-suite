package org.egov.web.actions.deduction;

import org.apache.struts2.convention.annotation.Action;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.dao.recoveries.TdsHibernateDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.deduction.DepartmentDOMapping;
import org.egov.model.recoveries.Recovery;
import org.egov.model.recoveries.RemittanceSchedulerLog;
import org.egov.services.deduction.ScheduledRemittanceService;
import org.egov.utils.FinancialConstants;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.HibernateException;



public class AutoRemittanceAction extends BaseFormAction {

	private final static Logger LOGGER=Logger.getLogger(AutoRemittanceAction.class);
	private static final String MANUAL = "manual";
	private ScheduledRemittanceService scheduledRemittanceService;
	private String glcode;
	private Integer dept;
	private String drawingOfficer;
	private Date lastRunDate;
	private Map<String,String> coaMap;
	private	EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
	private List<DepartmentDOMapping> deptDOList;
	private RemittanceSchedulerLog remittanceScheduler;
	private Map<String,String> lastRunDateMap ;
	private TdsHibernateDAO tdsDAO;
	
	@Override
	public Object getModel() {
			return null;
	}
	
@Action(value="/deduction/autoRemittance-manualSchedule")
	public String manualSchedule()
	{
		try {
			coaMap = new LinkedHashMap<String,String>();
			List<Recovery> allActiveAutoRemitTds = tdsDAO.getAllActiveAutoRemitTds();
			
			for(Recovery r:allActiveAutoRemitTds)
			{
				coaMap.put(r.getChartofaccounts().getGlcode(), r.getChartofaccounts().getGlcode()+"-"+r.getChartofaccounts().getName());	
			}
			
			addDropdownData("departmentList", masterCache.get("egi-department"));
			deptDOList = persistenceService.findAllBy("from DepartmentDOMapping where department is not null  ");
			
			List<Object[]> list =HibernateUtil.getCurrentSession().
					createSQLQuery("select glcode, to_char(max(lastrundate),'dd/mm/yyyy') from egf_remittance_scheduler where glcode is not null and sch_type='A' "+
							" GROUP by glcode order by glcode").list();
			lastRunDateMap=new HashMap<String,String>();
			for(Object[] ob:list)
			{
				lastRunDateMap.put((String)ob[0],(String) ob[1]);
			}
		} catch (EGOVRuntimeException e) {
			addActionError("failed");
		} catch (HibernateException e) {
			addActionError("failed");
		} catch (Exception e) {
			addActionError("failed");
	}
		
		return "manual";
		
	}
	
	@ValidationErrorPage(value="messages")
	public String schedule()
	{
		try {
			LOGGER.info("Inside RemittanceJob");
			remittanceScheduler = new RemittanceSchedulerLog();
			remittanceScheduler.setGlcode(glcode);
			remittanceScheduler.setSchType(FinancialConstants.REMITTANCE_SCHEDULER_SCHEDULAR_TYPE_MANUAL);
			remittanceScheduler.setSchJobName("Manual");
			remittanceScheduler.setLastRunDate(new Date());
			remittanceScheduler.setCreatedDate(new Date());
			remittanceScheduler.setCreatedBy( Integer.valueOf(EGOVThreadLocals.getUserId()));
			remittanceScheduler.setStatus("Started");      
			scheduledRemittanceService.getRemittanceSchedulerLogService().persist(remittanceScheduler);
			Long schedularLogId=remittanceScheduler.getId();
			boolean searchRecovery = scheduledRemittanceService.searchRecovery(glcode,"Manual",schedularLogId,dept,lastRunDate);
			if(searchRecovery==false)
			{
				addActionMessage(getText("schedular.failed"));
				addActionMessage(scheduledRemittanceService.getErrorMessage().toString());
			}else
			{
				addActionMessage(getText("schedular.succeful"));
			}
		}
		catch(ValidationException e)
		{
			addActionMessage(getText("schedular.failed"));
			throw new ValidationException(Arrays.asList(new ValidationError(scheduledRemittanceService.getErrorMessage().toString(),scheduledRemittanceService.getErrorMessage().toString())));	
		}
		
		catch(Exception e)
		{
			addActionMessage(getText("schedular.failed"));
			throw new ValidationException(Arrays.asList(new ValidationError(scheduledRemittanceService.getErrorMessage().toString(),scheduledRemittanceService.getErrorMessage().toString())));	
		}
		List<String> findAllBy = (List<String>)scheduledRemittanceService.getRemittancePersistenceService().getPersistenceService().findAllBy("select voucherheaderId.voucherNumber from " +
				"RemittanceSchedulePayment  where schId.id=?",remittanceScheduler.getId());
		if(findAllBy.isEmpty())
		{
			addActionMessage(" No Payments Created ");
		}else
		{
		addActionMessage(" Payment vouchernumbers listed below");
		addActionMessage(findAllBy.toString().replace('[', ' ').replace(']', ' '));
		}
		return "messages";  
	}
	
	
	public void setScheduledRemittanceService(
			ScheduledRemittanceService scheduledRemittanceService) {
		this.scheduledRemittanceService = scheduledRemittanceService;     
	}





	public String getGlcode() {
		return glcode;
	}





	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}





	public Integer getDept() {
		return dept;
	}





	public void setDept(Integer dept) {
		this.dept = dept;
	}





	public String getDrawingOfficer() {
		return drawingOfficer;
	}





	public void setDrawingOfficer(String drawingOfficer) {
		this.drawingOfficer = drawingOfficer;
	}





	public Date getLastRunDate() {  
		return lastRunDate;
	}





	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}

	public Map<String, String> getCoaMap() {
		return coaMap;
	}

	public void setCoaMap(Map<String, String> coaMap) {
		this.coaMap = coaMap;
	}

	public RemittanceSchedulerLog getRemittanceScheduler() {
		return remittanceScheduler;
	}

	public void setRemittanceScheduler(RemittanceSchedulerLog remittanceScheduler) {
		this.remittanceScheduler = remittanceScheduler;
	}

	public List<DepartmentDOMapping> getDeptDOList() {
		return deptDOList;
	}

	public void setDeptDOList(List<DepartmentDOMapping> deptDOList) {
		this.deptDOList = deptDOList;
	}

	public Map<String, String> getLastRunDateMap() {
		return lastRunDateMap;
	}

	public void setLastRunDateMap(Map<String, String> lastRunDateMap) {
		this.lastRunDateMap = lastRunDateMap;
	}

	public void setTdsDAO(TdsHibernateDAO tdsDAO) {
		this.tdsDAO = tdsDAO;
	}

}
