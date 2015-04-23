package org.egov.web.actions.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.Action;
import org.egov.model.budget.Budget;
import org.egov.model.budget.BudgetDetail;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.BudgetDetailConfig;
import org.egov.utils.Constants;

public class BudgetSearchAndModify extends BudgetSearchAction {
    private static final String ACTIONNAME="actionName";
    boolean enableApprovedAmount = false;
    boolean enableOriginalAmount = false;
    boolean consolidatedScreen = false;
    private String comments = "";
    private static final long serialVersionUID = 1L;
    private static final Logger    LOGGER    = Logger.getLogger(BudgetSearchAndModify.class);
    protected WorkflowService<Budget> budgetWorkflowService;
    private boolean    showDetails=false;
	private boolean isDetailByFunction;
	private ScriptService scriptService;

    public ScriptService getScriptService() {
		return scriptService;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

	public String modifyList(){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting modifyList...");
        if(parameters.containsKey(Constants.MODE) && ("approve".equals(parameters.get(Constants.MODE)[0]))){
				setMode(parameters.get(Constants.MODE)[0]);
				isApproveAction=true;
				disableBudget = true;
				enableApprovedAmount=true;
				//enableOriginalAmount=true;
			}
    	if(getMode()!=null && Constants.APPROVE.equals(getMode()) )
            budgetDetailApproveList();
        else
            budgetDetailList();
        //setEnablingAmounts();
        currentfinYearRange = topBudget.getFinancialYear().getFinYearRange();
        computePreviousYearRange();
        computeTwopreviousYearRange();
        computeNextYearRange();
       // isDetailByFunction=true;
       // setConsolidatedScreen(budgetRenderService.getConsolidateBudget());  
        populateNextYrBEinBudgetDetailList();
        loadApproverUser(savedbudgetDetailList);
        if(LOGGER.isInfoEnabled())     LOGGER.info("Completed modifyList...");
        return Constants.DETAILLIST;
    }
 
/*
 * this api is used fro budget detail workflow list
 */
  		public String modifyDetailList() {
  			if(LOGGER.isInfoEnabled())     LOGGER.info("starting modifyDetailList...");
  			if(parameters.containsKey(Constants.MODE) && ("approve".equals(parameters.get(Constants.MODE)[0]))){
  				setMode(parameters.get(Constants.MODE)[0]);
  				isApproveAction=true;
  				disableBudget = true;
  			}
  			if(getMode()!=null && Constants.APPROVE.equals(getMode()) )
  	            budgetDetailApprove();
  	        else
  	            budgetDetailList();
  			setEnablingAmounts();
  	        currentfinYearRange = topBudget.getFinancialYear().getFinYearRange();
  	        computePreviousYearRange();
  	        computeTwopreviousYearRange();
  	        computeNextYearRange();
  	       // setConsolidatedScreen(budgetRenderService.getConsolidateBudget());
  	        isDetailByFunction=true;
  	        populateNextYrBEinBudgetDetailList();
  	       loadApproverUser(savedbudgetDetailList);
  	       // return Constants.DETAILLIST;  
  	     if(LOGGER.isInfoEnabled())     LOGGER.info("completed modifyDetailList");	
  			return Constants.DETAILLIST;  
  		}
  		
  		public boolean isDetailByFunction() {
			return isDetailByFunction;
		}

		public void setDetailByFunction(boolean isDetailByFunction) {
			this.isDetailByFunction = isDetailByFunction;
		}

		public String budgetDetailApprove(){
  			
  			if(parameters.get("budgetDetail.id")[0]!=null)
  	        {
  				
  				budgetDetail=(BudgetDetail) persistenceService.find("from BudgetDetail where id=?",Long.valueOf(parameters.get("budgetDetail.id")[0]));
  	            setTopBudget(budgetDetail.getBudget());
  	            comments = topBudget.getState().getExtraInfo();
  	        }
  	       //if u want only selected function centre filter here by owner
  			String query =" from BudgetDetail bd where bd.budget=? and bd.function="+budgetDetail.getFunction().getId()+"  order by bd.function.name,bd.budgetGroup.name"	;
  			savedbudgetDetailList=budgetDetailService.findAllBy(query, topBudget);            
  	        re = checkRe(topBudget);
  	     // check what actuals needs to be shown for next year be AND possible remove if
  	       if(LOGGER.isInfoEnabled())     LOGGER.info("starting populateActualData...");
  	        if("BE".equalsIgnoreCase(topBudget.getIsbere())){
  	           CFinancialYear previousYearFor = budgetDetailHelper.getPreviousYearFor(topBudget.getFinancialYear());
  	           populateActualData(topBudget.getFinancialYear());
  	        }
  	        else
  	        	populateActualData(topBudget.getFinancialYear());
  	      if(LOGGER.isInfoEnabled())     LOGGER.info("Completed populateActualData...");
  	        computeActualAmounts(savedbudgetDetailList);
  	        if(LOGGER.isInfoEnabled())     LOGGER.info("finished loading detail List--------------------------------------------------------------");
  	        return    Constants.DETAILLIST;
  	    }
    @Override
    public void prepare() {
        super.prepare();
        if (parameters.containsKey("action") && Constants.MODIFY.equals(parameters.get("action")[0])) {
           if(budgetDetail.getId()!=null)
           {
        	   
           }else
           {
        	for (int i = 0; i < savedbudgetDetailList.size(); i++) {
                savedbudgetDetailList.set(i, budgetDetailService.findById(savedbudgetDetailList.get(i).getId(), false));
            }
           }
        }
        if(isApproveMode())
            dropdownData.put("budgetList", budgetDetailService.findBudgetsForFY(getFinancialYear()));
        else
            dropdownData.put("budgetList", budgetDetailService.findBudgetsForFYWithNewState(getFinancialYear()));
        EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
        addDropdownData("departmentList", masterCache.get("egi-department"));
        addDropdownData("designationList", Collections.EMPTY_LIST);
        addDropdownData("userList", Collections.EMPTY_LIST);
    }

    public String update(){
        Budget budget=null;
        Budget b=null;
        if(parameters.get("budget.id")!=null){
            budget=budgetService.find(" from Budget where id=?",Long.valueOf(parameters.get("budget.id")[0]));
            setTopBudget(budget);
        }
        if("forward".equalsIgnoreCase(parameters.get("actionName")[0]) || parameters.get("actionName")[0].contains("approve")){  //if mode is approve move the object
            approve();
        }else{           //if not approve then only update
            for (BudgetDetail detail : savedbudgetDetailList) {
                validateAmount(detail);
                if(consolidatedScreen)
                {
                	detail.setApprovedAmount(detail.getApprovedAmount().multiply(BigDecimal.valueOf(1000)));
                }
                budgetDetailService.persist(detail);
                b = detail.getBudget();
            }
            if(b!=null && b.getId()!=null){
                b = budgetService.find("from Budget where id=?",b.getId());
                if(b.getCurrentState()!=null)
                    b.getCurrentState().setText1(comments);
                budgetService.persist(b);
            }
            addActionMessage(getMessage("budgetdetail.updated"));
        }
        setBudgetDetail((BudgetDetail) session().get(Constants.SEARCH_CRITERIA_KEY));
        // Report block
        {
            Long count =(Long) persistenceService.find("select count(*) from Budget where materializedPath like ?",(topBudget.getMaterializedPath()+".%"));
            //if()
            //    financialYear=topBudget.getFinancialYear();
            if(count==0)
            {
                BudgetDetail detail =(BudgetDetail) persistenceService.find("from BudgetDetail where materializedPath like ?",(topBudget.getMaterializedPath()+".%"));
                if(detail!=null)
                {
                department = detail.getExecutingDepartment();
                }
            }
        showDetails=true;
        }

        return setUpDataForList();
    }

    private void validateAmount(BudgetDetail detail) {
        setEnablingAmounts();
        if(consolidatedScreen){
        	if(enableApprovedAmount && (detail.getApprovedAmount()==null || BigDecimal.ZERO.equals(detail.getApprovedAmount()))){
            loadApproverUser(savedbudgetDetailList);
            throw new ValidationException(Arrays.asList(new ValidationError("approved.amount.mandatory","approved.amount.mandatory")));
        	}
        }
        if(enableOriginalAmount && (detail.getOriginalAmount()==null || BigDecimal.ZERO.equals(detail.getOriginalAmount()))){
            loadApproverUser(savedbudgetDetailList);
            throw new ValidationException(Arrays.asList(new ValidationError("original.amount.mandatory","original.amount.mandatory")));
        }

    }

    public String setUpDataForList(){
        if(financialYear == null &&HibernateUtil.getCurrentSession().get(Constants.FINANCIALYEARID)!= null)
            financialYear = (Long)HibernateUtil.getCurrentSession().get(Constants.FINANCIALYEARID);
        dropdownData.put("budgetList", budgetDetailService.findBudgetsForFYWithNewState(financialYear==null?getFinancialYear():financialYear));
        return Constants.LIST;
    }

    private boolean isApproveMode() {
        return parameters.containsKey(Constants.MODE) && Constants.APPROVE.equals(parameters.get(Constants.MODE)[0]);
    }
    /**
     * move the budget detail and its parents depending on save or approve
     * @return
     */
    public void approve(){
        Integer userId = null;
        if( parameters.get(ACTIONNAME)[0] != null && parameters.get(ACTIONNAME)[0].contains("reject")){
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        }
        else if (null != parameters.get("approverUserId") &&  Integer.valueOf(parameters.get("approverUserId")[0])!=-1 ) {
            userId = Integer.valueOf(parameters.get("approverUserId")[0]);
        }else {
            userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
        }

        Position positionByUserId = eisCommonService.getPositionByUserId(userId);
        PersonalInformation empForCurrentUser = budgetDetailService.getEmpForCurrentUser();
        String name="";
		if(empForCurrentUser!=null)
        	name=empForCurrentUser.getName();
		if(name==null)
			name=empForCurrentUser.getEmployeeFirstName();
        if(LOGGER.isInfoEnabled())     LOGGER.info("===============Processing "+savedbudgetDetailList.size()+"Budget line items");
       if((parameters.get("actionName")[0]).contains("approv"))
       {
        for (BudgetDetail detail : savedbudgetDetailList) {
        	
            validateAmount(detail);
            if(consolidatedScreen)
            {
            	detail.setApprovedAmount(detail.getApprovedAmount().multiply(BigDecimal.valueOf(1000)));
            }
            String comment = detail.getState()==null?"":detail.getstate().getExtraInfo1();
          
           detail.changeState("END", positionByUserId, comment);
           budgetDetailService.persist(detail);
           BudgetDetail detailBE = (BudgetDetail)persistenceService.find("from BudgetDetail where id=?", detail.getNextYrId());
           if(consolidatedScreen)
           {
        	   detailBE.setApprovedAmount(detail.getNextYrapprovedAmount().multiply(BigDecimal.valueOf(1000)));
           }else{
           
           detailBE.setApprovedAmount(detail.getNextYrapprovedAmount());
           }
           detailBE.changeState("END", getPosition(), comment);
           budgetDetailService.persist(detailBE);
             
           
           
            //detail.getNextYearBEProposed
            //detail.getNextYearBEFixed
            //detailBE.changeState("END", positionByUserId, comment);
            
           // budgetDetailWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, detail, comment);
        }
       }else
       {
    	   for (BudgetDetail detail : savedbudgetDetailList) {
           	
               validateAmount(detail);
              if(consolidatedScreen)
               {
               	detail.setApprovedAmount(detail.getApprovedAmount().multiply(BigDecimal.valueOf(1000)));
               }
               String comment = detail.getState()==null?"":detail.getstate().getExtraInfo1();
             
               detail.changeState("Forwarded by "+name, positionByUserId, comment);
               
               budgetDetailService.persist(detail);  
               BudgetDetail detailBE = (BudgetDetail)persistenceService.find("from BudgetDetail where id=?", detail.getNextYrId());
              // detailBE.setOriginalAmount(detail.getOriginalAmount());
               if(consolidatedScreen)
               {
            	   detailBE.setApprovedAmount(detail.getNextYrapprovedAmount().multiply(BigDecimal.valueOf(1000)));
               }else
               {
               detailBE.setApprovedAmount(detail.getNextYrapprovedAmount());
               }
               detailBE.changeState("Forwarded by "+name, positionByUserId, comment);  
               budgetDetailService.persist(detailBE);
              // budgetDetailWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, detail, comment);
       }
       }
        if(LOGGER.isInfoEnabled())     LOGGER.info("Processed Budget line items");
        //if budget is not forwarded yet send the budget else ignore
       if(getTopBudget().getState().getOwnerPosition()!=null &&getTopBudget().getState().getOwnerPosition().getId()!= positionByUserId.getId())
       {
    	   getTopBudget().changeState("Forwarded by "+name, positionByUserId, comments);
    	   //add logic for BE approval also
    	   //
       }
     //add logic for BE approval also
	   Budget beBudget = budgetService.find("from Budget where referenceBudget=?",getTopBudget());
	   if(beBudget.getState().getOwnerPosition()!=null && beBudget.getState().getOwnerPosition().getId()!=positionByUserId.getId())
	   {
		   beBudget.changeState("Forwarded by "+name, positionByUserId, comments);
	   }
       
    	   // budgetWorkflowService.transition(parameters.get(ACTIONNAME)[0]+"|"+userId, getTopBudget(),comments);

        if((parameters.get("actionName")[0]).contains("approv")){
            if(topBudget.getState().getValue().equals("END")){
                addActionMessage(getMessage("budgetdetail.approved.end"));
            }else{
                addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(positionByUserId));
            }
        }else{
            addActionMessage(getMessage("budgetdetail.approved")+budgetService.getEmployeeNameAndDesignationForPosition(positionByUserId));
        }
        showButton=false;
    }

