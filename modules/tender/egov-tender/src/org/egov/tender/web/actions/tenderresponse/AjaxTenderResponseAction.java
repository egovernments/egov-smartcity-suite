package org.egov.tender.web.actions.tenderresponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.services.common.TenderService;
import org.egov.tender.services.tenderresponse.TenderResponseService;
import org.egov.tender.utils.TenderConstants;
import org.egov.web.actions.BaseFormAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author pritiranjan
 *
 */

@SuppressWarnings("serial")

public class AjaxTenderResponseAction extends BaseFormAction{

	private TenderResponseService tenderResponseService;
	private Long idTemp;
	private Integer bidderId;
	private Long tenderUnit;
	private String fileType;
	private String type;
	private String number;
	
	public Object getModel() {
		return null;
	}
	
	public String getTenderDetails() throws IOException,JSONException 
	{
		SimpleDateFormat dateFormat=new SimpleDateFormat(TenderConstants.DATEPATTERN,Locale.getDefault());
		List<GenericTenderResponse> negotiationDetails=tenderResponseService.getNegotiationDetail(idTemp);
		JSONObject retJsonObj;
		List<JSONObject> jsonIndentsList = new ArrayList<JSONObject>();
		int outerCount=1;
		for(final GenericTenderResponse response : negotiationDetails) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("srlNo", outerCount);
			jsonObj.put("id", response.getId());
			jsonObj.put("responseNo", response.getNumber());
			jsonObj.put("status", response.getStatus().getCode());
			jsonObj.put("count", outerCount-1);
			jsonObj.put("date", dateFormat.format(response.getResponseDate()));
			List<JSONObject> jsonItemsList = new ArrayList<JSONObject>();
			int innerCount=1;
			for (final TenderResponseLine line: response.getResponseLines()) {
				JSONObject jsonLineObj = new JSONObject();
				jsonLineObj.put("slno", innerCount);
				jsonLineObj.put("item",
					(line.getTenderableEntity().getNumber()!=null && line.getTenderableEntity().getDescription() != null) ?
							line.getTenderableEntity().getNumber()+"-"+line.getTenderableEntity().getDescription():"");
				jsonLineObj.put("qty", line.getQuantityByUom().toString());
				jsonLineObj.put("estimatedrate", line.getTenderableEntity().getRequestedValue().toString());
				jsonLineObj.put("quotedrate", line.getBidRateByUom().toString());
				jsonLineObj.put("totalquotedrate", line.getTotalBidRateByUom().toString());
				jsonItemsList.add(jsonLineObj);
				innerCount++;
			}
			jsonObj.put("lines", new JSONArray(jsonItemsList));
			jsonIndentsList.add(jsonObj);
			outerCount++;
		}

		retJsonObj = new JSONObject()
			.put("startIndex", 0)
			.put("recordsPerPage", negotiationDetails.size())
			.put("totalRecords",negotiationDetails.size())
			.put("results", new JSONArray(jsonIndentsList));
		//.out.println("String---->"+retJsonObj.toString());
		
		return retJsonObj.toString();
	}
	
	//
	
	public void checkResponseForBidder() throws IOException
	{
		Boolean flag=Boolean.FALSE;
		TenderUnit unit=(TenderUnit)persistenceService.find("from TenderUnit where id=?",tenderUnit);
		flag=tenderResponseService.checkUniqueResponseForBidder(idTemp, Long.valueOf(bidderId), unit.getTenderNotice().getTenderFileType().getBidderType(), unit);
		ServletActionContext.getResponse().getWriter().write(flag.toString());
		
	}
	
	public String getApprovedEntity() {
		Map<String,String> resultDetails=null;
		if(!"-1".equals(fileType)){
			TenderService tenderService = tenderResponseService.getAsociatedTenderService(fileType);
			GenericTenderResponse response=tenderResponseService.findById(idTemp, false);
			resultDetails= tenderService.getApprovedEntityForBidResponse(response.getNumber());
			if(resultDetails.isEmpty()) {
				type="";
				number="";
			}
			else {
				type=resultDetails.get(TenderConstants.TYPE);
				number=resultDetails.get(TenderConstants.NUMBER);
			}
		}
		return "approvedWOorRC";
		}
	

	public void setTenderResponseService(TenderResponseService tenderResponseService) {
		this.tenderResponseService = tenderResponseService;
	}

	public void getNegotiationDetailsAsJson() throws IOException, JSONException {
		ServletActionContext.getResponse().getWriter().write(getTenderDetails());
	}

	public void setIdTemp(Long idTemp) {
		this.idTemp = idTemp;
	}
	
	public void setBidderId(Integer bidderId) {
		this.bidderId = bidderId;
	}

	public void setTenderUnit(Long tenderUnit) {
		this.tenderUnit = tenderUnit;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getType() {
		return type;
	}

	public String getNumber() {
		return number;
	}
	

}
