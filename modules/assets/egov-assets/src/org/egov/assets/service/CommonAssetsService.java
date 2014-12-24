package org.egov.assets.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.pagination.PaginatedList;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.assets.model.Asset;
import org.egov.assets.model.AssetCategory;
import org.egov.assets.model.AssetOpeningBalance;
import org.egov.assets.model.AssetType;
import org.egov.assets.util.AssetConstants;
import org.egov.commons.EgwStatus;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.web.utils.EgovPaginatedList;
import org.hibernate.HibernateException;
import org.hibernate.Query;
/**
 * 
 * @author Nilesh
 *
 */
public class CommonAssetsService {
	
	private static final Logger LOGGER = Logger.getLogger(CommonAssetsService.class);
	protected AssetCategoryService assetCategoryService;
	protected AssetService assetService;
	protected PersistenceService genericService;
	
	// Create Asset parameters
	public static final String ASSET_ID = "ASSET_ID";												// Long
	public static final String ASSET_CODE = "ASSET_CODE"; 											// String
	public static final String ASSET_NAME = "ASSET_NAME";											// String
	public static final String ASSET_CATEGORY_ID = "ASSET_CATEGORY_ID";								// Long
	public static final String ASSET_AREA_ID = "ASSET_AREA_ID";										// Integer
	public static final String ASSET_LOCATION_ID = "ASSET_LOCATION_ID";								// Integer
	public static final String ASSET_STREET_ID = "ASSET_STREET_ID";									// Integer
	public static final String ASSET_WARD_ID = "ASSET_WARD_ID";										// Integer
	public static final String ASSET_DETAILS = "ASSET_DETAILS";										// String
	public static final String ASSET_MODE_OF_ACQUISITION = "ASSET_MODE_OF_ACQUISITION";				// String
	public static final String ASSET_COMMISSIONING_DATE = "ASSET_COMMISSIONING_DATE";				// Date
	public static final String ASSET_STATUS_ID = "ASSET_STATUS_ID";									// Integer					
	public static final String ASSET_GROSS_VALUE = "ASSET_GROSS_VALUE";								// BigDecimal
	public static final String ASSET_ACCUMULATIVE_DEPRECIATION = "ASSET_ACCUMULATIVE_DEPRECIATION";	// BigDecimal
	public static final String ASSET_WRITTEN_DOWN_VALUE = "ASSET_WRITTEN_DOWN_VALUE";				// BigDecimal
	public static final String ASSET_DESCRIPTION = "ASSET_DESCRIPTION";								// String
	public static final String ASSET_CATEGORY_CODE = "ASSET_CATEGORY_CODE";			                // String
	public static final String ASSET_CATEGORY_NAME = "ASSET_CATEGORY_NAME";		                    // String
	public static final String BOUNDARY_IMPL_QUERY="from BoundaryImpl where id=?";
	// Search Asset parameters
	public static final String ASSET_TYPE_ID = "ASSET_TYPE_ID";										// Long
	public static final String ASSET_DEPARTMENT_ID = "ASSET_DEPARTMENT_ID";							// Integer
	public static final String ASSET_STATUS_ID_LIST = "ASSET_STATUS_ID_LIST";						// List<Integer>
	public static final String ASSET_TYPE_NAME = "ASSET_TYPE_NAME";
	public static final String ASSET_DEPARTMENT_CODE="ASSET_DEPARTMENT_CODE";
	public static final String ASSET_WARD_NAME="ASSET_WARD_NAME";
	public static final String ASSET_STATUS_LIST="ASSET_STATUS_LIST";
	/**
	 * Find the <code>AssetCategory</code> by its ID. Return NULL if <code>AssetCategory</code> not found.
	 * @param categoryId
	 * @return <code>AssetCategory</code>
	 * @since v2.1
	 */
	public AssetCategory getAssetCategoryById(Long categoryId) 
	{
		AssetCategory assetCategory = null;
		try
		{
			assetCategory = assetCategoryService.find("from AssetCategory where id=?",categoryId);
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting AssetCategoryById.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting AssetCategoryById." + he.getMessage(), he);
		}
		
		return assetCategory;
	}
	/**
	 * Get all the List of <code>AssetCategory</code>.
	 * @return List<AssetCategory> - List of <code>AssetCategory</code> (Ordered by <tt>name</tt>)
	 * @since v2.1
	 */
	public List<AssetCategory> getAllAssetCategory()
	{
		List<AssetCategory> assetCategoryList = null;
		try
		{
			assetCategoryList = assetCategoryService.findAll("name");
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting AllAssetCategoryList.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting AllAssetCategoryList." + he.getMessage(), he);
		}
		
		return assetCategoryList;
	}
	
