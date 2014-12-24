/**
 * 
 */
package org.egov.assets.web.actions.assetreport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.assets.service.CommonAssetsService;
import org.egov.assets.util.AssetCommonUtil;
import org.egov.assets.util.RegisterReportBean;
import org.egov.assets.web.actions.assetmaster.AssetBaseSearchAction;
import org.egov.commons.EgwStatus;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQuerySQL;
import org.egov.web.utils.EgovPaginatedList;

/**
 * @author manoranjan
 *
 */
@ParentPackage("egov")
public class AssetRegisterReportAction extends AssetBaseSearchAction {

	private static final long serialVersionUID = 1L;
	private CommonAssetsService commonAssetsService ;
	private static final Logger LOGGER = Logger.getLogger(AssetRegisterReportAction.class);
	private static SimpleDateFormat formatteDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	private List<RegisterReportBean> listRegReport;
	@Override
	public void prepare() {
		super.prepare();
		List<EgwStatus> assetStatusList = commonAssetsService.getStatusListByDescs(new String[]{"Capitalized","Disposed"});
		addDropdownData("statusList",assetStatusList );
	}
	
	public String newform(){
		return "new";
	}
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		String[] query = getQuery();
		String reportQuery = query[0];
		if(null != sortField){
			reportQuery = reportQuery + "order by "+ sortField +" "+sortOrder;
		}
		return new SearchQuerySQL(reportQuery,"select count(*) from eg_asset asset where "+query[1],null);
	}

	private String[] getQuery() {
		LOGGER.debug(" AssetRegisterReportAction |getQuery |Start ");
		StringBuilder wherequery = new StringBuilder(600);
		wherequery.append(" asset.categoryid in "+"(select id from eg_assetcategory where assettype_id="+catTypeId+")");
		if(null != asset.getAssetCategory() && null!=asset.getAssetCategory().getId())
			wherequery.append(" and asset.categoryid in ("+ commonAssetsService.getAllChilds(asset.getAssetCategory().getId())+  ")");
		if(null != asset.getDepartment() && asset.getDepartment().getId() != -1)
			wherequery.append(" and asset.departmentid = "+ asset.getDepartment().getId());	
		if(zoneId!=null && zoneId !=-1 )
			wherequery.append(" and asset.ward_id in (select id_bndry from  eg_boundary where parent="+zoneId+")" );
		if(null != asset.getWard() && asset.getWard().getId() != -1)
			wherequery.append(" and asset.ward_id = "+asset.getWard().getId());
		if(null != asset.getStreet() && asset.getStreet().getId() != -1)
			wherequery.append(" and asset.street_id = "+asset.getStreet().getId());
		if(null != asset.getArea() && asset.getArea().getId() != -1)
			wherequery.append(" and asset.area_id = "+asset.getArea().getId());
		if(null != asset.getLocation() && asset.getLocation().getId() != -1)
			wherequery.append(" and asset.location_id = "+asset.getLocation().getId());
		if(null != asset.getName() && !asset.getName().trim().equalsIgnoreCase(""))
			wherequery.append(" and UPPER(asset.name) like '%" + asset.getName().toUpperCase()+ "%'");
		if(null!= asset.getDescription() && !asset.getDescription().trim().equalsIgnoreCase(""))
			wherequery.append(" and UPPER(asset.description) like '%" + asset.getDescription().toUpperCase()+ "%'");
		if(null!= asset.getStatus() && asset.getStatus().getId() != -1){
			wherequery.append(" and asset.statusid = "+asset.getStatus().getId()); 
		}else{
			List<EgwStatus> assetStatusList = commonAssetsService.getStatusListByDescs(new String[]{"Capitalized","Disposed"});
			wherequery.append(" and asset.statusid in ("); 
			wherequery.append(assetStatusList.get(0).getId()+","+assetStatusList.get(1).getId()+")");
		}
		
		StringBuffer reportquery = AssetCommonUtil.getAssetRegisterReportquery(wherequery.toString());
	
		
		
		String[] queryArr = new String[]{reportquery.toString(),wherequery.toString()};
		LOGGER.debug(" AssetRegisterReportAction |getQuery |query--->>> "+ queryArr[0]);
		return queryArr;
	}
	
	private void formatSearchResult(){
		
		EgovPaginatedList egovPaginatedList = (EgovPaginatedList)searchResult;
		List<Object[]> list = egovPaginatedList.getList();
		listRegReport = new ArrayList<RegisterReportBean>();
		for (Object[] object : list) {
			RegisterReportBean regReportBean = new RegisterReportBean();
			
			regReportBean.setAssetCode(object[0].toString());
			regReportBean.setAssetName(object[1].toString());
			regReportBean.setZoneName(object[2]!=null?object[2].toString():"");
			regReportBean.setWardName(object[3]!=null?object[3].toString():"");
			regReportBean.setAreaName(object[4]!=null?object[4].toString():"");
			regReportBean.setLocationName(object[5]!=null?object[5].toString():"");
			regReportBean.setStreetName(object[6]!=null?object[6].toString():"");
			regReportBean.setDeptName(object[7]!=null?object[7].toString():"");
			regReportBean.setStatus(object[8].toString());
			
			regReportBean.setCapAmount(object[10]!=null?new BigDecimal(object[10].toString()).setScale(2):null);
			regReportBean.setCapDate(object[11]!=null?formatteDDMMYYYY.format(object[11]):null);
			if(regReportBean.getCapAmount() != null){
				BigDecimal accDepVal = object[9]!=null?new BigDecimal(object[9].toString()):BigDecimal.ZERO;
				accDepVal = accDepVal.setScale(2);
				regReportBean.setWrittenDownVal((regReportBean.getCapAmount().subtract(accDepVal)).setScale(2));
				regReportBean.setAccDep(accDepVal);
			}
			
			
			listRegReport.add(regReportBean);
		}
		egovPaginatedList.setList(listRegReport);
	}
	public String list(){
		loadPreviousData();
		LOGGER.debug("AssetRegisterReportAction | list | Start");
		if(null == catTypeId){
			addActionError(getText("asset.category.id.null"));
			return "new";
		}
		search();
		formatSearchResult();
		return "new";
	}
	public CommonAssetsService getCommonAssetsService() {
		return commonAssetsService;
	}

	public void setCommonAssetsService(CommonAssetsService commonAssetsService) {
		this.commonAssetsService = commonAssetsService;
	}
	public List<RegisterReportBean> getListRegReport() {
		return listRegReport;
	}
	public void setListRegReport(List<RegisterReportBean> listRegReport) {
		this.listRegReport = listRegReport;
	}
	
}