    public String ajaxDeleteBudgetDetail(){
        Long id = Long.valueOf(parameters.get("id")[0]);
        BudgetDetail detail = budgetDetailService.findById(id, false);
        budgetDetailService.delete(detail);
        BudgetDetail criteria = (BudgetDetail)HibernateUtil.getCurrentSession().get(Constants.SEARCH_CRITERIA_KEY);
        savedbudgetDetailList = budgetDetailService.searchBy(criteria);
        return Constants.MODIFYLIST;
    }
    public String budgetDetailApproveList(){
    	if(LOGGER.isInfoEnabled())     LOGGER.info("Starting budgetDetailApproveList...");
        isApproveAction=true;
        consolidatedScreen=budgetDetailService.toBeConsolidated();
      //  if(LOGGER.isInfoEnabled())     LOGGER.info("Budget.id "+parameters.get("budget.id")[0]);
        if(parameters.get("budget.id")!=null && parameters.get("budget.id")[0]!=null)
        {
            topBudget=budgetService.findById(Long.valueOf(parameters.get("budget.id")[0]), false);
           
            comments = topBudget.getstate().getExtraInfo1();
        }else if(parameters.get("budgetDetail.budget.id")[0]!=null)
        {
        	 topBudget=budgetService.findById(Long.valueOf(parameters.get("budgetDetail.budget.id")[0]), false);
        }
        comments = topBudget.getstate().getExtraInfo1();
        //budgetDetail=budgetDetailService.find("from BudgetDetail where budget=?",topBudget);
        savedbudgetDetailList =getAllApprovedBudgetDetails(topBudget);
        if(savedbudgetDetailList.size()>0)
        budgetDetail=savedbudgetDetailList.get(0);
        //budgetDetailService.findAllBudgetDetailsWithReAppropriation(topBudget, criteria);
        re = checkRe(topBudget);
        populateActualData(topBudget.getFinancialYear());
        computeActualAmounts(savedbudgetDetailList);
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished budgetDetailApproveList");
        return    Constants.DETAILLIST;
    }   