	/**
	 * Find the <code>AssetCategory</code> by Department ID. Returns NULL if <code>AssetCategory</code> not found.
	 * @param deptId
	 * @return <code>AssetCategory</code>
	 * @since v2.1
	 */
	public Asset getAssetByDeptId(Integer deptId)
	{
		Asset asset = null;
		try
		{
			String query = "from Asset ac where ac.department.id=?";
			asset = assetService.find(query, deptId);
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting getAssetByDeptId.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting getAssetByDeptId." + he.getMessage(), he);
		}
		
		return asset;
	}
	
	/**
	 * Find the <code>Asset</code> by its ID. Returns NULL if <code>Asset</code> not found.
	 * @param assetId
	 * @return <code>Asset</code>
	 * @since v2.1
	 */
	public Asset getAssetById(Long assetId)
	{
		Asset asset = null;
		try
		{
			asset = assetService.find("from Asset where id=?", assetId);
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting AssetById.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting AssetById." + he.getMessage(), he);
		}

		return asset;
	}
	/**
	 * Find the <code>Asset</code> by its Code. Returns NULL if <code>Asset</code> not found.
	 * @param code
	 * @return <code>Asset</code>
	 * @since v2.1
	 */
	public Asset getAssetByCode(String code)
	{
		Asset asset = null;
		try
		{			
			asset = assetService.find("from Asset where code=?", code);
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting AssetByCode.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting AssetByCode." + he.getMessage(), he);
		}
		
		return asset;
	}
	
	/**
	 * Find the list of <code>Asset</code> by category ID.
	 * @param categoryId
	 * @return <code>Asset</code>
	 * @since v2.1
	 */
	public List<Asset> getAssetsByCategoryId(Long categoryId)
	{
		List<Asset> assetList = null;
		try
		{
			String query = "from Asset a where a.assetCategory.id=? order by code";
			assetList = assetService.findAllBy(query, categoryId);
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting AssetsByCategoryId.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting AssetsByCategoryId." + he.getMessage(), he);
		}

		return assetList;
	}
	
	/**
	 * Find the <code>AssetCategory</code> by its Code. Returns NULL if <code>AssetCategory</code> not found.
	 * @param code
	 * @return <code>AssetCategory</code>
	 * @since v2.1
	 */
	public AssetCategory getAssetCategoryByCode(String code)
	{
		AssetCategory assetCategory = null;
		try
		{			
			assetCategory = assetCategoryService.find("from AssetCategory where code=?", code);
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting AssetCategoryByCode.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting AssetCategoryByCode." + he.getMessage(), he);
		}
		
		return assetCategory;
	}
	
	/**
	 * Get all the List of <code>Asset</code>.
	 * @return List<Asset> - List of <code>Asset</code> (Ordered by <tt>code</tt>)
	 * @since v2.1
	 */
	public List<Asset> getAllAssets()
	{
		List<Asset> assetList = null;
		try
		{
			assetList = assetService.findAll("code");
		}
		catch (HibernateException he)
		{
			LOGGER.error("Error while getting AllAssetList.");	
			throw new EGOVRuntimeException("Hibernate Exception : getting AllAssetList." + he.getMessage(), he);
		}
		
		return assetList;
	}
	
	/**
	 * Get all status for asset
	 * @return - List of all status for asset
	 */
	public List<EgwStatus> getAllAssetStatus(){
		String query = "from EgwStatus st where st.moduletype='ASSET' order by description";
		return (List<EgwStatus>) genericService.findAllBy(query);
	}
	
