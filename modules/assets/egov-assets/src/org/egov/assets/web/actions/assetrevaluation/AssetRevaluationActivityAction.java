/**
 * 
 */
package org.egov.assets.web.actions.assetrevaluation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.assets.model.AssetRevaluation;
import org.egov.assets.model.AssetVoucherDetail;
import org.egov.assets.model.AssetVoucherHeader;
import org.egov.assets.service.AssetRevaluationService;
import org.egov.assets.service.CommonAssetsService;
import org.egov.assets.util.AssetCommonBean;
import org.egov.assets.util.AssetCommonUtil;
import org.egov.assets.util.AssetIdentifier;
import org.egov.assets.util.AssetRevaluationBean;
import org.egov.assets.web.actions.assetmaster.AssetBaseVoucherAction;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.web.annotation.ValidationErrorPage;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class AssetRevaluationActivityAction extends AssetBaseVoucherAction {

	private static final long serialVersionUID = 1L;
	private static final Logger	LOGGER	= Logger.getLogger(AssetRevaluationActivityAction.class);
	private static final String	APPROVER_USER_ID	= "approverUserId";
	private AssetRevaluationBean assetRevalBean;
	private CommonAssetsService commonAssetsService ;
	private AssetCommonBean assetCommonBean;
	private AssetRevaluationService  assetRevalService;
	private SimpleWorkflowService<AssetRevaluation> assetRevalWFService;
	private EisCommonsService eisCommonsService;
	private AssetRevaluation assetRevaluation = new AssetRevaluation();
	@Override
	public void prepare() {
		super.prepare();
		asset = commonAssetsService.getAssetById(asset.getId());
		addDropdownData("writtenoffacccodelist", getListOfFixedAssetWrnOffAccCode());
	    List<Map<String, String>> typOfChangeList = new ArrayList<Map<String, String>>(); // use to load the re-valuation type of change drop down 
		Map<String, String> mapInc = new HashMap<String, String>();
		Map<String, String> mapDec = new HashMap<String, String>();
		mapInc.put("type", "Increase");
		mapDec.put("type", "Decrease");
		typOfChangeList.add(mapInc);
		typOfChangeList.add(mapDec);
		addDropdownData("typOfChangeList",typOfChangeList);
	}

	@SuppressWarnings("unchecked")
	private List<CChartOfAccounts> getListOfFixedAssetWrnOffAccCode() {
		return (List<CChartOfAccounts>) persistenceService.findAllBy("from CChartOfAccounts where purposeId = (select id from EgfAccountcodePurpose where name=?) ", "Fixed Assets Written off");
	}
	/**
	 * loading the particular asset for re-valuation.
	 * @return
	 */
 public String beforeRevaluate(){
	 LOGGER.debug("AssetRevaluationActivityAction | beforeRevaluate | Start");
	 assetRevalBean = new AssetRevaluationBean();
	 assetCommonBean = new AssetCommonBean();
	 assetRevaluation = new AssetRevaluation();
	 assetCommonBean.setAssetValueAsOnDate(commonAssetsService.getAssetValueToDate(asset.getId(), new Date()));
	 assetRevalBean.setRevalDate(AssetCommonUtil.loadCurrentDate());
	 UserImpl user = (UserImpl)persistenceService.find(" from UserImpl where id="+Integer.valueOf(EGOVThreadLocals.getUserId()));
	 assetRevalBean.setRevaluatedBy(user.getUserName());
	 LOGGER.debug("AssetRevaluationActivityAction | beforeRevaluate | End");
 	 return "reVal";
 }

	
 @ValidationErrorPage(value="reVal")	
	public String create()  throws ValidationException{
	 LOGGER.debug("AssetRevaluationActivityAction | create | Start");
		try {
			validateData();
			addToAssetActivity();
			addToAssetVoucher(assetRevaluation);
			assetRevalWFService.start(assetRevaluation,getPosition());
			forwardAssetRevluation(assetRevaluation);
			beforeRevaluate();
			addActionMessage("Asset revaluation done successfully");
		}catch (ValidationException e) {
			loadAjaxData();
			clearMessages();
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getErrors().get(0).getMessage()));
			 throw new ValidationException(errors);
		} 
		catch (Exception e) {
			loadAjaxData();
			clearMessages();
			 List<ValidationError> errors=new ArrayList<ValidationError>();
			 errors.add(new ValidationError("exp",e.getMessage()));
			 throw new ValidationException(errors);
		}
		assetVH = new AssetVoucherHeader();
	return "reVal";
	}
