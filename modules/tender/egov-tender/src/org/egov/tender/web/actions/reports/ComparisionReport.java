package org.egov.tender.web.actions.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.tender.model.GenericTenderResponse;
import org.egov.tender.model.TenderResponseLine;
import org.egov.tender.model.TenderUnit;
import org.egov.tender.model.TenderableEntity;
import org.egov.tender.model.TenderableEntityGroup;
import org.egov.tender.services.common.GenericTenderService;
import org.egov.tender.services.common.TenderCommonService;
import org.egov.tender.utils.TenderConstants;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class ComparisionReport extends BaseFormAction{
	private Long tenderUnitId;
	private GenericTenderService genericTenderService;
	private ReportService reportService;
	private TenderCommonService tenderCommonService;
	private HashMap<String,Object> reportMapObject =new HashMap<String,Object>();
	private final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT,Locale.getDefault());
	private static final String DATE_FORMAT="dd/MM/yyyy";
	private static final String REPORT="report";
	private static final String TENDARABLEGROUPLABEL="TENDARABLEGROUPLABEL";
	private Integer reportId = -1;
	public Object getModel() { 
			return null;
	}
	@SkipValidation
	public String generateComparisionReport()
	{	TenderUnit unit=null;
	String tenderableGroupNumber="";
		List <GenericTenderResponse> responseList=null;
	//	Map<Long,Bidder> suppliers=new HashMap<Long,Bidder>();
		List<TenderResponseLine> responseLines=new ArrayList<TenderResponseLine>();
		if(tenderUnitId!=null){
			 unit=(TenderUnit)persistenceService.find("from TenderUnit where id=?",tenderUnitId);
			if(unit!=null)
				responseList=getGenericTenderService().getDistinctTenderResponsesByPassingTenderUnit(unit); 
			
			if(!responseList.isEmpty())
			{
				for(GenericTenderResponse response: responseList)
				{
					 //suppliers.put(response.getBidderId(),response.getBidder());
					responseLines.addAll(response.getResponseLines());
						
						tenderableGroupNumber = setTenderGroupNumberAndLabelForReport(
								tenderableGroupNumber, response);
				}
				reportMapObject.put("TENDARABLEGROUPNUMBER",tenderableGroupNumber);
				fillJasperReport(unit,responseList,responseLines);
				
				return REPORT;
			}
		}
	return "noResponses";
	}

	/**
	 * If tender group present, then first group value will be showed as Tenderable group number. Like estimate number etc.
	 * If group is null, then first entity will be showed as number. This will applicable for land estate module.
	 */
	private String setTenderGroupNumberAndLabelForReport(
			String tenderableGroupNumber, GenericTenderResponse response) {

		if (response.getTenderUnit().getTenderableGroups() != null
				&& !response.getTenderUnit().getTenderableGroups().isEmpty()) {
			tenderableGroupNumber = ((TenderableEntityGroup) response
					.getTenderUnit().getTenderableGroups().toArray()[0])
					.getNumber();
		} else if (response.getTenderUnit().getTenderableGroups() != null
				&& response.getTenderUnit().getTenderableGroups().isEmpty()
				&& !response.getTenderUnit().getTenderEntities().isEmpty()) {
			tenderableGroupNumber = ((TenderableEntity) response
					.getTenderUnit().getTenderEntities().toArray()[0])
					.getNumber();
		}
		if (response.getTenderUnit().getTenderNotice().getTenderFileType() != null
				&& (response.getTenderUnit().getTenderNotice()
						.getTenderFileType().getGroupType().equals(
								TenderConstants.FILETYPERATECONTRACT_INDENT) || response
						.getTenderUnit().getTenderNotice().getTenderFileType()
						.getGroupType().equals(
								TenderConstants.FILETYPEITEM_INDENT))) {
			reportMapObject.put(TENDARABLEGROUPLABEL,
					TenderConstants.FILETYPELABELINDENTNUMBER);
		} else if (response.getTenderUnit().getTenderNotice()
				.getTenderFileType() != null
				&& (response.getTenderUnit().getTenderNotice()
						.getTenderFileType().getGroupType().equals(
								TenderConstants.FILETYPEWORKS_RC_INDENT) || response
						.getTenderUnit().getTenderNotice().getTenderFileType()
						.getGroupType()
						.equals(TenderConstants.FILETYPEESTIMATE))) {
			reportMapObject.put(TENDARABLEGROUPLABEL,
					TenderConstants.FILETYPELABELESTIMATE);
		} else {
			reportMapObject.put(TENDARABLEGROUPLABEL,
					TenderConstants.FILETYPELABELGROUPNUMBER);
		}
		return tenderableGroupNumber;
	}
	/**
	 * This method used to fill jasper report (comparision report).Comparision report will be exported in pdf format. 
	 * @param unit
	 * @param responseList
	 * @param responseLines
	 */
	@SkipValidation
	public void fillJasperReport( TenderUnit unit, List<GenericTenderResponse> responseList, List<TenderResponseLine> responseLines){
	
		
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		reportMapObject = buildComparisionReportObject(unit, responseLines, request, session,reportMapObject);
		
		ReportRequest reportInput = new ReportRequest(TenderConstants.COMPARISIONREPORT_TEMPLATE,responseLines,reportMapObject);
		//reportInput.setReportFormat(FileFormat.HTM);  
		ReportOutput reportOutput = reportService.createReport(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportOutput,getSession());
	}
	/**
	 * Use this method to build report map . This map contains all the parameter which are used in comparision report.
	 * @param unit
	 * @param responseLines
	 * @param request
	 * @param session
	 * @param reportMapObject 
	 */
	private HashMap<String, Object> buildComparisionReportObject(TenderUnit unit,
			List<TenderResponseLine> responseLines, HttpServletRequest request,
			HttpSession session, HashMap<String, Object> reportMapObject) {
	
		reportMapObject.put("NAMEOFTHECITY",session.getAttribute("cityname"));
		reportMapObject.put("RESPONSELIST",responseLines);
		reportMapObject.put("logoPath",getTenderCommonService().getCityLogoName(request));
		reportMapObject.put("TENDERNOTICENUMBER",unit.getTenderNotice().getNumber());
		if(unit.getTenderNotice()!=null && unit.getTenderNotice().getNoticeDate()!=null)
			reportMapObject.put("TENDERNOTICEDATE",FORMATTER.format(unit.getTenderNotice().getNoticeDate()));
		reportMapObject.put("DEPARTMENTNAME",unit.getTenderNotice().getDepartment().getDeptName());
		reportMapObject.put("TENDERFILENUMBER",unit.getTenderNotice().getTenderFileRefNumber());
		//reportMapObject.put("TENDARABLEGROUPNUMBER",unit.getTenderGroupRefNumber());
		return reportMapObject;
	}
	
	public Long getTenderUnitId() {
		return tenderUnitId;
	}

	public void setTenderUnitId(Long tenderUnitId) {
		this.tenderUnitId = tenderUnitId;
	}

	public GenericTenderService getGenericTenderService() {
		return genericTenderService;
	}

	public void setGenericTenderService(GenericTenderService genericTenderService) {
		this.genericTenderService = genericTenderService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}
	public TenderCommonService getTenderCommonService() {
		return tenderCommonService;
	}
	public void setTenderCommonService(TenderCommonService tenderCommonService) {
		this.tenderCommonService = tenderCommonService;
	}

		
}