	/**
	 * API to create or update an Asset.
	 * Expecting a map which will have :
	 * @param ASSET_ID (optional) - Long - This param must be pass in case of update only. If its not passed the API will create a new asset.
	 * @param ASSET_CODE (mandatory) - String
	 * @param ASSET_NAME (mandatory) - String
	 * @param ASSET_CATEGORY_ID (mandatory) - Long
	 * @param ASSET_STATUS_ID (mandatory) - Integer					
	 * @param ASSET_AREA_ID (optional) - Integer
	 * @param ASSET_LOCATION_ID (optional) - Integer
	 * @param ASSET_STREET_ID (optional) - Integer
	 * @param ASSET_WARD_ID (optional) - Integer
	 * @param ASSET_DETAILS (optional) - String
	 * @param ASSET_MODE_OF_ACQUISITION (optional) - String
	 * @param ASSET_COMMISSIONING_DATE (optional) - Date
	 * @param ASSET_GROSS_VALUE (optional) - BigDecimal
	 * @param ASSET_ACCUMULATIVE_DEPRECIATION (optional) - BigDecimal
	 * @param ASSET_WRITTEN_DOWN_VALUE (optional) - BigDecimal
	 * @param ASSET_DESCRIPTION (optional) - String
	 * @param parameters - Map holding asset values 
	 * @return <code>Asset</code> - if the asset has create or update successfully otherwise <code>Null</code>. 
	 * @exception ValidationException, {@link ClassCastException}
	 */
	public Asset createOrUpdateAsset(Map<String,Object> parameters){

		Asset asset = new Asset();
		
		//**************************Update Logic***********************
		// Check for update or create asset
		if(parameters.get(ASSET_ID)!=null)
			asset = assetService.findById((Long)parameters.get(ASSET_ID), false);
		// Check if passed invalid AssetId for update
		if(asset==null)
			throw new ValidationException(Arrays.asList(new ValidationError("asset.id.doesNotExist","asset.id.doesNotExist")));
		//**************************Update Logic End***********************
		
		try{
			// Code (String) - Mandatory Field
			if(parameters.get(ASSET_CODE)==null && asset.getCode()==null)
				throw new ValidationException(Arrays.asList(new ValidationError("asset.code.mandatory","asset.code.mandatory")));
			else if (parameters.get(ASSET_CODE)!=null)
				asset.setCode((String)parameters.get(ASSET_CODE));
			
			// Name (String) - Mandatory Field
			if(parameters.get(ASSET_NAME)==null && asset.getName()==null)
				throw new ValidationException(Arrays.asList(new ValidationError("asset.name.mandatory","asset.name.mandatory")));
			else if (parameters.get(ASSET_NAME)!=null)
				asset.setName((String)parameters.get(ASSET_NAME));
			
			// Category Id (Long) - Mandatory Field
			if(parameters.get(ASSET_CATEGORY_ID)==null && asset.getAssetCategory()==null)
				throw new ValidationException(Arrays.asList(new ValidationError("asset.category.mandatory","asset.category.mandatory")));
			else if(parameters.get(ASSET_CATEGORY_ID)!=null){
			AssetCategory assetCategory = (AssetCategory)genericService.find("from AssetCategory where id=?",(Long)parameters.get(ASSET_CATEGORY_ID));
			if(assetCategory==null)
				throw new ValidationException(Arrays.asList(new ValidationError("asset.category.doesNotExist","asset.category.doesNotExist")));
			asset.setAssetCategory(assetCategory);
			}
			// Status Id (Integer) - Mandatory Field
			if(parameters.get(ASSET_STATUS_ID)==null && asset.getStatus()==null)
				throw new ValidationException(Arrays.asList(new ValidationError("asset.status.mandatory","asset.status.mandatory")));
			else if (parameters.get(ASSET_STATUS_ID)!=null){
				EgwStatus status = (EgwStatus)genericService.find("from EgwStatus where id=?",(Integer)parameters.get(ASSET_STATUS_ID));
			if(status==null)
				throw new ValidationException(Arrays.asList(new ValidationError("asset.status.doesNotExist","asset.status.doesNotExist")));
			asset.setStatus(status);
			if("Capitalized".equalsIgnoreCase(status.getDescription())){
				/*if(parameters.get(ASSET_GROSS_VALUE)!=null)
					asset.setGrossValue((BigDecimal)parameters.get(ASSET_GROSS_VALUE));
				if(parameters.get(ASSET_ACCUMULATIVE_DEPRECIATION)!=null)
					asset.setAccDepreciation((BigDecimal)parameters.get(ASSET_ACCUMULATIVE_DEPRECIATION));
				if(parameters.get(ASSET_WRITTEN_DOWN_VALUE)!=null)
					asset.setWrittenDownValue((BigDecimal)parameters.get(ASSET_WRITTEN_DOWN_VALUE));*/
			}
			}
			// Asset Description (String)
			if(parameters.get(ASSET_DESCRIPTION)!=null)
				asset.setDescription((String)parameters.get(ASSET_DESCRIPTION));
			
			// Area Id (Integer) - Mandatory Field
			if(parameters.get(ASSET_AREA_ID)!=null){
				Boundary area = (BoundaryImpl)genericService.find(BOUNDARY_IMPL_QUERY,(Integer)parameters.get(ASSET_AREA_ID));
				if(area==null)
					throw new ValidationException(Arrays.asList(new ValidationError("asset.area.doesNotExist","asset.area.doesNotExist")));
				asset.setArea(area);
			}
			
			// Location Id (Integer) - Mandatory Field
			if(parameters.get(ASSET_LOCATION_ID)!=null){
				Boundary location = (BoundaryImpl)genericService.find(BOUNDARY_IMPL_QUERY,(Integer)parameters.get(ASSET_LOCATION_ID));
				if(location==null)
					throw new ValidationException(Arrays.asList(new ValidationError("asset.location.doesNotExist","asset.location.doesNotExist")));
				asset.setLocation(location);
			}
			
			// Street Id (Integer) - Mandatory Field
			if(parameters.get(ASSET_STREET_ID)!=null){
				Boundary street = (BoundaryImpl)genericService.find(BOUNDARY_IMPL_QUERY,(Integer)parameters.get(ASSET_STREET_ID));
				if(street==null)
					throw new ValidationException(Arrays.asList(new ValidationError("asset.street.doesNotExist","asset.street.doesNotExist")));
				asset.setStreet(street);
			}
			
			// Ward Id (Integer) - Mandatory Field
			if(parameters.get(ASSET_WARD_ID)!=null){
				Boundary ward = (BoundaryImpl)genericService.find(BOUNDARY_IMPL_QUERY,(Integer)parameters.get(ASSET_WARD_ID));
				if(ward==null)
					throw new ValidationException(Arrays.asList(new ValidationError("asset.ward.doesNotExist","asset.ward.doesNotExist")));
				asset.setWard(ward);
			}
			
			// Asset Details (String)
			/*if(parameters.get(ASSET_DETAILS)!=null)
				asset.setAssetDetails((String)parameters.get(ASSET_DETAILS));*/
			
			// Mode Of Acquisition (String)
			if(parameters.get(ASSET_MODE_OF_ACQUISITION)!=null)
				asset.setModeOfAcquisition((String)parameters.get(ASSET_MODE_OF_ACQUISITION));
			
			// Commissioning Date (Date)
			/*if(parameters.get(ASSET_COMMISSIONING_DATE)!=null)
				asset.setCommDate((Date)parameters.get(ASSET_COMMISSIONING_DATE));*/
			
			asset = assetService.persist(asset);
			
		}catch(HibernateException he){
			LOGGER.error("Error while creating Asset through API.");	
			throw new EGOVRuntimeException("Hibernate Exception : in createAsset." + he.getMessage(), he);
		}
		
		return asset;
	}
	