/**
 *  validate data before creating the asset re-valuation
 */
  private void validateData(){
	  
	  if(assetRevalBean.getDescription().trim().length() > 250){
		  throw new ValidationException(Arrays.asList(new ValidationError("revalreason","Reason for revaluation text can not have more than 250 characters")));
	  }
  }
  /**
   * @description - sending the assetRevaluation object into the workflow.
   * @param assetRevaluation - the work flow item
   */
	private void forwardAssetRevluation(AssetRevaluation assetRevaluation){
	    LOGGER.debug("AssetRevaluationActivityAction | forwardAssetRevluation | Start");
		Integer userId = null;
		if(null != parameters.get(APPROVER_USER_ID) &&  Integer.valueOf(parameters.get(APPROVER_USER_ID)[0])!=-1)
		{
			userId = Integer.valueOf(parameters.get(APPROVER_USER_ID)[0]);
		}else 
		{
			userId = Integer.valueOf(EGOVThreadLocals.getUserId().trim());
		}
		
		LOGGER.debug("User selected id is : "+userId);
		assetRevaluation= assetRevalWFService.transition(parameters.get("actionName")[0]+"|"+userId, assetRevaluation,parameters.get("comments")[0]);
		if("END".equalsIgnoreCase(assetRevaluation.getState().getValue())){
			createVoucherAndAttachToAsset(assetRevaluation.getId());
		}
		addActionMessage(" The file is finally approved");
		LOGGER.debug("AssetRevaluationActivityAction | forwardAssetRevluation | End");
	}
	/**
	 * @description - when the final user approves the asset re-valuation then create the voucher and attach the same
	 *	to the same voucher object to  asset re-valuation object.
	 * @param assetRevalId
	 */
	private void createVoucherAndAttachToAsset(Long assetRevalId){
		
		LOGGER.debug("AssetRevaluationActivityAction | createVoucherAndUpdateActivity | Start");
		AssetRevaluation assetRevaluation = assetRevalService.find("from AssetRevaluation where id="+assetRevalId );
		CVoucherHeader voucherHeader = createVoucher(assetRevalId);
		assetRevaluation.setVoucherHeader(voucherHeader);
		assetRevalService.update(assetRevaluation);
		LOGGER.debug("AssetRevaluationActivityAction | createVoucherAndUpdateActivity | End");
	}
	/**
	 * 
	 * @param assetRevaluation
	 */
	private void addToAssetVoucher(AssetRevaluation assetRevaluation){
		LOGGER.debug("AssetRevaluationActivityAction | addToAssetVoucher | Start");
		assetVH.setAssetRefId(assetRevaluation.getId());
		assetVH.setVoucherDate(assetRevaluation.getActivityDate());
		assetVH.setDescription(assetRevaluation.getDescription());
		assetVH = assetVHService.persist(assetVH);
		AssetVoucherDetail assetVDDb = new AssetVoucherDetail();
		AssetVoucherDetail assetVDCr = new AssetVoucherDetail();
		assetVDDb.setAssetVhId(assetVH);
		assetVDCr.setAssetVhId(assetVH);
		if("Increase".equalsIgnoreCase(assetRevalBean.getTypeOfChange())){
			assetVDDb.setCreditAmount(BigDecimal.ZERO);
			assetVDDb.setDebitAmount(assetRevalBean.getRevalAmt());
			assetVDDb.setGlCodeId(asset.getAssetCategory().getAssetCode());
			
			assetVDCr.setCreditAmount(assetRevalBean.getRevalAmt());
			assetVDCr.setDebitAmount(BigDecimal.ZERO);
			assetVDCr.setGlCodeId(asset.getAssetCategory().getRevCode());
			
		}else if("Decrease".equalsIgnoreCase(assetRevalBean.getTypeOfChange())){
			
			assetVDDb.setCreditAmount(BigDecimal.ZERO);
			assetVDDb.setDebitAmount(assetRevalBean.getRevalAmt());
			assetVDDb.setGlCodeId((CChartOfAccounts)persistenceService.find("from CChartOfAccounts where id="+assetRevalBean.getWrtnOffAccCodeId()));
			
			assetVDCr.setCreditAmount(assetRevalBean.getRevalAmt());
			assetVDCr.setDebitAmount(BigDecimal.ZERO);
			assetVDCr.setGlCodeId(asset.getAssetCategory().getAssetCode());
		}
		
		assetVDService.persist(assetVDDb);
		assetVDService.persist(assetVDCr);
		LOGGER.debug("AssetRevaluationActivityAction | addToAssetVoucher | End");
	}
	/**
	 * @description - persist data to asset activity table.
	 */
	private AssetRevaluation addToAssetActivity() {
		
		LOGGER.debug("AssetRevaluationActivityAction | addToAssetActivity | Start");
		assetRevaluation.setAsset(asset);
		assetRevaluation.setActivityDate(assetRevalBean.getRevalDate());
		if("Increase".equalsIgnoreCase(assetRevalBean.getTypeOfChange())){
			assetRevaluation.setAdditionAmount(assetRevalBean.getRevalAmt());
		}else if("Decrease".equalsIgnoreCase(assetRevalBean.getTypeOfChange())){
			assetRevaluation.setDeductionAmount(assetRevalBean.getRevalAmt());
		}
		assetRevaluation.setDescription(assetRevalBean.getDescription());
		assetRevaluation.setIdentifier(AssetIdentifier.R);
		EgwStatus status = (EgwStatus)persistenceService.find(" from EgwStatus where moduletype='"+"ASSETREVALUATION"+"' and description='"+"RevaluataionCreated"+"'");
		assetRevaluation.setStatus(status);
		assetRevalService.persist(assetRevaluation);
		LOGGER.debug("AssetRevaluationActivityAction | addToAssetActivity | End");
		return assetRevaluation;
	}
	/**
	 * 
	 * @return
	 */
	public String beforeView(){
		loadDataForView();
		return "view";
	}
	/**
	 * 
	 */
	private void loadDataForView(){
		assetRevalBean = new AssetRevaluationBean();
		assetCommonBean = new AssetCommonBean();
		assetRevaluation = assetRevalService.find(" from AssetRevaluation where id="+Long.valueOf(parameters.get("actvId")[0]));
		assetVH = assetVHService.find("from AssetVoucherHeader where assetRefId="+assetRevaluation.getId());
		BigDecimal assetValAsOnDate = commonAssetsService.getAssetValueToDate(asset.getId(), assetRevaluation.getActivityDate());
		if(null != assetRevaluation.getAdditionAmount()){
			assetRevalBean.setTypeOfChange("Increase");
			assetRevalBean.setRevalAmt(assetRevaluation.getAdditionAmount());
			assetCommonBean.setAssetValueAsOnDate(assetValAsOnDate.subtract(assetRevaluation.getAdditionAmount()));
		}else if(null !=  assetRevaluation.getDeductionAmount()){
			assetRevalBean.setTypeOfChange("Decrease");
			assetRevalBean.setRevalAmt(assetRevaluation.getDeductionAmount());
			assetCommonBean.setAssetValueAsOnDate(assetValAsOnDate.add(assetRevaluation.getDeductionAmount()));
			AssetVoucherDetail assetVD =assetVDService.find(" from AssetVoucherDetail where creditAmount=0 and assetVhId.id="+assetVH.getId());
			assetRevalBean.setWrtnOffAccCodeId(assetVD.getGlCodeId().getId());
		}
		assetRevalBean.setValAfterReval(assetValAsOnDate);
		assetRevalBean.setRevalDate(assetRevaluation.getActivityDate());
		assetRevalBean.setRevaluatedBy(assetRevaluation.getCreatedBy().getUserName());
		assetRevalBean.setDescription(assetRevaluation.getDescription());
	
		loadAjaxData();
	}
	
	public Position getPosition()throws EGOVRuntimeException
	{
		return  eisCommonsService.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
	}
	public AssetRevaluationBean getAssetRevalBean() {
		return assetRevalBean;
	}

	public void setAssetRevalBean(AssetRevaluationBean assetRevalBean) {
		this.assetRevalBean = assetRevalBean;
	}

	public CommonAssetsService getCommonAssetsService() {
		return commonAssetsService;
	}

	public void setCommonAssetsService(CommonAssetsService commonAssetsService) {
		this.commonAssetsService = commonAssetsService;
	}

	public AssetCommonBean getAssetCommonBean() {
		return assetCommonBean;
	}

	public void setAssetCommonBean(AssetCommonBean assetCommonBean) {
		this.assetCommonBean = assetCommonBean;
	}
	public AssetVoucherHeader getAssetVH() {
		return assetVH;
	}

	public void setAssetVH(AssetVoucherHeader assetVH) {
		this.assetVH = assetVH;
	}

	public void setAssetRevalService(AssetRevaluationService assetRevalService) {
		this.assetRevalService = assetRevalService;
	}

	public void setEisCommonsService(EisCommonsService eisCommonsService) {
		this.eisCommonsService = eisCommonsService;
	}

	public void setAssetRevalWFService(
			SimpleWorkflowService<AssetRevaluation> assetRevalWFService) {
		this.assetRevalWFService = assetRevalWFService;
	}

	public AssetRevaluation getAssetRevaluation() {
		return assetRevaluation;
	}

	public void setAssetRevaluation(AssetRevaluation assetRevaluation) {
		this.assetRevaluation = assetRevaluation;
	}
	
	
}
