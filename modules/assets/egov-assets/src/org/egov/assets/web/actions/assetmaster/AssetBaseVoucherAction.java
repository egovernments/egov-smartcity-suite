/**
 * 
 */
package org.egov.assets.web.actions.assetmaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.assets.model.Asset;
import org.egov.assets.model.AssetVoucherDetail;
import org.egov.assets.model.AssetVoucherHeader;
import org.egov.assets.service.AssetVoucherDetailService;
import org.egov.assets.service.AssetVoucherHeaderService;
import org.egov.assets.util.AssetConstants;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.services.financingsource.FinancingSourceService;
import org.egov.web.actions.BaseFormAction;

/**
 * @author manoranjan
 *
 */
public class AssetBaseVoucherAction extends BaseFormAction {

private static final long serialVersionUID = 1L;
	
	protected Asset asset = new Asset();
	private static final Logger	LOGGER	= Logger.getLogger(AssetBaseVoucherAction.class);
	protected List<String> headerFields = new ArrayList<String>();
	protected List<String> mandatoryFields = new ArrayList<String>();
	protected AssetVoucherHeader assetVH = new AssetVoucherHeader();
	protected AssetVoucherHeaderService assetVHService;
	protected AssetVoucherDetailService assetVDService;
	private FinancingSourceService financingSourceService;
	public AssetBaseVoucherAction(){
		addRelatedEntity("department", DepartmentImpl.class);
		addRelatedEntity("fund", Fund.class);
		addRelatedEntity("scheme", Scheme.class);
		addRelatedEntity("subscheme", SubScheme.class);
		addRelatedEntity("functionary", Functionary.class);
		addRelatedEntity("fundsource", Fundsource.class);
		addRelatedEntity("field", BoundaryImpl.class);
		addRelatedEntity("function", CFunction.class);
	}
	
	@Override
	public Object getModel() {
		return assetVH;
	}
	@Override
	public void prepare() {
		super.prepare();
		EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
		LOGGER.debug("Inside Prepare method");
		getHeaderMandateFields();
		if(headerFields.contains("department")){
			addDropdownData("departmentList", masterCache.get("egi-department"));
		}
		if(headerFields.contains("functionary")){
			addDropdownData("functionaryList", masterCache.get("egi-functionary"));
		}
		if(headerFields.contains("fund")){
			addDropdownData("fundList",  masterCache.get("egi-fund"));
		}
		if(headerFields.contains("fundsource")){
			addDropdownData("fundsourceList", masterCache.get("egi-fundSource"));
		}
		if(headerFields.contains("field")){
			addDropdownData("fieldList", masterCache.get("egi-ward"));
		}
		if(headerFields.contains("scheme")){
			addDropdownData("schemeList",  Collections.EMPTY_LIST );
		}
		if(headerFields.contains("subscheme")){
			addDropdownData("subschemeList", Collections.EMPTY_LIST);
		}
		addDropdownData("functionList", masterCache.get("egi-function"));
		
		addDropdownData("typeList", persistenceService.findAllBy(" select distinct vh.type from CVoucherHeader vh where vh.status!=4 order by vh.type"));
		LOGGER.debug("Number of  MIS attributes are :"+headerFields.size());
		LOGGER.debug("Number of mandate MIS attributes are :"+mandatoryFields.size());
		
	}
	protected void loadAjaxData(){
		LOGGER.debug("AssetBaseVoucherAction | Loading ajax data");
		if(headerFields.contains("scheme") && null != assetVH.getFund()){
			addDropdownData("schemeList", getPersistenceService().findAllBy(" from Scheme where fund=?",  assetVH.getFund()));
		}
		if(headerFields.contains("subscheme") && null !=  assetVH.getScheme()){
			addDropdownData("subschemeList", getPersistenceService().findAllBy("from SubScheme where scheme=? and isActive=1 order by name", assetVH.getScheme()));
		}
		if(headerFields.contains("fundsource") && null != assetVH.getSubscheme()){
			List<Fundsource> fundSourceList = financingSourceService.getFinancialSourceBasedOnSubScheme(assetVH.getSubscheme().getId());
			addDropdownData("fundsourceList", fundSourceList);
		}
	}
	protected void getHeaderMandateFields() {
		List<AppConfig> appConfigList = (List<AppConfig>) persistenceService.findAllBy("from AppConfig where key_name = 'DEFAULTTXNMISATTRRIBUTES'");
		for (AppConfig appConfig : appConfigList) {
			for (AppConfigValues appConfigVal : appConfig.getAppDataValues()) {
				String value = appConfigVal.getValue();
				String header=value.substring(0, value.indexOf("|"));
				headerFields.add(header);
				String mandate = value.substring(value.indexOf("|")+1);
				if(mandate.equalsIgnoreCase("M")){
					mandatoryFields.add(header);
				}
			}
		}
		mandatoryFields.add("voucherdate");
	}
	protected CVoucherHeader createVoucher(Long assetRefId){
		assetVH =assetVHService.find(" from AssetVoucherHeader where assetRefId ="+ assetRefId);
		CreateVoucher egfCreateVoucher = new CreateVoucher();
		HashMap<String, Object> headerdetails = createHeaderAndMisDetails(assetVH);
		List<HashMap<String,Object>> accountdetails = createVoucherDetails(assetVH.getId());
		List<HashMap<String,Object>> subledgerdetails = new ArrayList<HashMap<String,Object>>();
		CVoucherHeader voucherHeader = egfCreateVoucher.createVoucher(headerdetails, accountdetails, subledgerdetails);
		return voucherHeader;
	}
	