	/**
	 * Search Assets.
	 * If the map is empty it will fetch all the assets.
	 * Expecting a map which will have :
	 * @param ASSET_CODE (optional) - String
	 * @param ASSET_TYPE_ID (optional) - Long
	 * @param ASSET_CATEGORY_ID (optional) - Long
	 * @param ASSET_DEPARTMENT_ID (optional) - Integer					
	 * @param ASSET_WARD_ID (optional) - Integer
	 * @param ASSET_STATUS_ID_LIST (optional) - List of Integer
	 * @param ASSET_DESCRIPTION (optional) - String
	 * @param parameters - Map holding asset search criteria 
	 * @return List of assets for given search criteria
	 * @exception ClassCastException
	 */
	public List<Asset> searchAssets(Map<String,Object> parameters){
		return assetService.findAllBy(getsearchAssetQuery(parameters));
	}
	
	/**
	 * 
	 * Search Assets page.
	 * 
	 * If the map is empty it will fetch asset page with.out ant filter.
	 * 
	 * Expecting a map which will have :
	 * @param ASSET_CODE (optional) - String
	 * @param ASSET_TYPE_ID (optional) - Long
	 * @param ASSET_CATEGORY_ID (optional) - Long
	 * @param ASSET_DEPARTMENT_ID (optional) - Integer					
	 * @param ASSET_WARD_ID (optional) - Integer
	 * @param ASSET_STATUS_ID_LIST (optional) - List of Integer
	 * @param ASSET_DESCRIPTION (optional) - String
	 * @param parameters - Map holding asset search criteria 
	 * @return List of assets for given search criteria
	 * @exception ClassCastException
	 * @return PaginatedList.
	 */
	public PaginatedList searchAssetPage(Map<String,Object> parameters,int pageNumber, int pageSize){
		String query = getsearchAssetQuery(parameters);
		Page page = assetService.findPageBy(query, pageNumber, pageSize);
		Long count = (Long)genericService.find("select count(*) " + query);
		return  new EgovPaginatedList(page, count.intValue());
	}