    private List<BudgetDetail> getAllApprovedBudgetDetails(Budget budget) {
		return budgetDetailService.findAllBy("from BudgetDetail where budget=? and (state.value='END' or state.owner=?) order by function.name,budgetGroup.name ",budget,getPosition());
	}

	private void computeActualAmounts(List<BudgetDetail> budgetDetails) {
		if(LOGGER.isInfoEnabled())     LOGGER.info("Starting computeActualAmounts .... ");
		budgetAmountView = new ArrayList<BudgetAmountView>();
		for (BudgetDetail detail : budgetDetails) {
			BudgetAmountView view = new BudgetAmountView();
			view.setId(detail.getId());
			String previousYearAmount = getPreviousYearBudgetDetailIdsAndAmount().get(detail.getUniqueNo());
			view.setPreviousYearActuals(previousYearAmount==null?BigDecimal.ZERO.setScale(2):new BigDecimal(previousYearAmount).setScale(2));
			String twopreviousYearAmount =getTwopreviousYearBudgetDetailIdsAndAmount().get(detail.getUniqueNo());
	        view.setTwopreviousYearActuals(twopreviousYearAmount==null?BigDecimal.ZERO.setScale(2):new BigDecimal(twopreviousYearAmount).setScale(2));
			String currentYearAmount = getBudgetDetailIdsAndAmount().get(detail.getUniqueNo());
			view.setCurrentYearBeActuals(currentYearAmount==null?BigDecimal.ZERO.setScale(2):new BigDecimal(currentYearAmount).setScale(2));
			budgetAmountView.add(view);
		   //	if(LOGGER.isInfoEnabled())     LOGGER.info(view);
			if(detail.getState()!=null)
				detail.setComment(detail.getstate().getExtraInfo1());
			BigDecimal approvedAmt = detail.getApprovedAmount()==null?BigDecimal.ZERO:detail.getApprovedAmount().setScale(2);
			if(re) {
				view.setCurrentYearReApproved(approvedAmt.setScale(2).toString());
				BigDecimal approvedReAppropriationsTotal = detail.getApprovedReAppropriationsTotal();
				BigDecimal lastBEAmount = getLastBE(detail);
				BigDecimal total = approvedReAppropriationsTotal.add(lastBEAmount);
				view.setReappropriation(approvedReAppropriationsTotal.setScale(2).toString());
				view.setLastBEApproved(total.setScale(2).toString());
				view.setLastTotal(total.setScale(2).toString());
			} else{
				view.setCurrentYearBeApproved(approvedAmt.setScale(2).toString());
				BigDecimal approvedReAppropriationsTotal = detail.getApprovedReAppropriationsTotal();
				BigDecimal lastBEAmount = getLastBE(detail);
				BigDecimal total = approvedReAppropriationsTotal.add(lastBEAmount);
				view.setReappropriation(approvedReAppropriationsTotal.toString());
				view.setLastBEApproved(lastBEAmount.toString());
				view.setLastTotal(total.setScale(2).toString());
			}
			detail.setAnticipatoryAmount(detail.getAnticipatoryAmount()==null?BigDecimal.ZERO:detail.getAnticipatoryAmount().setScale(2));
			detail.getOriginalAmount().setScale(2);
		}
		if(LOGGER.isInfoEnabled())     LOGGER.info("Done computeActualAmounts ");
	}

