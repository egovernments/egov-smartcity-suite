package org.egov.works.web.actions.capitaliseAsset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.assets.model.Asset;
import org.egov.assets.model.AssetType;
import org.egov.assets.model.CapitaliseAsset;
import org.egov.assets.service.CommonAssetsService;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.EgwStatus;
import org.egov.egf.commons.EgovCommon;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.works.models.contractorBill.AssetForBill;
import org.egov.works.services.ContractorBillService;
import org.egov.works.web.actions.contractorBill.ContractorBillAction;

@ParentPackage("egov")
public class CapitaliseAssetAction extends BaseFormAction{
	
	private static final Logger logger = Logger.getLogger(ContractorBillAction.class);
	private static final String SEARCH = "search";
	private static final String DETAILS = "details";
	private static final String ADMIN_HIERARCHY_TYPE = "ADMINISTRATION";
	private static final String Zone_BOUNDARY_TYPE = "Zone";
	private static final String Unable_To_Load_Heirarchy_Information="Unable to load Heirarchy information";
	private static final String Error_While_Loading_HeirarchyType="Error while loading HeirarchyType - HeirarchyType.";
	private static final String Error_While_Loading_Capitalisation_Details="Unable to load Capitalisation Details";
	private EmployeeService employeeService;
	private ContractorBillService contractorBillService;
	private CommonAssetsService commonAssetsService;
	private Long assetId;
	private Asset asset;
	private BigDecimal totalCapitalisationValue;
	private String xmlconfigname;
	private String categoryname;
	private Long parentId;
	private Long catTypeId;
	private Integer departmentId;	
	private List<Integer> statusId;
	private Integer zoneId;
	private String code;
	private String description;
	private Integer locationId; // its a wardId for search
	private Date fromDate;
	private Date toDate;
	private Date dateOfCapitalisation;
	private List<CapitaliseAsset> assetList = null;
	private List<AssetForBill> capitalisationDetails = null;
	private String remark;
	private PersonalInformation assetPreparedBy;
	private Integer preparedId;
	private EmployeeView assetPreparedByView;
	private PersonalInformationService personalInformationService;
	private Map<Integer,String> listProjectType ;
	private Integer projectType;
	private EgovCommon egovCommon;
	private BigDecimal totalIndrctExpns;
	private BigDecimal assetIndrctExpns;
	public CapitaliseAssetAction() {
		addRelatedEntity("assetType", AssetType.class);
		addRelatedEntity("department", DepartmentImpl.class);
	}

	@Override
	public void prepare() {
		AjaxCapitaliseAssetAction ajaxCapAction = new AjaxCapitaliseAssetAction();
		ajaxCapAction.setPersistenceService(getPersistenceService());
		ajaxCapAction.setEmployeeService(employeeService);
		ajaxCapAction.setPersonalInformationService(personalInformationService);
		super.prepare();
		setupDropdownDataExcluding("");
		addDropdownData("zoneList", getAllZone());
		addDropdownData("wardList", getWardList(zoneId));
		if(null == projectType || projectType.equals(-1)){
			
			addDropdownData("statusList", (List<EgwStatus>) persistenceService.findAllBy("from EgwStatus st where st.moduletype='ASSET' order by description"));
		}
		else{
			addDropdownData("statusList", getStatusListByDescs(getStatus(projectType)));
		}
		if(assetId!=null){
			try{
				asset = commonAssetsService.getAssetById(Long.valueOf(assetId));
				capitalisationDetails = contractorBillService.getAssetCapitalisationDetails(asset);
				totalCapitalisationValue = capitalisationValue(capitalisationDetails);
			}catch(Exception e){
				logger.error(Error_While_Loading_Capitalisation_Details + e.getMessage());
				addFieldError("asset", Error_While_Loading_Capitalisation_Details);
			}
		}
		
		populatePreparedByList(ajaxCapAction);
		setPreparedView(preparedId);
		setProjectType();// The options are Capitalisation, Improvement and Repairs and Maintenance	
	}
	
	private void  setProjectType(){
		
		listProjectType = new HashMap<Integer, String>();
		listProjectType.put(0, "Capitalisation");
		listProjectType.put(1, "Improvement");
		listProjectType.put(2, "Repairs and Maintenance");
	}
	
	protected void populatePreparedByList(AjaxCapitaliseAssetAction ajaxCapAction){
		PersonalInformation pi = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		Assignment ass= employeeService.getAssignmentByEmpAndDate(new Date(), pi.getIdPersonalInformation());
			ajaxCapAction.setDepartmentId(ass.getDeptId().getId());
			ajaxCapAction.usersInDepartment();
			addDropdownData("preparedByList", ajaxCapAction.getUsersInDepartment());
	}
	
	protected void setPreparedView(Integer idPersonalInformation) {
		if (validPreparedBy(idPersonalInformation)) {
			 assetPreparedByView = (EmployeeView) getPersistenceService().find("from EmployeeView where id = ?", idPersonalInformation);
		 }		 
	 }
	