	/**
	    * Search Assets page.
	    * If the map is empty it will fetch asset page without any filter.
	    * Expecting a map which will have : * @param ASSET_NAME (optional) – String
	    * @param ASSET_CODE (optional) – it's free type search. Query is using  ' like clause' to search. 
	    * @param ASSET_TYPE_NAME (optional) – String (There can be three values :Land,MovableAsset,ImmovableAsset)
	    * @param ASSET_CATEGORY_CODE (optional) – String
	    * @param ASSET_DEPARTMENT_CODE (optional) – String
	    * @param ASSET_WARD_CODE (optional) – String
	    * @param ASSET_STATUS_LIST (optional) – List of String
	    * @param ASSET_DESCRIPTION (optional) – String
	    * @param parameters – Map holding asset search criteria
	    * @return List of assets for given search criteria
	    * @return PaginatedList.
	*/ 
	public PaginatedList findAssetPage(Map<String,Object> parameters,int pageNumber, int pageSize){
		String query = findAssetQuery(parameters);
		Page page = assetService.findPageBy(query, pageNumber, pageSize);
		Long count = (Long)genericService.find("select count(*) " + query);
		return  new EgovPaginatedList(page, count.intValue());
	}
	/**
	 * Formulate the query based on map parameters
	 * @param parameters
	 * @return search query
	 */
	private String findAssetQuery(Map<String,Object> parameters) {		
		StringBuilder sql = new StringBuilder(500);
		sql.append("from Asset asset where asset.code is not null ");
		
		if(parameters == null)
			return sql.toString();
		if(!StringUtils.isEmpty((String)parameters.get(ASSET_CODE)))
			sql.append(" and UPPER(asset.code) like '%" + ((String)parameters.get(ASSET_CODE)).toUpperCase()+ "%'");
		if(!StringUtils.isEmpty((String)parameters.get(ASSET_TYPE_NAME))){
			AssetType assetType = (AssetType) genericService.find(" from AssetType where upper(name)=?", ((String)parameters.get(ASSET_TYPE_NAME)).toUpperCase());
			if(assetType==null)
				throw new ValidationException("invalid.asset.type","Invalid Asset Type");
			else
				sql.append(" and asset.assetCategory.assetType.id = "+assetType.getId());
		}
		if(!StringUtils.isEmpty((String)parameters.get(ASSET_CATEGORY_CODE))){
			AssetCategory assetCategory = (AssetCategory) genericService.find(" from AssetCategory where upper(code)=?", ((String)parameters.get(ASSET_CATEGORY_CODE)).toUpperCase());
			if(assetCategory==null)
				throw new ValidationException("invalid.assetcategory.code","Invalid Assetcategory Code");
			else
				sql.append(" and asset.assetCategory.id = "+assetCategory.getId());
		}
		if(!StringUtils.isEmpty((String)parameters.get(ASSET_DEPARTMENT_CODE))){
			DepartmentImpl dept = (DepartmentImpl) genericService.find(" from DepartmentImpl where upper(deptCode)=?", ((String)parameters.get(ASSET_DEPARTMENT_CODE)).toUpperCase());
			if(dept==null)
				throw new ValidationException("invalid.department.code","Invalid Department Code");
			else
				sql.append(" and asset.department.id = "+dept.getId());
		}
		if(!StringUtils.isEmpty((String)parameters.get(ASSET_WARD_NAME))){
			BoundaryImpl boundary = (BoundaryImpl) genericService.find(" from BoundaryImpl where upper(name)=? and boundaryType=(select id from BoundaryTypeImpl where upper(name)=? and heirarchyType=(select id from HeirarchyTypeImpl where upper(name)=?))", ((String)parameters.get(ASSET_WARD_NAME)).toUpperCase(),"WARD","ADMINISTRATION");
			if(boundary==null)
				throw new ValidationException("invalid.ward.name","Invalid Ward Name");
			else
				sql.append(" and asset.ward.id = "+boundary.getId());
		}
		if(parameters.get(ASSET_DESCRIPTION)!=null && !((String)parameters.get(ASSET_DESCRIPTION)).trim().equalsIgnoreCase(""))
			sql.append(" and UPPER(asset.description) like '%" + ((String)parameters.get(ASSET_DESCRIPTION)).toUpperCase()+ "%'");
		
		if(parameters.get(ASSET_NAME)!=null && !((String)parameters.get(ASSET_NAME)).trim().equalsIgnoreCase(""))
		{
			sql.append(" and UPPER(asset.name) like '%" + ((String)parameters.get(ASSET_NAME)).toUpperCase()+ "%'");
		}
		if(parameters.get(ASSET_STATUS_LIST)!=null)
		{
			sql.append(" and asset.status.id in ("); 
			for(int j=0;j<((String[])parameters.get(ASSET_STATUS_LIST)).length;j++){
				EgwStatus status=(EgwStatus) genericService.find(" from EgwStatus where upper(moduletype)='ASSET' and upper(description)=?",((String[])parameters.get(ASSET_STATUS_LIST))[j].toUpperCase());
				if(status==null)
					throw new ValidationException("invalid.status.description","Invalid Status Description");
				else
				{
					sql.append(status.getId());
					if(j<((String[])parameters.get(ASSET_STATUS_LIST)).length-1){
						 sql.append(",");
					 }
				}
			}
			sql.append(")");	
		}
		return sql.toString();
	}
	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return PaginatedList
	 */
	public PaginatedList  getAssetCategoryPage(int pageNumber, int pageSize)
	{
		String query = getAssetCategorySearchQuery(null);
		Page page = assetCategoryService.findPageBy(query, pageNumber, pageSize);
		Long count = (Long)genericService.find("select count(*) " + query);
		return  new EgovPaginatedList(page, count.intValue());
	}
	
