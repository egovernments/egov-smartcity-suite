package org.egov.tender.web.actions.tendernotice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.utils.DateUtils;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.service.EisUtilService;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.utils.TenderConstants;
import org.egov.tender.web.actions.common.SearchAction;
import org.egov.web.annotation.ValidationErrorPage;


/**
 * 
 * @author pritiranjan
 *
 */

@ParentPackage("egov")
@SuppressWarnings("serial")
public class SearchTenderNoticeAction extends SearchAction{
	
	private String tenderNoticeNumber;
	//private Long notice;
	private List<TenderUnit> tenderUnits;
	private List<TenderNotice> noticeList;
	private String tenderFileTypeName;
	private String cancelRemarks;
	private String sourcepage="";
	private String filetypeNumber;
	private EisUtilService eisService;
    private static final Map<Integer,String> userMap=new HashMap<Integer,String>();
	
	
	public String getFiletypeNumber() {
		return filetypeNumber;
	}

	public void setFiletypeNumber(String filetypeNumber) {
		this.filetypeNumber = filetypeNumber;
	}

	public String getTenderFileTypeName() {
		return tenderFileTypeName;
	}

	public void setTenderFileTypeName(String tenderFileTypeName) {
		this.tenderFileTypeName = tenderFileTypeName;
	}
	
	public void prepare()
	{
		super.prepare();
		if("cancelTenderNotice".equals(sourcepage)) {
			dropdownData.remove("tenderFileTypeList");
			addDropdownData("tenderFileTypeList",persistenceService.findAllBy("from TenderFileType where isActive=1 and groupType in ('ESTIMATE','WORKS_RC_INDENT') order by fileType "));
		}
	}
	
	
	public  String getUsertName(Integer id)
    {
		
		if(!userMap.containsKey(id))
		{
			User user=eisService.getUserForPosition(id,DateUtils.today());
			if(user!=null)
				userMap.put(id, user.getUserName());
		}
		return userMap.get(id);
	  }
	@ValidationErrorPage(value=NEW)
	public String searchTenderNotice()
	{
		Map<String,Object> paramMap=new HashMap<String, Object>();
		
		if(fromDate!=null)
			paramMap.put(TenderConstants.FROMDATE,fromDate);
		
		if(toDate!=null)
			paramMap.put(TenderConstants.TODATE,toDate);
		
		if(departmentId!=null)
			paramMap.put(TenderConstants.DEPARTMENT,departmentId);
		
		if(tenderNoticeNumber!=null && !"".equals(tenderNoticeNumber))
			paramMap.put(TenderConstants.TENDERNOTICENUMBER,tenderNoticeNumber);
		
		if(fileType!=null && !"".equals(fileType)){
			paramMap.put(TenderConstants.TENDERFILETYPE,Long.valueOf(fileType));
		}
		if(filetypeNumber!=null&&!"".equals(filetypeNumber)){
			paramMap.put(TenderConstants.TENDERFILETYPENUMBER,filetypeNumber);
		}
		if(searchMode == null || "".equals(searchMode))
			paginatedList=tenderNoticeService.searchTenderNotice(paramMap,page);
		else{
			paramMap.put(TenderConstants.SEARCHMODE, searchMode);
			noticeList=tenderNoticeService.searchTenderNoticeList(paramMap);
		}
		if("cancelTenderNotice".equalsIgnoreCase(sourcepage)){
			 paramMap.put(TenderConstants.OBJECT_STATUS, TenderConstants.TENDERNOTICE_APPROVED);
			 paginatedList=tenderNoticeService.searchTenderNotice(paramMap,page);
		 }
		
		if(fileType!=null&&!"".equals(fileType))
		tenderFileTypeName=tenderNoticeService.getTenderFileTypebyId(Long.parseLong(fileType)).getDescription();
		mode=NEW;
		return NEW;
	}
	
	public List<TenderNotice> getNoticeList() {
		return noticeList;
	}

	public void validate()
	{
		//if(departmentId==null)
			//addActionError(getMessage("department.required"));
		if(fileType==null || "".equals(fileType))
			addActionError(getMessage("filetype.required"));
		if(fromDate!=null && toDate!=null && fromDate.after(toDate))
			addActionError(getMessage("fromdate.todate.required"));
	}
	
	public List<TenderUnit> getTenderUnits() {
		return tenderUnits;
	}
	
	public String getTenderNoticeNumber() {
		return tenderNoticeNumber;
	}

	public void setTenderNoticeNumber(String tenderNoticeNumber) {
		this.tenderNoticeNumber = tenderNoticeNumber;
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
	   public void setEisService(EisUtilService eisService) {
			this.eisService = eisService;
		}
	
	
}
