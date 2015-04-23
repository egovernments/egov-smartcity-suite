
package org.egov.web.actions.masters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.commons.Bankaccount;
import org.egov.commons.FinancingInstitution;
import org.egov.commons.Fundsource;
import org.egov.commons.SharedFundSource;
import org.egov.commons.SubScheme;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.services.financingsource.FinancingSourceService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class FinancingSourceAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(FinancingSourceAction.class);
	private Fundsource  fundsource =  new Fundsource();
	private  List<String> fundingTypeList;
	private List<FinancingInstitution> finInstList;
	private  List<String> rePymntFrqList;
	private List<Fundsource> fundSourceList = new ArrayList<Fundsource>() ;
	private FinancingSourceService financingSourceService;
	private BigDecimal initialEstimateAmount;
	private List<Fundsource> finSrcTypOwnSrcList;
	private PersistenceService<SharedFundSource, Long> shrdFSrcPerSer;
	public FinancingSourceAction(){
		addRelatedEntity("bankAccountId",Bankaccount.class);
		addRelatedEntity("subSchemeId",SubScheme.class);
		addRelatedEntity("finInstId",FinancingInstitution.class);
	}
	@Override
	public Object getModel() {
		return fundsource;
	}
	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("subschemeList", (List<SubScheme>)persistenceService.findAllBy("from SubScheme where isactive=1 order by name"));
	    StringTokenizer sTokenizer = new StringTokenizer(getText("masters.finsrc.fundingtypes"), "|");
	    fundingTypeList = new ArrayList<String>();
	    while (sTokenizer.hasMoreElements()) {
			fundingTypeList.add( (String) sTokenizer.nextElement());
		}
	   finInstList = (List<FinancingInstitution>) persistenceService.findAllBy("from FinancingInstitution order by name");
	   StringTokenizer frqTokenizer = new StringTokenizer(getText("masters.finsrc.repymtfrq"), "|");
	   rePymntFrqList = new ArrayList<String>();
	    while (frqTokenizer.hasMoreElements()) {
	    	rePymntFrqList.add((String) frqTokenizer.nextElement());
		}
	   
	   addDropdownData("fundingTypeList", fundingTypeList);
	   addDropdownData("finInstList", finInstList);
	   addDropdownData("rePymntFrqList", rePymntFrqList);
	   addDropdownData("accNumList", Collections.EMPTY_LIST);
	   finSrcTypOwnSrcList = financingSourceService.getListOfSharedFinancialSource();
	   addDropdownData("finSrcTypOwnSrcList", finSrcTypOwnSrcList);
	   
	}
@Action(value="/masters/financingSource-newform")
	public String newform(){
		if(LOGGER.isDebugEnabled())     LOGGER.debug("FinancingSourceAction | newform | start");
		return "new";
	}
@Action(value="/masters/financingSource-getIntEstAmt")
	public String getIntEstAmt(){
		
		SubScheme subscheme =(SubScheme) persistenceService.find("from SubScheme  where id = "+ Integer.valueOf(parameters.get("subSchemeId")[0]));
		initialEstimateAmount = subscheme.getInitialEstimateAmount();
		if(LOGGER.isDebugEnabled())     LOGGER.debug(" initial estimate amount received = " +  initialEstimateAmount);
		if(null == subscheme.getInitialEstimateAmount()){
			initialEstimateAmount = BigDecimal.ZERO;
		}
		return "result";
	}