	/**
	 * 
	 * Expecting a map which will have :
	 * @param ASSET_CATEGORY_CODE
	 * @param ASSET_CATEGORY_NAME
	 * @return List<AssetCategory>
	 */
	public List<AssetCategory> searchAssetCategory(Map<String,Object> parameters)
	{
		return assetCategoryService.findAllBy(getAssetCategorySearchQuery(parameters));
	}
	
	/**
	 * Expecting a map which will have :
	 * @param ASSET_CATEGORY_CODE
	 * @param ASSET_CATEGORY_NAME
	 * @param parameters
	 * @param pageNumber
	 * @param pageSize
	 * @return PaginatedList
	 */
	public PaginatedList searchAssetCategoryPage(Map<String,Object> parameters,int pageNumber, int pageSize)
	{
		String query = getAssetCategorySearchQuery(parameters);
		Page page = assetCategoryService.findPageBy(query, pageNumber, pageSize);
		Long count = (Long)genericService.find("select count(*) " + query);
		return  new EgovPaginatedList(page, count.intValue());
	}

	/**
	 * Formulate the query based on map parameters
	 * @param parameters
	 * @return search query
	 */
	private String getsearchAssetQuery(Map<String,Object> parameters) {		
		StringBuilder sql = new StringBuilder(260);
		sql.append("from Asset asset where asset.code is not null ");
		
		if(parameters == null)
			return sql.toString();
		
		if(parameters.get(ASSET_CATEGORY_ID)!=null)
			sql.append(" and asset.assetCategory.id = "+ (Long)parameters.get(ASSET_CATEGORY_ID));
		if(parameters.get(ASSET_TYPE_ID)!=null)
			sql.append(" and asset.assetCategory.assetType.id = "+(Long)parameters.get(ASSET_TYPE_ID));
		if(parameters.get(ASSET_DEPARTMENT_ID)!=null)
			sql.append(" and asset.assetCategory.department.id = "+(Integer) parameters.get(ASSET_DEPARTMENT_ID));		
		if(parameters.get(ASSET_CODE)!=null && !((String)parameters.get(ASSET_CODE)).trim().equalsIgnoreCase(""))
			sql.append(" and UPPER(asset.code) like '%" + ((String)parameters.get(ASSET_CODE)).toUpperCase()+ "%'");
		if(parameters.get(ASSET_DESCRIPTION)!=null && !((String)parameters.get(ASSET_DESCRIPTION)).trim().equalsIgnoreCase(""))
			sql.append(" and UPPER(asset.description) like '%" + ((String)parameters.get(ASSET_DESCRIPTION)).toUpperCase()+ "%'");
		if(parameters.get(ASSET_WARD_ID)!=null)
			sql.append(" and asset.ward.id = "+(Integer) parameters.get(ASSET_WARD_ID));
		if(parameters.get(ASSET_STATUS_ID_LIST)!=null){
			List<Integer> statusIds = (List<Integer>)parameters.get(ASSET_STATUS_ID_LIST);
			if(!statusIds.isEmpty()){			
				sql.append(" and asset.status.id in ("); 
				 for(int i=0,len=statusIds.size(); i<len;i++){
					 sql.append(statusIds.get(i));
					 if(i<len-1){
						 sql.append(",");
					 }
				 }			 
				 sql.append(")");		
			}
		}

		return sql.toString();
	}
	/**
	 * Formulate the query based on map parameters
	 * @param parameters
	 * @return AssetCategorySerahcQuery
	 */
	private String getAssetCategorySearchQuery(Map<String,Object> parameters) {
		
		StringBuffer sql = new StringBuffer(270);
		sql.append("from AssetCategory assetcategory where assetcategory.code is not null ");
			
		if(parameters == null)
			return sql.toString();
		
		if(parameters.get(ASSET_CATEGORY_CODE)!=null && !((String)parameters.get(ASSET_CATEGORY_CODE)).trim().equalsIgnoreCase(""))
			sql.append(" and UPPER(assetcategory.code) like '%" + ((String)parameters.get(ASSET_CATEGORY_CODE)).toUpperCase()+ "%'");
		if(parameters.get(ASSET_CATEGORY_NAME)!=null && !((String)parameters.get(ASSET_CATEGORY_NAME)).trim().equalsIgnoreCase(""))
			sql.append(" and UPPER(assetcategory.name) like '%" + ((String)parameters.get(ASSET_CATEGORY_NAME)).toUpperCase()+ "%'");
		
		return sql.toString();
	}
	@SuppressWarnings("unchecked")
	public String getAllChilds(Long parentCatgId){
		 StringBuffer assetCatIdStr = new StringBuffer(100);
		 Query query = HibernateUtil.getCurrentSession().getNamedQuery("ParentChildCategories");
	     query.setParameter("assetcatId",parentCatgId);
	     List<Long> assetChildCategoryList = new LinkedList<Long>();
		 assetChildCategoryList=query.list();
	     for(int i=0,len=assetChildCategoryList.size(); i<len;i++){
	    	 assetCatIdStr.append(assetChildCategoryList.get(i).toString());
			 if(i<len-1)
				 assetCatIdStr.append(',');
		}
	     return assetCatIdStr.toString();
		}
	/**
	 * Get the list of <code>EgwStatus</code> related to ASSET module.
	 * @param statusDescList - List of status descriptions
	 * @return
	 */
	public  List<EgwStatus> getStatusListByDescs(String[] statusDesc){
		List<EgwStatus> lStatusList = null;
		
		StringBuffer sql = new StringBuffer(100);
		sql.append("from EgwStatus st where st.moduletype='ASSET'  and UPPER(st.description) in (");
		for (int i = 0; i < statusDesc.length; i++) {
			 sql.append("'" + statusDesc[i].trim().toUpperCase() + "'");
			 if(i<statusDesc.length-1 )sql.append(",");
		}
		
		sql.append(") order by description");
		String query = sql.toString();
		lStatusList = (List<EgwStatus>)genericService.findAllBy(query);
			 
		return lStatusList;
	}
	