	protected boolean validPreparedBy(Integer idPersonalInformation) {
		 if (idPersonalInformation != null && idPersonalInformation > 0) {
			 return true;
		 }
		 return false;
	 }
	
	private BigDecimal capitalisationValue(List<AssetForBill> capitalisationDetails){
		BigDecimal total = BigDecimal.ZERO;
		for(AssetForBill afb: capitalisationDetails){
			total = total.add(afb.getAmount());
		}
		return total;
	}
	
	public List<Boundary> getAllZone() {
	    HeirarchyType hType = null;
		try{	
			hType = new HeirarchyTypeDAO().getHierarchyTypeByName(ADMIN_HIERARCHY_TYPE);
		}catch(EGOVException e){
			logger.error(Error_While_Loading_HeirarchyType+ e.getMessage());
			throw new EGOVRuntimeException(Unable_To_Load_Heirarchy_Information,e);
		}
		List<Boundary> zoneList = null;
		BoundaryType bType = new BoundaryTypeDAO().getBoundaryType(Zone_BOUNDARY_TYPE,hType);
		zoneList = new BoundaryDAO().getAllBoundariesByBndryTypeId(bType.getId());
		return zoneList;
	}
	
	public List<Boundary> getWardList(Integer zoneId){
		List<Boundary> wardList = new ArrayList<Boundary>();
		if(zoneId!=null){
			try {
				wardList = new BoundaryDAO().getChildBoundaries(String.valueOf(zoneId));
			} catch (Exception e){
				logger.error("Error while loading wards - wards." + e.getMessage());
			}
		}
		return wardList;
	}
	
	@Override
	public Object getModel() {
		return null;
	}
	