    /**
     * @param detail
     * @return
     */
    private BigDecimal getLastBE(BudgetDetail detail) {
        BudgetDetail detailWithoutBudget = new BudgetDetail();
        detailWithoutBudget.copyFrom(detail);
        detailWithoutBudget.setBudget(null);
        //List<Object[]> previousYearResult;
         CFinancialYear financialYear2 = detail.getBudget().getFinancialYear();
         Long finyearId=financialYear2.getId();
        if(detail.getBudget().getIsbere().equalsIgnoreCase("BE"))
        {
            Date startingDate = financialYear2.getStartingDate();
            Date lastyear = subtractYear(startingDate);
            CFinancialYear lastFinYear =(CFinancialYear) persistenceService.find("from CFinancialYear where startingDate=? and isActive=1",lastyear);
          if(lastFinYear!=null)
          {

              finyearId =lastFinYear.getId();
          }

        }
        BigDecimal approvedAmount=BigDecimal.ZERO;
        List<BudgetDetail> budgetDetail = budgetDetailService.searchByCriteriaWithTypeAndFY(finyearId,"BE",detailWithoutBudget);
            if(budgetDetail!=null && budgetDetail.size()>0)
            {
                approvedAmount = budgetDetail.get(0).getApprovedAmount();
            }
            return approvedAmount.setScale(2);

    }
    void populateActualData(CFinancialYear financialYear){
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting populate Actual data..... ");
        List<Object[]> beforePreviousYearResult,previousYearResult;
        if(financialYear==null) return;
        Budget lastYearTopBudget,beforeLastYearTopBudget,thirdLastYearTopBudget;
        BudgetDetail budgetDetailObj;
        String previousYearTopBudget,TwopreviousYearTopBudget;
        String fromDate = Constants.DDMMYYYYFORMAT1.format(financialYear.getStartingDate());
        List<AppConfigValues> list = new ArrayList<AppConfigValues>();
        list=getExcludeStatusForBudget();
        if(LOGGER.isInfoEnabled())     LOGGER.info("Starting fetchActualsForFY..... ");
        
        List<Object[]> result = budgetDetailService.fetchActualsForFinYear(financialYear,mandatoryFields,topBudget,null,null, defaultDept, null, list);
        if(LOGGER.isInfoEnabled())     LOGGER.info("Done fetchActualsForFY..... "+result.size());
        for (Object[] row : result) {
            getBudgetDetailIdsAndAmount().put(row[0].toString(), row[1].toString());
        }
        CFinancialYear lastFinancialYearByDate = getFinancialYearDAO().getPreviousFinancialYearByDate(financialYear.getStartingDate());
        CFinancialYear beforeLastFinancialYearByDate = getFinancialYearDAO().getTwoPreviousYearByDate(financialYear.getStartingDate());
        lastYearTopBudget=budgetService.find(" from Budget where financialYear.id=? and parent is null  and isbere=?",lastFinancialYearByDate.getId(),topBudget.getIsbere());
        beforeLastYearTopBudget=budgetService.find( "from Budget where financialYear.id=? and parent is null  and isbere=?",beforeLastFinancialYearByDate.getId(),topBudget.getIsbere());

        if(lastYearTopBudget!=null){
            previousYearResult = budgetDetailService.fetchActualsForFinYear(lastFinancialYearByDate,mandatoryFields,lastYearTopBudget,topBudget,null, defaultDept, null, list);
        }else{
            previousYearResult=new ArrayList<Object[]>();
        }
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished Fetching previous Year results");
            

        if(beforeLastYearTopBudget!=null){
        	beforePreviousYearResult = budgetDetailService.fetchActualsForFinYear(beforeLastFinancialYearByDate,mandatoryFields,beforeLastYearTopBudget,topBudget,null, defaultDept, null, list);
        }else{
        	 beforePreviousYearResult=new ArrayList<Object[]>();
        }
        	
        if(LOGGER.isInfoEnabled())     LOGGER.info("Finished Fetching before Last Year results");
        mapBudgetDetailForPreviousYear(savedbudgetDetailList,previousYearResult,beforePreviousYearResult);

        if(LOGGER.isInfoEnabled())     LOGGER.info("Ending populate Actual data. ");
    }
    