	/**
	 * @description - returns the  value of a asset till a particular date.
	 * @param -tillDate the date till which the asset value need to be calculated.
	 *        - assetId the id of that asset 
	 * @return - value of asset till the date specified.
	 *  @throws ValidationException 
	 */
	
	public BigDecimal getAssetValueToDate(Long assetId,Date tillDate) throws ValidationException{
		
		if(null == assetId){
			throw new ValidationException(Arrays.asList(new ValidationError("assetId","assetId supplied is null")));
		}
		if(null == tillDate){
			throw new ValidationException(Arrays.asList(new ValidationError("tillDate","tillDate supplied is null")));
		}
		
		BigDecimal assetValue = BigDecimal.ZERO;
		AssetOpeningBalance assetOpeningBalance = getAssetOppBalance(assetId,tillDate);
		StringBuffer assetActQry = new StringBuffer(400); // query to calculate total amount of all the activities done on that asset
		assetActQry.append("select decode(sum(additionAmount),null,0,sum(additionAmount)) - decode(sum(deductionAmount),null,0,sum(deductionAmount) )").
		append(" from  AssetActivities where asset.id="+assetId ).
		append(" and activityDate <= to_date('"+AssetConstants.DDMMYYYYFORMATH.format(tillDate)+"','dd/MM/yyyy')" );
		if(null != assetOpeningBalance){
			assetActQry.append(" and activityDate >= to_date('"+AssetConstants.DDMMYYYYFORMATH.format(assetOpeningBalance.getFinancialYear().getStartingDate())+"','dd/MM/yyyy')" );
			assetValue = assetOpeningBalance.getGrossOpeningBalance().subtract(assetOpeningBalance.getDeductionOpeningBalance());
		}
		
		BigDecimal assetActVal  = BigDecimal.valueOf((Double)genericService.find(assetActQry.toString()));
		if(null != assetActVal)
		assetValue = assetValue.add(assetActVal); // opening balance amount  + total amount of activity done 
		
		StringBuffer assetDepQry = new StringBuffer(400);
		
		assetDepQry.append(" select sum(amount) from AssetDepreciation where asset.id="+assetId ).
		append(" and toDate <= to_date('"+AssetConstants.DDMMYYYYFORMATH.format(tillDate)+"','dd/MM/yyyy')");
		if(null != assetOpeningBalance){
			assetDepQry.append(" and fromDate >= to_date('"+AssetConstants.DDMMYYYYFORMATH.format(assetOpeningBalance.getFinancialYear().getStartingDate())+"','dd/MM/yyyy')" );
		}
		BigDecimal assetDepVal  = (BigDecimal)genericService.find(assetDepQry.toString());
		
		if(null !=assetDepVal )
		assetValue = assetValue.subtract(assetDepVal); // subtracted the asset depreciation done 
		assetValue = assetValue.setScale(2);
		return assetValue;
	}
	