public String getOwnSrcAmount(){
	
	Fundsource fundsource= (Fundsource)persistenceService.find("from Fundsource where id="+Integer.valueOf(parameters.get("finSrcOwnSrcId")[0]));
	initialEstimateAmount = fundsource.getSourceAmount();
	if(LOGGER.isDebugEnabled())     LOGGER.debug(" initial estimate amount received = " +  initialEstimateAmount);
	if(null ==initialEstimateAmount){
		initialEstimateAmount = BigDecimal.ZERO;
	}
	
	return "result";
}
@Action(value="/masters/financingSource-codeUniqueCheck")
public String codeUniqueCheck(){
	
	return "codeUniqueCheck";
}
public boolean getCodeCheck(){
	
	boolean codeExistsOrNot = false;
	Fundsource fundsourceObj = (Fundsource)persistenceService.find("from Fundsource where code='"+fundsource.getCode()+"'");
	if(null != fundsourceObj ){
		codeExistsOrNot = true; 
	}
	return codeExistsOrNot;
}
@Action(value="/masters/financingSource-nameUniqueCheck")
public String nameUniqueCheck(){
	
	return "nameUniqueCheck";
}
public boolean getNameCheck(){
	
	boolean nameExistsOrNot = false;
	Fundsource fundsourceObj = (Fundsource)persistenceService.find("from Fundsource where name='"+fundsource.getName()+"'");
	if(null != fundsourceObj ){
		nameExistsOrNot = true; 
	}
	return nameExistsOrNot;
}
@ValidationErrorPage(value="new")
    public String save(){
    	if(LOGGER.isDebugEnabled())     LOGGER.debug("FinancingSourceAction | save | start");
    	if(LOGGER.isDebugEnabled())     LOGGER.debug("financial source list size "+ fundSourceList.size());
    	User user =(User)persistenceService.find("from User where id="+EGOVThreadLocals.getUserId());
    	SharedFundSource sharedFundSource;
    	try {
    			for (Fundsource fundsource : fundSourceList) {
    				if(fundsource.getType().equalsIgnoreCase("Shared Source")){
    					sharedFundSource = new SharedFundSource();
    					sharedFundSource.setSubSchemeId(fundsource.getSubSchemeId());
    					sharedFundSource.setFundSourceId(financingSourceService.findById(fundsource.getId(), false));
    					sharedFundSource.setAmount(fundsource.getSourceAmount());
    					shrdFSrcPerSer.persist(sharedFundSource);
    				}else{
    					fundsource = checkRelatedEntities(fundsource);
    					fundsource.setCreated(new Date());
        				fundsource.setCreatedBy(user);
        				financingSourceService.persist(fundsource);
    				}
    			
    			}
    			addActionMessage(getText("masters.finsrc.saved.sucess"));
		} catch (Exception e) {
			LOGGER.error("error occured while creating financial source"+e.getMessage(), e);
			 clearMessages();
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage(),e.getMessage()));
			 throw new ValidationException(errors);
		}
	
    	
    	return "new";
    }
private Fundsource  checkRelatedEntities(Fundsource fundsource){
	
	if(null == fundsource.getSubSchemeId().getId() ){
		fundsource.setSubSchemeId(null);
	}
	if(null == fundsource.getFinInstId().getId()){
		fundsource.setFinInstId(null);
	}
	if(null == fundsource.getBankAccountId().getId()){
		fundsource.setBankAccountId(null);
	}
	return fundsource;
}
	public Fundsource getFundsource() {
		return fundsource;
	}
	public void setFundsource(Fundsource fundsource) {
		this.fundsource = fundsource;
	}
	public List<String> getFundingTypeList() {
		return fundingTypeList;
	}
	public void setFundingTypeList(List<String> fundingTypeList) {
		this.fundingTypeList = fundingTypeList;
	}
	public List<FinancingInstitution> getFinInstList() {
		return finInstList;
	}
	public void setFinInstList(List<FinancingInstitution> finInstList) {
		this.finInstList = finInstList;
	}
	public List<String> getRePymntFrqList() {
		return rePymntFrqList;
	}
	public void setRePymntFrqList(List<String> rePymntFrqList) {
		this.rePymntFrqList = rePymntFrqList;
	}
	public List<Fundsource> getFundSourceList() {
		return fundSourceList;
	}
	public void setFundSourceList(List<Fundsource> fundSourceList) {
		this.fundSourceList = fundSourceList;
	}
	public FinancingSourceService getFinancingSourceService() {
		return financingSourceService;
	}
	public void setFinancingSourceService(
			FinancingSourceService financingSourceService) {
		this.financingSourceService = financingSourceService;
	}
	public BigDecimal getInitialEstimateAmount() {
		return initialEstimateAmount;
	}
	public void setInitialEstimateAmount(BigDecimal initialEstimateAmount) {
		this.initialEstimateAmount = initialEstimateAmount;
	}
	public List<Fundsource> getFinSrcTypOwnSrcList() {
		return finSrcTypOwnSrcList;
	}
	public void setFinSrcTypOwnSrcList(List<Fundsource> finSrcTypOwnSrcList) {
		this.finSrcTypOwnSrcList = finSrcTypOwnSrcList;
	}
	public void setShrdFSrcPerSer(
			PersistenceService<SharedFundSource, Long> shrdFSrcPerSer) {
		this.shrdFSrcPerSer = shrdFSrcPerSer;
	}
}