    public void mapBudgetDetailForPreviousYear(List<BudgetDetail> budgetdetail,List<Object[]> previousYearList,List<Object[]> beforelastYearList) {
        BudgetDetail budgetdt=new BudgetDetail();
        if(previousYearList.size()>0){
            for(Object[] row:previousYearList){
                            getPreviousYearBudgetDetailIdsAndAmount().put(row[0].toString().toString(),row[1].toString());
       
            }
        }
         if(beforelastYearList.size()>0){
            for(Object[] row:beforelastYearList){
        
                                getTwopreviousYearBudgetDetailIdsAndAmount().put(row[0].toString().toString(),row[1].toString());
            }     
        }
    }
    public boolean enableApprovedAmount(){
        return enableApprovedAmount;
    }
    public boolean enableOriginalAmount(){
        return enableOriginalAmount;
    }
    public void setBudgetDetailWorkflowService(
            SimpleWorkflowService<BudgetDetail> workflowService) {
        this.budgetDetailWorkflowService = workflowService;
    }

    public List<Action> getValidActions(){
        List<Action> validButtons=null;
        if(isReferenceBudget(getTopBudget())){
            if(LOGGER.isInfoEnabled())     LOGGER.info("Budget is Reference budget hence cannot be saved to sent for approval");
        }else{
            validButtons=budgetWorkflowService.getValidActions(getTopBudget());
        }
        return validButtons;

    }
    