	private List<HashMap<String,Object>> createVoucherDetails(Long assetVHId){
		
		List<HashMap<String,Object>> accountdetails = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> detailMap;
		List<AssetVoucherDetail> listAssetVD = assetVDService.findAllBy(" from AssetVoucherDetail where assetVhId.id=?", assetVHId);
		for (AssetVoucherDetail assetVoucherDetail : listAssetVD) {
			detailMap = new HashMap<String, Object>();
			detailMap.put(VoucherConstant.CREDITAMOUNT, assetVoucherDetail.getCreditAmount());
			detailMap.put(VoucherConstant.DEBITAMOUNT, assetVoucherDetail.getDebitAmount());
			detailMap.put(VoucherConstant.GLCODE, assetVoucherDetail.getGlCodeId().getGlcode());
			if(null != assetVoucherDetail.getAssetVhId().getFunction()){
				detailMap.put(VoucherConstant.FUNCTIONCODE, assetVoucherDetail.getAssetVhId().getFunction().getCode());
			}
			accountdetails.add(detailMap);
		}
		return accountdetails;
	}
	private HashMap<String, Object> createHeaderAndMisDetails(AssetVoucherHeader assetVH) throws ValidationException{
		
		HashMap<String, Object> headerdetails = new HashMap<String, Object>();
		headerdetails.put(VoucherConstant.VOUCHERNAME, "JVGeneral");
		headerdetails.put(VoucherConstant.VOUCHERTYPE, "Journal Voucher");
		headerdetails.put(VoucherConstant.VOUCHERDATE, assetVH.getVoucherDate());
		headerdetails.put(VoucherConstant.DESCRIPTION, assetVH.getDescription());
		if(null != assetVH.getDepartment() )
			headerdetails.put(VoucherConstant.DEPARTMENTCODE, assetVH.getDepartment().getDeptCode());
		if(null != assetVH.getFund())
			headerdetails.put(VoucherConstant.FUNDCODE, assetVH.getFund().getCode());
		if(null != assetVH.getScheme())
			headerdetails.put(VoucherConstant.SCHEMECODE, assetVH.getScheme().getCode());
		if( null != assetVH.getSubscheme())
			headerdetails.put(VoucherConstant.SUBSCHEMECODE, assetVH.getSubscheme().getCode());
		if(null != assetVH.getFundsource())
			headerdetails.put(VoucherConstant.FUNDSOURCECODE,assetVH.getFundsource().getCode());
		if(null != assetVH.getField())
			headerdetails.put(VoucherConstant.DIVISIONID,assetVH.getField().getId());
		if(null!= assetVH.getFunctionary())
			headerdetails.put(VoucherConstant.FUNCTIONARYCODE,assetVH.getFunctionary().getCode());
		headerdetails.put(VoucherConstant.MODULEID, AssetConstants.MODULE_ID);
		headerdetails.put(VoucherConstant.SOURCEPATH, 
			"/egassets/assetrevaluation/assetRevaluationActivity!beforeView.action?actvId="+assetVH.getAssetRefId()+"&asset.id="+asset.getId());
		
		return headerdetails;
	}
	
	public boolean isFieldMandatory(String field){
		return mandatoryFields.contains(field);
	}
	public boolean shouldShowHeaderField(String field){
		
		return  headerFields.contains(field);
	}
	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public List<String> getHeaderFields() {
		return headerFields;
	}

	public List<String> getMandatoryFields() {
		return mandatoryFields;
	}

	public void setHeaderFields(List<String> headerFields) {
		this.headerFields = headerFields;
	}

	public void setMandatoryFields(List<String> mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}

	public AssetVoucherHeader getAssetVH() {
		return assetVH;
	}

	public void setAssetVH(AssetVoucherHeader assetVH) {
		this.assetVH = assetVH;
	}
	public void setAssetVHService(AssetVoucherHeaderService assetVHService) {
		this.assetVHService = assetVHService;
	}
	public void setAssetVDService(AssetVoucherDetailService assetVDService) {
		this.assetVDService = assetVDService;
	}

	public FinancingSourceService getFinancingSourceService() {
		return financingSourceService;
	}

	public void setFinancingSourceService(
			FinancingSourceService financingSourceService) {
		this.financingSourceService = financingSourceService;
	}
}