	public String showSearch(){
		return SEARCH;
	}
	@ValidationErrorPage (value=SEARCH)
	public String list() throws ValidationException 
	{  
		if(projectType.equals(-1)){
			throw new ValidationException(Arrays.asList(new ValidationError("projectType",getText("asset.project.select"))));
		
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		setXmlconfigname(xmlconfigname);
	    setCategoryname(categoryname);
	    setCatTypeId(catTypeId);
		paramsMap.put("parentId", parentId);
		paramsMap.put("catTypeId", catTypeId);
		paramsMap.put("departmentId", departmentId);
		paramsMap.put("zoneId", zoneId);
		paramsMap.put("locationId", locationId);
		paramsMap.put("code", code);
		paramsMap.put("description", description);
		paramsMap.put("fromDate", fromDate);
		paramsMap.put("toDate", toDate);
		paramsMap.put("projectType",projectType );
		if(null == statusId){
			List<EgwStatus> statusList = getStatusListByDescs(getStatus(projectType));
			statusId= new ArrayList<Integer>(); 
			for (EgwStatus egwStatus : statusList) {
				statusId.add(egwStatus.getId());
			}
			paramsMap.put("statusId", statusId);
			
		}else{
			paramsMap.put("statusId", statusId);
		}
		
		assetList = contractorBillService.getAssetsForCapitalisationList(paramsMap);
		return SEARCH;  
	}

	public String showDetails() throws ValidationException,Exception{
		try{
		getIndirectExpenses();
		}
		catch(ValidationException ve){
			throw new ValidationException(Arrays.asList(new ValidationError("showDetails", ve.getMessage())));
		}
		
		return DETAILS;  
	}
	private void getIndirectExpenses() throws Exception{
		
		Map<String, String> assetProjCode = new HashMap<String, String>(); // keep details of multiple estimates for a single asset.
		 totalIndrctExpns  = BigDecimal.ZERO;
		for (AssetForBill assetForBill : capitalisationDetails) {
			
			String projEntityId = assetForBill.getWorkOrderEstimate().getEstimate().getProjectCode().getId().toString();
			String accCode = assetForBill.getCoa().getGlcode();
			if(null == assetProjCode.get(accCode)){
				assetProjCode.put(accCode,projEntityId );
			}else{
				projEntityId = assetProjCode.get(accCode)+","+projEntityId;
				assetProjCode.put(accCode,projEntityId );
			}
			
		}
		 Iterator<Entry<String, String>> mapIterator = assetProjCode.entrySet().iterator();
		 while (mapIterator.hasNext()) {
		        Map.Entry<String, String> pairs = (Map.Entry<String, String>)mapIterator.next();
		        CChartOfAccountDetail coaDetail  = (CChartOfAccountDetail) persistenceService.find("from CChartOfAccountDetail where  glCodeId.glcode='"+pairs.getKey()+"'");
		        if(null != coaDetail){
		        	
		        	totalIndrctExpns = totalIndrctExpns.add(egovCommon.getAccCodeBalanceForIndirectExpense(new Date(), pairs.getKey(), coaDetail.getDetailTypeId().getId(), pairs.getValue()));
		        }
		    	
		    }
	}
	@ValidationErrorPage(value="details")
	public String capitaliseAsset() 
	{  
		PersonalInformation emp = employeeService.getEmloyeeById(preparedId);
		try{
			
			asset = commonAssetsService.getAssetById(Long.valueOf(assetId));
			asset.setPreparedBy(emp);
			asset.setRemark(remark);
			assetIndrctExpns = assetIndrctExpns==null?BigDecimal.ZERO:assetIndrctExpns;
			contractorBillService.capitaliseAsset(asset, dateOfCapitalisation,assetIndrctExpns,projectType);
			
			addActionMessage(getText("asset.capitalise.success","The Asset was Capitalise successfully"));
		}catch(ValidationException ve){
			throw new ValidationException(Arrays.asList(new ValidationError("asset.capitalise.fail", "Asset capitalisation failed.")));
		}
		catch(Exception e){
			throw new ValidationException(Arrays.asList(new ValidationError("asset.capitalise.fail", "Asset capitalisation failed.")));
		}
		return "success";
	}
	
	private String[] getStatus(Integer projectTypeId){
		
		String[] status ;
		status = projectTypeId.equals(Integer.valueOf(0))?new String[]{"Created","CWIP"}:new String[]{"Capitalized"} ;
		return status;
	}
	@SuppressWarnings("unchecked")
	private List<EgwStatus> getStatusListByDescs(String[] statusDesc){
		
		StringBuffer sql = new StringBuffer(150);
		sql.append("from EgwStatus st where st.moduletype='ASSET'  and UPPER(st.description) in (");
		for (int i = 0; i < statusDesc.length; i++) {
			 sql.append("'" + statusDesc[i].trim().toUpperCase() + "'");
			 if(i<statusDesc.length-1 )sql.append(",");
		}
		
		sql.append(") order by description");
		String query = sql.toString();
		return  (List<EgwStatus>)persistenceService.findAllBy(query);
			 
		
	}
	
	public String getXmlconfigname() {
		return xmlconfigname;
	}

	public void setXmlconfigname(String xmlconfigname) {
		this.xmlconfigname = xmlconfigname;
	}

	public void setContractorBillService(ContractorBillService contractorBillService) {
		this.contractorBillService = contractorBillService;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getCatTypeId() {
		return catTypeId;
	}

	public void setCatTypeId(Long catTypeId) {
		this.catTypeId = catTypeId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public List<Integer> getStatusId() {
		return statusId;
	}

	public void setStatusId(List<Integer> statusId) {
		this.statusId = statusId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public List<CapitaliseAsset> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<CapitaliseAsset> assetList) {
		this.assetList = assetList;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public List<AssetForBill> getCapitalisationDetails() {
		return capitalisationDetails;
	}

	public void setCapitalisationDetails(List<AssetForBill> capitalisationDetails) {
		this.capitalisationDetails = capitalisationDetails;
	}

	public void setCommonAssetsService(CommonAssetsService commonAssetsService) {
		this.commonAssetsService = commonAssetsService;
	}

	public Date getDateOfCapitalisation() {
		return dateOfCapitalisation;
	}

	public void setDateOfCapitalisation(Date dateOfCapitalisation) {
		this.dateOfCapitalisation = dateOfCapitalisation;
	}

	public BigDecimal getTotalCapitalisationValue() {
		return totalCapitalisationValue;
	}

	public void setTotalCapitalisationValue(BigDecimal totalCapitalisationValue) {
		this.totalCapitalisationValue = totalCapitalisationValue;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public PersonalInformation getAssetPreparedBy() {
		return assetPreparedBy;
	}

	public void setAssetPreparedBy(PersonalInformation assetPreparedBy) {
		this.assetPreparedBy = assetPreparedBy;
	}

	public EmployeeView getAssetPreparedByView() {
		return assetPreparedByView;
	}

	public void setAssetPreparedByView(EmployeeView assetPreparedByView) {
		this.assetPreparedByView = assetPreparedByView;
	}

	public Integer getPreparedId() {
		return preparedId;
	}

	public void setPreparedId(Integer preparedId) {
		this.preparedId = preparedId;
	}
	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

	public Map<Integer, String> getListProjectType() {
		return listProjectType;
	}

	public void setListProjectType(Map<Integer, String> listProjectType) {
		this.listProjectType = listProjectType;
	}

	public Integer getProjectType() {
		return projectType;
	}

	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}

	public void setEgovCommon(EgovCommon egovCommon) {
		this.egovCommon = egovCommon;
	}

	public BigDecimal getTotalIndrctExpns() {
		return totalIndrctExpns;
	}

	public void setTotalIndrctExpns(BigDecimal totalIndrctExpns) {
		this.totalIndrctExpns = totalIndrctExpns;
	}

	public BigDecimal getAssetIndrctExpns() {
		return assetIndrctExpns;
	}

	public void setAssetIndrctExpns(BigDecimal assetIndrctExpns) {
		this.assetIndrctExpns = assetIndrctExpns;
	}

	

}