    public void populateNextYrBEinBudgetDetailList(){
    	if(LOGGER.isInfoEnabled())     LOGGER.info("starting populateNextYrBEinBudgetDetailList");
    	if(!savedbudgetDetailList.isEmpty()){
    		for(BudgetDetail budgetDetail : savedbudgetDetailList){
    			BudgetDetail nextYrbudgetDetail = (BudgetDetail) persistenceService.find("from BudgetDetail where uniqueNo=? and budget.referenceBudget=?", budgetDetail.getUniqueNo(), budgetDetail.getBudget());
    			budgetDetail.setNextYrId(nextYrbudgetDetail.getId());
    			budgetDetail.setNextYroriginalAmount(nextYrbudgetDetail.getOriginalAmount());
    			budgetDetail.setNextYrapprovedAmount(nextYrbudgetDetail.getApprovedAmount());
    		}
    	}
    	if(LOGGER.isInfoEnabled())     LOGGER.info("Completed populateNextYrBEinBudgetDetailList");
    }

    private void setEnablingAmounts(){
        String value = (String) scriptService.executeScript( "BudgetDetail.enable.amounts", ScriptService.createContext("wfItem",topBudget,"persistenceService",budgetService));
        if("approved".equalsIgnoreCase(value))
            enableApprovedAmount = true;
        else if("original".equalsIgnoreCase(value))
            enableOriginalAmount = true;
    }
    /**
     * reference Budget is one which  exists in  the system but wont be having active and primary budget as parent
     * it is used for reference . These should be filtered for approval life cycle
     * @param budget
     */    

