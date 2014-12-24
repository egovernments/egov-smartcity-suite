package org.egov.works.web.actions.tender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.Page;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.web.actions.SearchFormAction;
import org.egov.web.utils.EgovPaginatedList;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.TenderFile;
import org.egov.works.services.TenderResponseClient;
import org.hibernate.Query;

public class SearchTenderFileAction extends SearchFormAction{

	
	private TenderFile tenderFile=new TenderFile();
	private String searchType;
	private String status;
	private String fileNum;
	private String noticeNumber;
	private String mode;
	private Long estimateId;
	//private String TENDERORQUOTATION="TenderOrQuotation";
	//private String RATECONTRACT="RateContract";
	private String SEARCH		 =	"search";
	private String VIEW			 =  "view";
	private String KEY_TENDERFILE=  "TenderFile";
	private Map<String,Object> criteriaMap=null;
	private GenericTenderService genericTenderService;
	private CommonsService commonsService;
	private TenderResponseClient tenderResponseClient;
	private String pullDataForeTenderingFlag;
	private EgovPaginatedList pagedResults;
	
	
	@Override
	public Object getModel() {
		return tenderFile;
	}
	
	@Override
	public void prepare(){
		super.prepare();
		
	}
	
	public String search(){	
		  return SEARCH;
	}
	
	public String edit(){
		return VIEW;
	}
	
	public String execute(){
		return searchTenderFile();
	}
	
	public List<AbstractEstimate> getEstimatesList(){
		List<AbstractEstimate> estimateList=new ArrayList<AbstractEstimate>();
		//estimateList=(List<AbstractEstimate>)persistenceService.findAllBy("select abstractEstimate from TenderFileDetail");
		estimateList=Collections.EMPTY_LIST;
		return estimateList;
	}
	
	
	public List<EgwStatus> getTenderFileStatuses() {
		List<EgwStatus> statusList = commonsService.getStatusByModule(KEY_TENDERFILE);
		List<EgwStatus> latestStatusList = new ArrayList<EgwStatus>();
		if(!statusList.isEmpty()){
			for(EgwStatus egwStatus : statusList){
				if(egwStatus.getCode().equals("APPROVED")){
					latestStatusList.add(egwStatus);
				}
			}
		}
		return latestStatusList;
	}
	
	public String searchTenderFile(){
		//Commented because of Oracle Error : ORA-01795
		/*		criteriaMap = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(status) && !getStatus().equals("-1"))	{
			criteriaMap.put("STATUS",status);
		}
		if(StringUtils.isNotBlank(getFileNum()))
			criteriaMap.put("FILENUMBER",fileNum);
		if(StringUtils.isNotBlank(noticeNumber))
			criteriaMap.put("NOTICENUMBER",noticeNumber);
		if(estimateId!=null && estimateId!=-1)
			criteriaMap.put("ESTIMATEID",estimateId);

		super.search();
		setPageSize(searchResult.getFullListSize());
		super.search();
		if(searchResult.getFullListSize()>0){
		List<TenderFile> results=searchResult.getList();
		List trIdList=new ArrayList();
			for(TenderFile tf:results){
				for(TenderFileDetail tfd:tf.getTenderFileDetails()){
					for(GenericTenderResponse tr:genericTenderService.getAcceptedTenderResponse(tfd.getAbstractEstimate()))
						trIdList.add(tr.getId());
				}

			}
			if(trIdList.size()>0){
				String query="select gtr from org.egov.tender.model.GenericTenderResponse gtr where gtr.id in(:trIdList) and " +
				"gtr.number not in (select wo.negotiationNumber from WorkOrder wo where wo.negotiationNumber is not null and wo.state.previous is null and wo.state.value='NEW')" +
				" and gtr.number not in (select wo.negotiationNumber from WorkOrder wo where wo.negotiationNumber is not null and wo.state.previous is not null and wo.state.previous.value!='CANCELLED')";
				Query queryObj=persistenceService.getSession().createQuery(query);
				queryObj.setParameterList(String.valueOf("trIdList"), trIdList);

				Page unitPage=new Page(queryObj,1,WorksConstants.PAGE_SIZE);
				searchResult =new EgovPaginatedList(unitPage,queryObj.list().size()); 
			}else{
				searchResult=new EgovPaginatedList(0,0);
			}
		}*/

		Map<String,String> params=new HashMap<String,String>();
		String tenderFileQuery="select distinct tfd.abstractEstimate.estimateNumber from TenderFileDetail tfd where tfd.id is not null ";
		if(StringUtils.isNotBlank(status) && !getStatus().equals("-1"))	{
			tenderFileQuery = tenderFileQuery + " and tfd.tenderFile.egwStatus.code = :code";
			params.put("STATUS",status);
		}
		if(StringUtils.isNotBlank(getFileNum())){
			tenderFileQuery = tenderFileQuery + " and UPPER(tfd.tenderFile.fileNumber) like :fileNum";
			params.put("FILENUMBER","%"+getFileNum().trim().toUpperCase()+"%");
		}
		if(StringUtils.isNotBlank(noticeNumber)){
			tenderFileQuery = tenderFileQuery + " and tfd.tenderFile.fileNumber in (select tn.tenderFileRefNumber from TenderNotice tn where UPPER(tn.number) like :noticeNum)";
			params.put("NOTICENUMBER","%"+noticeNumber.trim().toUpperCase()+"%");
		}
		if(estimateId!=null && estimateId!=-1){
			tenderFileQuery = tenderFileQuery + " and tfd.abstractEstimate.id=:estimateId";
			params.put("ESTIMATEID",estimateId.toString());
		}

		String tenderResQuery="select gtr from org.egov.tender.model.GenericTenderResponse gtr,org.egov.tender.model.TenderUnit tu left outer join tu.tenderableGroups tg " +
				"where gtr.tenderUnit.id=tu.id and gtr.status.code='Accepted' and tg.number in ("+tenderFileQuery+") and "+
				"gtr.number not in (select wo.negotiationNumber from WorkOrder wo where wo.negotiationNumber is not null and wo.state.previous is null and wo.state.value='NEW')" +
				" and gtr.number not in (select wo.negotiationNumber from WorkOrder wo where wo.negotiationNumber is not null and wo.state.previous is not null and wo.state.previous.value!='CANCELLED')";;

				Query queryObj=persistenceService.getSession().createQuery(tenderResQuery);
				if(params.get("STATUS")!=null) {
					queryObj.setString("code", params.get("STATUS"));
				}
				if(params.get("FILENUMBER")!=null) {
					queryObj.setString("fileNum", params.get("FILENUMBER"));
				}
				if(params.get("NOTICENUMBER")!=null) {
					queryObj.setString("noticeNum", params.get("NOTICENUMBER"));
				}
				if(params.get("ESTIMATEID")!=null) {
					queryObj.setLong("estimateId", Long.parseLong(params.get("ESTIMATEID")));
				}

				//: TODO Needs to be enabled for e-tendering integration 
				/*if(pullDataForeTenderingFlag!=null && "true".equals(pullDataForeTenderingFlag)){
					try{
						List<ContractorQuote> contractorQuoteList = tenderResponseClient.getResponseFromeTender(params.get("NOTICENUMBER"));
						pagedResults = new EgovPaginatedList(1, 20);
						pagedResults.setList(contractorQuoteList);
					}catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				else
				{*/
					List bidResponseList=queryObj.list();
					Page unitPage=new Page(queryObj,getPage(),getPageSize());
					searchResult =new EgovPaginatedList(unitPage,bidResponseList.size()); 
				//}
				return SEARCH;
	}
	
	
	
