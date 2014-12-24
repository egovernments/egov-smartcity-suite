package org.egov.tender.web.actions.tenderresponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderableEntityGroup;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.tender.utils.TenderConstants;
import org.egov.tender.web.actions.common.SearchAction;
import org.egov.web.annotation.ValidationErrorPage;

/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("serial")
@ParentPackage("egov")
public class SearchTenderResponseAction extends SearchAction{

	private String noticeNumber;
	private Integer userId;
	private String fileNumber;
	private String groupNumber;
	private String responseNumber;
	private TenderResponseService tenderResponseService;
	private TenderCommonService tenderCommonService;
	private String cancelRemarks;
	private String sourcepage="";
	

    public void prepare()
	{
		super.prepare();
		List<TenderableEntityGroup> groupList=Collections.emptyList();
		List<GenericTenderResponse> responseList=Collections.emptyList();
		if(fileType != null && !"".equals(fileType))
			groupList=tenderCommonService.getGroupListByFileType(Long.valueOf(fileType));
		if(groupNumber!=null && !"".equals(groupNumber))
			responseList=tenderResponseService.getResponseListByReferenceNumber(groupNumber);
		if("cancelBidResponse".equals(sourcepage)) {
			dropdownData.remove("tenderFileTypeList");
			addDropdownData("tenderFileTypeList",persistenceService.findAllBy("from TenderFileType where isActive=1 and groupType in ('ESTIMATE','WORKS_RC_INDENT') order by fileType "));
		}
		addDropdownData("groupNumberList",groupList);
		addDropdownData("responseNumberList",responseList);
		addDropdownData("userList", persistenceService.findAllBy("select distinct tr.createdBy from org.egov.tender.model.GenericTenderResponse tr "));
	}
    
	
	
	
	
	@SkipValidation
	public String newform()
	{	
		return NEW;
	}
	
	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value=NEW)
	public String searchTenderResponse()
	{
		Map<String,Object> paramMap=new HashMap<String,Object>();
		
		if(fromDate!=null)
			paramMap.put(TenderConstants.FROMDATE,fromDate);
		
		if(toDate!=null)
			paramMap.put(TenderConstants.TODATE,toDate);
		
		if(fileNumber!=null && !"".equals(fileNumber.trim()))
			paramMap.put(TenderConstants.TENDERFILENUMBER,fileNumber.trim());
		
		if(noticeNumber!=null && !"".equals(noticeNumber.trim()))
			paramMap.put(TenderConstants.TENDERNOTICENUMBER,noticeNumber.trim());
		
		if(userId!=null && userId != -1)
			paramMap.put(TenderConstants.CREATEDBY,userId);
		
		if(groupNumber!=null && !"".equals(groupNumber))
			 paramMap.put(TenderConstants.GROUPNUMBER,groupNumber);
		
		if(responseNumber!=null && !"".equals(responseNumber))
			 paramMap.put(TenderConstants.RESPONSENUMBER,responseNumber);
		
		 paramMap.put(TenderConstants.DEPARTMENT,departmentId);
		 paramMap.put(TenderConstants.TENDERFILETYPE,Long.valueOf(fileType));
		 if("cancelBidResponse".equalsIgnoreCase(sourcepage)){
			 paramMap.put(TenderConstants.SEARCHMODE, TenderConstants.TENDERRESPONSE_APPROVED);		 
		 }
		
		paginatedList=tenderResponseService.searchTenderResponse(paramMap, page);
		List <GenericTenderResponse> finalResult = (paginatedList==null?null:paginatedList.getList());
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		if (finalResult != null){
			for (GenericTenderResponse response : finalResult) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("response",	response);
				map.put("availableStatus",tenderResponseService.getAvailableActions(response));
				mapList.add(map);
			}
		}
		if(paginatedList!=null)
			paginatedList.setList(mapList);
		mode=NEW;
		return NEW;
	}

	public  String getUsertName(Integer id)
    {
		String owner=tenderCommonService.getUsertName(id);
		return owner;
    }
	public void validate()
	{
		if(departmentId == null)
			addActionError(getMessage("department.required"));
		if(fileType == null || "".equals(fileType))
			addActionError(getMessage("noticetype.required"));
		/*if(fileNumber == null || "".equals(fileNumber))
			addActionError(getMessage("groupnumber.required"));
		if(responseNumber == null || "".equals(responseNumber))
			addActionError(getMessage("responsenumber.required"));*/
		if(fromDate!=null && toDate!=null && fromDate.after(toDate))
			addActionError(getMessage("fromdate.todate.required"));
	}
	
	public String getNoticeNumber() {
		return noticeNumber;
	}
	public void setNoticeNumber(String noticeNumber) {
		this.noticeNumber = noticeNumber;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	public String getGroupNumber() {
		return groupNumber;
	}
	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}
	public String getResponseNumber() {
		return responseNumber;
	}
	public void setResponseNumber(String responseNumber) {
		this.responseNumber = responseNumber;
	}
	public void setTenderResponseService(TenderResponseService tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}
	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
	}
	public String getCancelRemarks() {
		return cancelRemarks;
	}
	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}
	public String getSourcepage() {
		return sourcepage;
	}
	public void setSourcepage(String sourcepage) {
		this.sourcepage = sourcepage;
	}
	
	

	
}