    public boolean isReferenceBudget(Budget budget) {
        boolean  isReference=false;
        if(budget==null)
        {
            isReference=false;
            return isReference;
        }
        String mPath=budget.getMaterializedPath();
        if(LOGGER.isInfoEnabled())     LOGGER.info("meterialized path for the Budget"+mPath);
        if(mPath==null || mPath.isEmpty())  //if null system is not expecting anything without materialized path
        {
            throw new EGOVRuntimeException("Materialized path is not set for the Budget "+budget.getName());
        }
        else if(budget.getIsPrimaryBudget() && budget.getIsActiveBudget()) //check for root budget if yes return
        {
            isReference=false;
            return isReference;
        }
        else{ //it should be some child
        int start=mPath.indexOf('.');
        if(start!=-1)
        {
            String rootPath = mPath.substring(0, start);
            if(LOGGER.isInfoEnabled())     LOGGER.info("meterialized path for root the Budget"+"   "+rootPath);
            Budget rootBudget = budgetService.find("from Budget where materializedPath=?",rootPath);
            if(rootBudget==null)
            {
                throw new EGOVRuntimeException("Materialized path is incorrect please verify for "+rootPath);
            }
            else
                if(rootBudget.getIsPrimaryBudget() && rootBudget.getIsActiveBudget())
                {
                    isReference=false;
                }
                else
                {
                    isReference=true;
                }
        }
        else
        {
            isReference=true;    //it is not root it is not child of any budget so it is reference
        }
        }
        return isReference;
    }