	@Override
	public SearchQuery prepareQuery(String sortField, String sortOrder) {
		List<Object> paramList = new ArrayList<Object>();
		String query="from TenderFile tf where tf.id is not null ";
		if(criteriaMap.get("STATUS") != null) {
			/*System.out.println(criteriaMap.get("STATUS"));
			if(criteriaMap.get("STATUS").equals("APPROVED") || 
					criteriaMap.get("STATUS").equals("CANCELLED")){
				query = query + " and tf.state.previous.value = ? and " +
						" tf.id not in (select objectId from SetStatus where objectType='TenderFile')";
				paramList.add(criteriaMap.get("STATUS"));
			}
		}
			else*/ if(!criteriaMap.get("STATUS").equals("-1"))
			{
				query = query + " and tf.egwStatus.code = ?";
			paramList.add(criteriaMap.get("STATUS"));
			}
		}
		if(criteriaMap.get("FILENUMBER") != null){
			query = query + " and UPPER(tf.fileNumber) like '%"+criteriaMap.get("FILENUMBER").toString().trim().toUpperCase()+"%'";
		}
		if(criteriaMap.get("NOTICENUMBER") != null){
			query = query + " and tf.fileNumber in (select tn.tenderFileRefNumber from TenderNotice tn where " +
		"UPPER(tn.number) like '%"+criteriaMap.get("NOTICENUMBER").toString().trim().toUpperCase()+"%')";
		}
		if(criteriaMap.get("ESTIMATEID") != null){
			query = query + " and tf.id in(select tfd.tenderFile.id from TenderFileDetail tfd where tfd.abstractEstimate.id=? )";
			paramList.add(criteriaMap.get("ESTIMATEID"));
		}
		
		String tenderFileSearchQuery="select distinct tf "+	query;
		String countQuery = "select distinct count(tf) " + query;
		return new SearchQueryHQL(tenderFileSearchQuery, countQuery, paramList);
	}

	public TenderFile getTenderFile() {
		return tenderFile;
	}

	public void setTenderFile(TenderFile tenderFile) {
		this.tenderFile = tenderFile;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileNum() {
		return fileNum;
	}

	public void setFileNum(String fileNum) {
		this.fileNum = fileNum;
	}

	public String getNoticeNumber() {
		return noticeNumber;
	}

	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}

	public Long getEstimateId() {
		return estimateId;
	}

	public void setEstimateId(Long estimateId) {
		this.estimateId = estimateId;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	public String getPullDataForeTenderingFlag() {
		return pullDataForeTenderingFlag;
	}

	public void setPullDataForeTenderingFlag(String pullDataForeTenderingFlag) {
		this.pullDataForeTenderingFlag = pullDataForeTenderingFlag;
	}

	public void setTenderResponseClient(TenderResponseClient tenderResponseClient) {
		this.tenderResponseClient = tenderResponseClient;
	}
	
	public EgovPaginatedList getPagedResults()
	{
		return this.pagedResults;
	}
	
}
