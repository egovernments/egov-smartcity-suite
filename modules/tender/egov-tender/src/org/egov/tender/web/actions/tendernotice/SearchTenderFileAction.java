package org.egov.tender.web.actions.tendernotice;

import java.util.HashMap;
import java.util.Map;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.tender.model.TenderFileType;
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
public class SearchTenderFileAction extends SearchAction{
	
	private String tenderFileNumber;
	private String status;
	private String linkSource;

	@ValidationErrorPage(value=NEW)
	public String searchTenderFile()
	{
		Map<String,Object> paramMap=new HashMap<String,Object>();
		
		if(fromDate!=null)
			paramMap.put(TenderConstants.FROMDATE, fromDate);
		
		if(toDate!=null)
			paramMap.put(TenderConstants.TODATE,toDate);
		
		if(departmentId!=null)
			paramMap.put(TenderConstants.DEPARTMENT,departmentId);
		
		if(tenderFileNumber!=null && !"".equals(tenderFileNumber))
			paramMap.put(TenderConstants.TENDERFILENUMBER,tenderFileNumber);
		
		paramMap.put(TenderConstants.STATUS,TenderConstants.APPROVED);
		
		TenderFileType tenderFileType=(TenderFileType)persistenceService.find("from TenderFileType where fileType=?",fileType);
		
		paramMap.put(TenderConstants.TENDERFILEGROUPTYPE,tenderFileType.getGroupType());
		
		paginatedList=tenderNoticeService.searchTenderFile(paramMap,fileType,page);
		linkSource=tenderFileType.getLinkSource();
		mode=NEW;
		return NEW;
	}
	
	public String getLinkSource() {
		return linkSource;
	}

	public void validate()
	{
		if(departmentId==null)
			addActionError(getMessage("department.required"));
		if(fileType==null || "".equals(fileType))
			addActionError(getMessage("filetype.required"));
		if(fromDate!=null && toDate!=null && fromDate.after(toDate))
			addActionError(getMessage("fromdate.todate.required"));
	}
	
	public String getTenderFileNumber() {
		return tenderFileNumber;
	}

	public void setTenderFileNumber(String tenderFileNumber) {
		this.tenderFileNumber = tenderFileNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

}