    public void setBudgetWorkflowService(WorkflowService<Budget> budgetWorkflowService) {
        this.budgetWorkflowService = budgetWorkflowService;
    }

    /*
     * validates the comments for length 1024
     *
     */
    public void validate()
    {
        for (BudgetDetail detail : savedbudgetDetailList) {
            if(detail.getComment()!=null && !(detail.getComment().trim()).isEmpty() && detail.getComment().length()>1024)
            {
                addFieldError("Comments Max Length  Exceeded BudgetDetail ",getText("budgetdetail.comments.lengthcheck"));}
        }
        if(parameters.get("budget.comments")!=null && parameters.get("budget.comments")[0]!=null && !parameters.get("budget.comments")[0].trim().isEmpty() &&  parameters.get("budget.comments")[0].length()>1024 )
        {
            addFieldError("Comments Max Length  Exceeded for  Budget",getText("budget.comments.lengthcheck"));}
    }

    @SkipValidation
    private void loadApproverUser(List<BudgetDetail> budgetDetailList)
    {
        EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
        Map<String, Object>  map = voucherService.getDesgBYPassingWfItem("BudgetDetail.nextDesg",null,budgetDetailList.get(0).getExecutingDepartment().getId());
        addDropdownData("departmentList", masterCache.get("egi-department"));

        List<Map<String,Object>> desgList  =  (List<Map<String,Object>>) map.get("designationList");
        String  strDesgId = "", dName = "";
        boolean bDefaultDeptId = false;
        List< Map<String , Object>> designationList = new ArrayList<Map<String,Object>>();
        Map<String, Object> desgFuncryMap;
        for(Map<String,Object> desgIdAndName : desgList) {
            desgFuncryMap = new HashMap<String, Object>();

            if(desgIdAndName.get("designationName") != null ) {
                desgFuncryMap.put("designationName",(String) desgIdAndName.get("designationName"));
            }

            if(desgIdAndName.get("designationId") != null ) {
                strDesgId = (String) desgIdAndName.get("designationId");
                if(strDesgId.indexOf("~") != -1) {
                    strDesgId = strDesgId.substring(0, strDesgId.indexOf('~'));
                    dName = (String) desgIdAndName.get("designationId");
                    dName = dName.substring(dName.indexOf('~')+1);
                    bDefaultDeptId = true;
                }
                desgFuncryMap.put("designationId",strDesgId);
            }
            designationList.add(desgFuncryMap);
        }
        map.put("designationList", designationList);

        addDropdownData("designationList", (List<DesignationMaster>)map.get("designationList"));
        if(bDefaultDeptId && !dName.equals("")) {
            Department dept = (Department) persistenceService.find("from Department where deptName like '%"+dName+"' ");
            defaultDept = dept.getId();
        }
        wfitemstate = map.get("wfitemstate")!=null?map.get("wfitemstate").toString():"";
    }

    private String wfitemstate;
    private VoucherService voucherService;
    private Integer defaultDept;
    private Department    department;
    public Integer getDefaultDept() {
        return defaultDept;
    }

    public void setDefaultDept(Integer defaultDept) {
        this.defaultDept = defaultDept;
    }

    public void setVoucherService(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public String getWfitemstate() {
        return wfitemstate;
    }

    public void setWfitemstate(String wfitemstate) {
        this.wfitemstate = wfitemstate;
    }

    public String capitalize(String value){
        if (value == null || value.length() == 0) return value;
        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }


    public Date subtractYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }
    public boolean isShowDetails() {
        return showDetails;
    }
    public void setShowDetails(boolean showDetails) {
        this.showDetails = showDetails;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department =department;
    }

    public BudgetSearchAndModify(BudgetDetailConfig budgetDetailConfig) {
        super(budgetDetailConfig);
    }

	public boolean isConsolidatedScreen() {
		return consolidatedScreen;
	}

	public void setConsolidatedScreen(boolean consolidatedScreen) {
		this.consolidatedScreen = consolidatedScreen;
	}

}
