/**
 * 
 */
package org.egov.assets.web.actions.assetrevaluation;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.assets.model.Asset;
import org.egov.assets.service.CommonAssetsService;
import org.egov.assets.util.AssetCommonBean;
import org.egov.assets.util.AssetConstants;
import org.egov.assets.web.actions.assetmaster.AssetBaseSearchAction;
import org.egov.commons.EgwStatus;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.web.utils.EgovPaginatedList;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class AssetRevaluationSearchAction extends AssetBaseSearchAction {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AssetRevaluationSearchAction.class);
	private Asset asset = new Asset();
	private Date fromDate;
	private Date toDate;
	private CommonAssetsService commonAssetsService ;
	private List<AssetCommonBean> assetRevList;
	private AssetCommonBean assetCommonBean;

	public String newform(){
		return "new";
	}
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String[] queryArr = getQuery();
		if(null != sortField){
			queryArr[0]=(queryArr[0]+"order by "+sortField +" "+sortOrder);
		}else{
			queryArr[0]=(queryArr[0]+" order by asset.code");
		}
		return new SearchQueryHQL(queryArr[0],getCountQuery(queryArr[1]) , null);
	}
	private String getCountQuery(String whereQry){
		LOGGER.debug(" AssetRegisterReportAction |getQuery |Start ");
		StringBuilder sql = new StringBuilder(500);
		sql.append("select count(*) from Asset asset,AssetActivities assetact where asset.id = assetact.asset.id");
		sql.append(whereQry);
		return sql.toString();
	}

	private String[] getQuery() {
		LOGGER.debug(" AssetRegisterReportAction |getQuery |Start ");
		StringBuilder selectquery = new StringBuilder(600);
		StringBuilder wherequery = new StringBuilder(600);
		StringBuilder groupByquery = new StringBuilder(600);
		
		selectquery.append("select asset.code as assetcode,asset.name as assetname,assetact.activityDate as capitalizationdate,").
		append(" assetact.additionAmount as capitalizationamt,asset.createdDate as createddate,asset.id as assetid from Asset asset , AssetActivities assetact").
		append(" where asset.id = assetact.asset.id ");
		
		wherequery.append(" and  asset.assetCategory.assetType.id= "+catTypeId);
		List<EgwStatus> assetStatusList = commonAssetsService.getStatusListByDescs(new String[]{"Capitalized"});
		wherequery.append(" and asset.status.id in (").append(assetStatusList.get(0).getId()+")"); 
		wherequery.append(" and assetact.identifier = '"+"C'");
		if(null != asset.getAssetCategory() && null!=asset.getAssetCategory().getId())
			wherequery.append(" and asset.assetCategory.id in ("+ commonAssetsService.getAllChilds(asset.getAssetCategory().getId())+  ")");
		if(null != asset.getDepartment() && asset.getDepartment().getId() != -1)
			wherequery.append(" and asset.department.id = "+ asset.getDepartment().getId());	
		if(zoneId!=null && zoneId !=-1 )
			wherequery.append(" and asset.ward.parent.id = "+zoneId);
		if(null != asset.getWard() && asset.getWard().getId() != -1)
			wherequery.append(" and asset.ward.id = "+asset.getWard().getId());
		if(null != asset.getStreet() && asset.getStreet().getId() != -1)
			wherequery.append(" and asset.street.id = "+asset.getStreet().getId());
		if(null != asset.getArea() && asset.getArea().getId() != -1)
			wherequery.append(" and asset.area.id = "+asset.getArea().getId());
		if(null != asset.getLocation() && asset.getLocation().getId() != -1)
			wherequery.append(" and asset.location.id = "+asset.getLocation().getId());
		if(null != asset.getName() && !asset.getName().trim().equalsIgnoreCase(""))
			wherequery.append(" and UPPER(asset.name) like '%" + asset.getName().toUpperCase()+ "%'");
		if(null!= asset.getDescription() && !asset.getDescription().trim().equalsIgnoreCase(""))
			wherequery.append(" and UPPER(asset.description) like '%" + asset.getDescription().toUpperCase()+ "%'");
		if(null != fromDate){
			wherequery.append(" and asset.createdDate >= to_date('"+AssetConstants.DDMMYYYYFORMATH.format(fromDate)+"','dd/MM/yyyy')");
		}
		if(null != toDate){
			wherequery.append(" and asset.createdDate <= to_date('"+AssetConstants.DDMMYYYYFORMATH.format(toDate)+"','dd/MM/yyyy')");
		}
		String[] queryArr = new String[]{selectquery.toString()+wherequery.toString()+groupByquery.toString(),wherequery.toString()};
		LOGGER.debug(" AssetRevaluationAction |getQuery |query--->>> "+ queryArr[0]);
		return queryArr;
	}
	
	/**
	 * returns the list of assets for re-valuation.
	 * @return
	 */
		public String list(){
			validateBeforeSearch();
			if(hasFieldErrors()) return "new";
			setPageSize(AssetConstants.PAGE_SIZE);
			search();
			formatSearchResult();
			LOGGER.debug("AssetRevaluationAction | list | Start");
			return "new";
		}
	/**
	 * validate the search parameters while searching for the asset for re-valuation
	 */

	 public void validateBeforeSearch(){
		 loadPreviousData();
		if(null == catTypeId){
			addFieldError("asset.assetCategory.id",getText("asset.category.id.null"));	
		}
		if(null!= fromDate && null!= toDate && fromDate.after(toDate)){
			addFieldError("fromDate",getText("asset.search.date.validation"));	
		}
	 } 
	/**
	 * formatting the list of assets returns from query to populate in the screen using display tag.
	 */
private void formatSearchResult(){
		
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		List<Object[]> list = egovPaginatedList.getList();
		assetRevList = new ArrayList<AssetCommonBean>();
		for (Object[] object : list) {
			AssetCommonBean assetRevBean = new AssetCommonBean();
			assetRevBean.setAssetCode(object[0].toString());
			assetRevBean.setAssetName(object[1].toString());
			try {
				assetRevBean.setCapDate(AssetConstants.DDMMYYYYFORMATS.format(AssetConstants.YYYYMMDDFORMATH .parse(object[2].toString())));
				assetRevBean.setCreateDate(AssetConstants.DDMMYYYYFORMATS.format(AssetConstants.YYYYMMDDFORMATH .parse(object[4].toString())));
			} catch (ParseException e) {
				LOGGER.error("Exception Occured while Parsing capitalization date" + e.getMessage());
			}
			BigDecimal capAmount = new BigDecimal(object[3].toString()).setScale(2);
			assetRevBean.setCapAmount(capAmount);
			assetRevBean.setAssetId(Long.valueOf(object[5].toString()));
			assetRevList.add(assetRevBean);
		}
		egovPaginatedList.setList(assetRevList);
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
	public CommonAssetsService getCommonAssetsService() {
		return commonAssetsService;
	}

	public void setCommonAssetsService(CommonAssetsService commonAssetsService) {
		this.commonAssetsService = commonAssetsService;
	}

	public List<AssetCommonBean> getAssetRevList() {
		return assetRevList;
	}

	public void setAssetRevList(List<AssetCommonBean> assetRevList) {
		this.assetRevList = assetRevList;
	}

	public AssetCommonBean getAssetCommonBean() {
		return assetCommonBean;
	}

	public void setAssetCommonBean(AssetCommonBean assetCommonBean) {
		this.assetCommonBean = assetCommonBean;
	}

	

}
