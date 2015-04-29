package org.egov.works.web.actions.masters;  
  
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.CommonsService;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.services.PersistenceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.models.masters.ExpenditureType;
import org.egov.works.models.masters.Overhead;
import org.egov.works.services.WorksService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;
  
  
@ParentPackage("egov")  
@Result(name=Action.SUCCESS, type="ServletRedirectResult.class", location = "overhead.action")  
public class OverheadAction extends BaseFormAction{ 
	/**
	 *  An instance of Logger
	 */
	private static final Logger logger = Logger.getLogger(OverheadAction.class);
	/**
	 * An instance <code>PersistenceService</code>
	 */  
	private PersistenceService<Overhead,Long> overheadService;  
	
	/**
	 * An instance of <code>Overhead</code> populated from the view.
	 */
	private Overhead overhead= new Overhead();  
	
	/**
	 * A <code>Long</code> value representing the id of the model.
	 */
	private Long id;
	
	/**
	 * A <code>List</code> of <code>Overhead</code> objects representing 
	 * overhead data retrieved from the database.
	 */
	private WorksService worksService;
	

	private List<Overhead> overheadList=null;
	
	private List<ExpenditureType> expenditureTypeList=new ArrayList<ExpenditureType>();
	
	@Autowired
        private CommonsService commonsService;
	
	/**
	 * Default constructor
	 */
	public OverheadAction() {
		addRelatedEntity("account", CChartOfAccounts.class);
	}

	/**
	 * The default action method/
	 * 
	 * @return a <code>String</code> representing the value 'INDEX'
	 */
	public String execute() { 
		return list();  
	}  
	  
	/**
	 * This method is invoked to create a new form.
	 * 
	 * @return a <code>String</code> representing the value 'NEW'
	 */
	public String newform(){ 
		return NEW;  
	}  
	  
	/**
	 * This method is invoked to display the list of over heads.
	 * @return
	 */
	public String list() 
	{  
		overheadList = overheadService.findAllBy(" from Overhead o order by name asc");
		return INDEX;  
	}  
  
	public String edit()
	{  
		return EDIT;  
	}  
	  
	public String save()
	{  
		overheadService.persist(overhead);
		return SUCCESS;  
	}  
	  
	public String create()
	{  
		overheadService.persist(overhead);
		addActionMessage(getText("overhead.save.success","The overhead was saved successfully"));
        return list();  
	}  
	  
	public Object getModel() 
	{
		return overhead;  
	}  

	public void prepare()
	{
		if (id != null) {
			 overhead = overheadService.findById(id, false);
	    }
		
		expenditureTypeList = (List)overheadService.findAllBy("select distinct expenditureType from WorkType");
		
		super.prepare();
		setupDropdownDataExcluding("account");
		try {
			
			List<CChartOfAccounts> accounts = commonsService.getAccountCodeByPurpose(Integer.valueOf(worksService.getWorksConfigValue("OVERHEAD_PURPOSE")));
			addDropdownData("accountList", accounts);
			
		}
		catch (EGOVException e) {
			logger.error("Unable to load accountcode :"+e.getMessage());
			addFieldError("accountcode", "Unable to load accountcode");
		} 
		
		String[] expenditure = parameters.get("expenditure");
		if(!ArrayUtils.isEmpty(expenditure) && !expenditure[0].equals("-1"))
		{
			overhead.setExpenditureType(new ExpenditureType(expenditure[0]));
		}
	}

	public List<Overhead> getOverheadList() {  
		return overheadList;  
	}  
	
	public void setOverheadList(List<Overhead> overheadList) {
		this.overheadList = overheadList;
	}
	
	public PersistenceService<Overhead, Long> getOverheadService() 
	{
		return overheadService;
	}
	public void setOverheadService(PersistenceService<Overhead,Long> service) {  
		this.overheadService= service;  
	}  
	
	public Overhead getOverhead() {
		return overhead;
	}

	public void setOverhead(Overhead overhead) {
		this.overhead = overhead;
	}
	
	public List<ExpenditureType> getExpenditureTypeList() {
		return expenditureTypeList;
	}

	public void setExpenditureTypeList(List<ExpenditureType> expenditureTypeList) {
		this.expenditureTypeList = expenditureTypeList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	public WorksService getWorksService() {
		return worksService;
	}

	public void setWorksService(WorksService worksService) {
		this.worksService = worksService;
	}
}