	/**
	 * 
	 * @param assetId - asset id for which the opening balance to be fetched.
	 * @param tillDate - tillDate the date till which the asset opening balance need to be fetched.
	 * @return
	 */
	private AssetOpeningBalance getAssetOppBalance(Long assetId,Date tillDate){
		
		AssetOpeningBalance assetOpeningBalance = null;
		StringBuffer fyCountQry =  new StringBuffer(150);
		fyCountQry.append("select count(*) from CFinancialYear where startingDate <= to_date('").append(AssetConstants.DDMMYYYYFORMATH.format(tillDate)+"','dd/MM/yyyy')");
		Long fyYearCount = (Long)genericService.find(fyCountQry.toString());
		StringBuffer oppBalFyquery = new StringBuffer(200);
		oppBalFyquery.append("select id from (select id from  financialyear where startingDate <= to_date('").append(AssetConstants.DDMMYYYYFORMATH.format(tillDate)+"','dd/MM/yyyy')");
		oppBalFyquery.append(" order by startingDate desc ) group by rownum,id having rownum=");
		for (int i = 1; i <= fyYearCount; i++) {
			Long fyId = Long.valueOf(genericService.getSession().createSQLQuery(oppBalFyquery.toString()+i).list().get(0).toString());
			StringBuffer oppBalquery = new StringBuffer(200);
			oppBalquery.append(" from AssetOpeningBalance where asset.id="+assetId+" and financialYear.id="+fyId);
			assetOpeningBalance = (AssetOpeningBalance)genericService.find(oppBalquery.toString());
			// if opening balance exists for a  financial year(checking financial year in increasing order )then return that 
			// assetopeningbalance obj, else continue till get the latest entry for a financial year for that asset.
			if(null == assetOpeningBalance){
				continue;
			}else break;
		}
		return assetOpeningBalance;
	}
	// Setter for Services
	public void setAssetCategoryService(
			AssetCategoryService assetCategoryService) {
		this.assetCategoryService = assetCategoryService;
	}
	
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
	public void setGenericService(PersistenceService genericService) {
		this.genericService = genericService;
	}

	

}
